package app.efleet.vehicleinfo.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import app.efleet.vehicleinfo.Model.Downloader;
import app.efleet.vehicleinfo.R;

import static android.icu.util.MeasureUnit.MEGABYTE;

public class MyimageView extends AppCompatActivity {
    String imgPath;
    String pdf;
    ImageView imageView;
    Bitmap bitmap;
    ImageView imgShare;
    Uri imageUri=null;
    String fileName;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myimage_view);
        imageView = (ImageView) findViewById(R.id.image);
        imgShare = (ImageView) findViewById(R.id.imgShareLogo);
        imgPath = getIntent().getStringExtra("img");
        String pdf=imgPath;
       // String pdf = "http://docs.google.com/gview?embedded=true&url=" +imgPath;
        Log.e("Path",pdf);
          fileName = pdf.substring(pdf.lastIndexOf('/') + 1);
        Log.e("fileName",fileName);
        bitmap=getbmpfromURL(pdf);
        imageView.setImageBitmap(bitmap);

        imgShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                String path = MediaStore.Images.Media.insertImage(MyimageView.this.getContentResolver(), bitmap, "Title", null);
//                Uri imageUri = Uri.parse(path);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
               // Uri uri = FileProvider.getUriForFile(MyimageView.this, getPackageName(), file);
                Uri uri=Uri.fromFile(file);
                //share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(share, "Share Image"));


            }
        });
        //String pdf = "http://docs.google.com/gview?embedded=true&url=" + imgPath;
       /* URL url = null;
        try {
            url = new URL(pdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
    public Bitmap getbmpfromURL(String surl){
        try {
            URL url = new URL(surl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
           // urlcon.setRequestMethod("GET");
            //urlcon.setDoOutput(true);
           // urlcon.connect();
            //urlcon.setDoInput(true);

            InputStream in = urlcon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
           // File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
              file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
           // file = new File(MyimageView.this.getFilesDir(), fileName);
            FileOutputStream out = new FileOutputStream(file);
            byte data[] = new byte[1024];


            int count= 0;

            while ((count = bis.read(data))>0) {
                out.write(data, 0, count);


            }
           // out.close();
            Log.e("Response",in.toString());
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            //imageView.setImageBitmap(mIcon);
            return  mIcon;
        } catch (Exception e) {
            Log.e("Error1", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}