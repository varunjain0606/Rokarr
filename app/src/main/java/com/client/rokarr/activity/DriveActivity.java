package com.client.rokarr.activity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.client.rokarr.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import android.view.ViewGroup.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by Varun on 11-12-2015.
 */
public class DriveActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "DriveActivity";
    private GoogleApiClient api;
    private boolean mResolvingError = false;
    private DriveFile mfile;
    private static final int DIALOG_ERROR_CODE =100;
    private static final String DATABASE_NAME = "localdata";
    private static final String GOOGLE_DRIVE_FILE_NAME = "rokarr_backup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater
                = (LayoutInflater) getApplicationContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popupbackup, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        // Create the Drive API instance
        api = new GoogleApiClient.Builder(this).addApi(Drive.API).addScope(Drive.SCOPE_FILE).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        //popupWindow.showAsDropDown(popupView, -30, -180);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mResolvingError) {
            api.connect(); // Connect the client to Google Drive
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        api.disconnect(); // Disconnect the client from Google Drive
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "Connected successfully");

        /* Connection to Google Drive established. Now request for Contents instance, which can be used to provide file contents.
           The callback is registered for the same. */
        //Drive.DriveApi.newContents(api).setResultCallback(contentsCallback);
        Drive.DriveApi.newDriveContents(api).setResultCallback(contentsCallback);
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "Connection failed");
        if(mResolvingError) { // If already in resolution state, just return.
            return;
        } else if(result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);
            } catch (SendIntentException e) {
                e.printStackTrace();
            }
        } else { // Error cannot be resolved. Display Error Dialog stating the reason if possible.
            ErrorDialogFragment fragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putInt("error", result.getErrorCode());
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "errordialog");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DIALOG_ERROR_CODE) {
            mResolvingError = false;
            if(resultCode == RESULT_OK) { // Error was resolved, now connect to the client if not done so.
                if(!api.isConnecting() && !api.isConnected()) {
                    api.connect();
                }
            }
        }
    }

    final private ResultCallback<DriveApi.DriveContentsResult> contentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
            if (!driveContentsResult.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create new file contents");
                return;
            }

            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(DATABASE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();
            // create a file on root folder
            Drive.DriveApi.getRootFolder(api)
                    .createFile(api, changeSet, driveContentsResult.getDriveContents())
                    .setResultCallback(fileCallback);

        }
    };

    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create the file");
                return;
            }
            mfile = result.getDriveFile();
            mfile.open(api, DriveFile.MODE_WRITE_ONLY, new DriveFile.DownloadProgressListener() {
                @Override
                public void onProgress(long bytesDownloaded, long bytesExpected) {
                    //DialogFragment_Sync.setProgressText("Creating backup file... ("+bytesDownloaded+"/"+bytesExpected+")");
                }
            }).setResultCallback(contentsOpenedCallback);
        }
    };

    final private ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
            if (!driveContentsResult.getStatus().isSuccess()) {
                Log.v(TAG, "Error opening file, try again");
                return;
            }

            byte[] buffer = new byte[1024];
            DriveContents contents = driveContentsResult.getDriveContents();
            BufferedOutputStream out = new BufferedOutputStream(contents.getOutputStream());
            int n = 0;
            try {
                FileInputStream is = new FileInputStream(getDbPath());
                BufferedInputStream in = new BufferedInputStream(is);
                while( ( n = in.read(buffer) ) >= 0 ) {
                    out.write(buffer, 0, n);
                }

                in.close();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            contents.commit(api, null).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status result) {
                    Toast.makeText(getApplication(),"Backing up...",Toast.LENGTH_LONG).show();
                    // Handle the response status
                }
            });
        }
    };

    private File getDbPath() {
        File file = new File( Environment.getExternalStorageDirectory()
                + File.separator + "Rokarr"
                + File.separator + DATABASE_NAME);
        return file;
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {}

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt("error");
            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), DIALOG_ERROR_CODE);
        }

        public void onDismiss(DialogInterface dialog) {
            ((DriveActivity) getActivity()).onDialogDismissed();
        }
    }

}
