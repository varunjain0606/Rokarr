package com.client.rokarr;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.client.rokarr.customView.CustomGridView;
import com.client.rokarr.loginandregistration.helper.SQLiteHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Varun on 21-11-2015.
 */
public class PrintPreview extends AppCompatActivity {

    ArrayList<String> prodDetails,prodPrice,quantity,totalamt;
    TextView clientname,clientaddress,total,price,disc,billno, date,addressLine1, addressLine2,companyName,tin,cst, contact1,paid;
    CustomGridView customGrid;
    private SQLiteHandler sqLiteHandler;
    Uri imageUri;
    Button printshare, print1, camera;
    ListView listdata;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printlayout);
//        imageUri = (Uri) savedInstanceState.getBinder("imageuri");
        prodDetails = new ArrayList<String>();
        prodPrice = new ArrayList<String>();
        quantity = new ArrayList<String>();
        totalamt = new ArrayList<String>();
        setfield();
        buttonlistener();
        sqLiteHandler = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = sqLiteHandler.getUserDetails();
        if (user != null) {
            String company = user.get("companyName");
            String address1 = user.get("address1");
            String address2 = user.get("address2");
            String tinno = user.get("tin");
            String cstno = user.get("cst");
            String contact = user.get("contact");

            // Displaying the user details on the screen
            if (company != null && address1 != null && address2 != null && tin != null && cst != null) {
                companyName.setText(company);
                addressLine1.setText(address1);
                addressLine2.setText(address2);
                cst.setText(cstno);
                tin.setText(tinno);
                contact1.setText(contact);
            }
        }
            Bundle bundle = getIntent().getExtras();
            clientname.setText(bundle.getString("name1"));
            clientaddress.setText(bundle.getString("address1"));
            total.setText(bundle.getString("netamount"));
            price.setText(bundle.getString("price"));
            date.setText(bundle.getString("presentdate"));
            billno.setText(bundle.getString("billNo"));
            disc.setText(bundle.getString("disco"));
            paid.setText(bundle.getString("paidamt"));
            prodDetails = bundle.getStringArrayList("listproduct1");
            prodPrice = bundle.getStringArrayList("listprice1");
            quantity = bundle.getStringArrayList("listquantity1");
            totalamt = bundle.getStringArrayList("listTotal1");
            customGrid = new CustomGridView(this, prodDetails, prodPrice, quantity, totalamt, 2);
            listdata.setAdapter(customGrid);

    }

    void setfield()
    {
        listdata = (ListView) findViewById(R.id.listView2);
        clientname = (TextView) findViewById(R.id.client_name);
        clientaddress = (TextView) findViewById(R.id.client_address);
        total = (TextView) findViewById(R.id.printAmt);
        price = (TextView) findViewById(R.id.printPrice);
        disc = (TextView) findViewById(R.id.printDiscount);
        print1 = (Button) findViewById(R.id.bt_print);
        printshare = (Button) findViewById(R.id.bt_share);
        billno = (TextView) findViewById(R.id.clientBillNo);
        date = (TextView) findViewById(R.id.billDate);
        companyName = (TextView) findViewById(R.id.compay_name);
        tin = (TextView) findViewById(R.id.TINshop);
        cst = (TextView) findViewById(R.id.CSTshop);
        addressLine1 = (TextView) findViewById(R.id.addressLine1);
        addressLine2 = (TextView) findViewById(R.id.addressLine2);
        contact1 = (TextView) findViewById(R.id.telephone);
        paid = (TextView)findViewById(R.id.printAmtPaid);
    }

    void buttonlistener()
    {
        LinearLayout layout=  (LinearLayout) findViewById(R.id.printscreen);
        final LinearLayout finalLayout = layout;
        print1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap map = ConvertToBitmap(finalLayout);
                shareImage(map, 3);
                finish();
            }
        });

        printshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap map = ConvertToBitmap(finalLayout);
                LayoutInflater layoutInflater
                        = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);

                ImageView whatsapp = (ImageView) popupView.findViewById(R.id.whats);
                whatsapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean installed = appInstalledOrNot("com.whatsapp");
                        if (installed == false) {
                            Toast.makeText(getApplicationContext(), "Whatsapp Not installed! Please install", Toast.LENGTH_SHORT).show();
                        } else {
                            shareImage(map, 2);
                            finish();
                        }
                    }

                });
                ImageView mailer = (ImageView) popupView.findViewById(R.id.gmail);
                mailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean installed = appInstalledOrNot("com.google.android.gm");
                        if (installed == false) {
                            Toast.makeText(getApplicationContext(), "Gmail Not installed! Please Install", Toast.LENGTH_SHORT).show();
                        } else {
                            shareImage(map, 1);
                            finish();
                        }
                    }
                });
                Button btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

                popupWindow.showAsDropDown(printshare, -30, -180);
            }
        });
            camera = (Button) findViewById(R.id.bt_camera);
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c = Calendar.getInstance();
                    int yy = c.get(Calendar.YEAR);
                    int mm = c.get(Calendar.MONTH)+1;
                    int dd = c.get(Calendar.DAY_OF_MONTH);
                    int hr = c.get(Calendar.HOUR);
                    int min = c.get(Calendar.MINUTE);
                    int sec = c.get(Calendar.SECOND);
                    String date = dd + "-" + mm + "-" + yy;
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator  + "SimpleBill"
                            + File.separator + "Backup" + File.separator + date);
                    if(!folder.exists())
                    {
                        folder.mkdirs();
                    }

                    File f = new File(folder + File.separator + "cam" +
                            billno.getText().toString() + "_" + hr+min+sec+ ".png");
                    Intent camera= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    imageUri = Uri.fromFile(f);
                    camera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(camera, 1);
                }
            });

            }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    if(data != null) {
                        Uri image = data.getData();
                        Toast.makeText(this, "Image saved to:\n" +
                                image, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Image saved to:\n" +
                                imageUri.toString(), Toast.LENGTH_LONG).show();
                    }
                    // TODO Update your TextView.
                }
                break;
            }
        }
    }

    protected Bitmap ConvertToBitmap(LinearLayout layout) {
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(),
                Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }

    public void shareImage(Bitmap bitmap, int id){

        Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH)+1;
        int dd = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        String date = dd + "-" + mm + "-" + yy;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        boolean flag = false;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator  + "Rokarr"
                + File.separator + "Backup" + File.separator + date);
        if(!folder.exists())
        {
            folder.mkdirs();
        }

            File f = new File(folder + File.separator +
                    billno.getText().toString() + "_" + hr+min+sec+ ".jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (id == 1)
                share.setPackage("com.google.android.gm");
            else if (id == 2) {
                share.setPackage("com.whatsapp");
            }
            else if (id == 3) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    Intent sharingIntent = new Intent(
                            android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("image/png");
                    sharingIntent
                            .setComponent(new ComponentName(
                                    "com.android.bluetooth",
                                    "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
                } else {
                    ContentValues values = new ContentValues();
                    values.put(BluetoothShare.URI, f.toString());
                    values.put(BluetoothShare.DIRECTION,
                            BluetoothShare.DIRECTION_OUTBOUND);
                    Long ts = System.currentTimeMillis();
                    values.put(BluetoothShare.TIMESTAMP, ts);
                    getContentResolver().insert(BluetoothShare.CONTENT_URI,
                            values);
                }
            }
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            startActivity(Intent.createChooser(share, "Share image using"));
        }



    private boolean appInstalledOrNot(String uri)
    {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            app_installed = false;
        }
        return app_installed ;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
//        savedInstanceState.putCharSequence("imageuri", (CharSequence) imageUri);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}

