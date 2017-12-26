package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.efleet.vehicleinfo.Model.Touch;
import app.efleet.vehicleinfo.R;

public class ImageViewerActivity extends Activity {
    ImageView imageview;
    ImageView imgShare;
    Bitmap bitmap;
    Uri bmpUri;
    String fileName;
    File file;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_viewer);
        imageview = (ImageView) findViewById(R.id.image);
        imgShare = (ImageView) findViewById(R.id.imgShareLogo);
        back=(ImageButton)findViewById(R.id.imgback);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
       // String img = SalesZenPrefs.getString(getApplicationContext(), "img");
        String img = getIntent().getStringExtra("img");
        Log.e("image",img);
        //String pdf = "http://docs.google.com/gview?embedded=true&url=" +img;
        img=img.replace(" ","%20");
        String pdf =  img;
        fileName = pdf.substring(pdf.lastIndexOf('/') + 1);
        Log.e("PdfPath",pdf);
        getbmpfromURL(pdf);
        Picasso.with(getApplicationContext())
                .load(img)
                .resize(1100,1700)
                .into(imageview);
               // .load(pdf)
                //.fit()
                //.centerInside()

        //  bmpUri = getLocalBitmapUri(imageview);
       // bitmap = ((BitmapDrawable)imageview.getDrawable()).getBitmap();
        imageview.setOnTouchListener(new Touch());

        imgShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //String path = MediaStore.Images.Media.insertImage(ImageViewerActivity.this.getContentResolver(), bitmap, "Title", null);
               // Uri imageUri = Uri.parse(path);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                Uri uri=Uri.fromFile(file);
                share.putExtra(Intent.EXTRA_STREAM, uri);
               /* ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));*/
                startActivity(Intent.createChooser(share, "Share Image"));

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Vehicle Info");
//                String sAux = "\nLet me recommend you this application\n\n";
//                sAux = sAux + "https://play.google.com/store/apps/details?id="+getBaseContext().getPackageName()+"\n\n";
//                intent.putExtra(Intent.EXTRA_TEXT, sAux);
//                startActivity(Intent.createChooser(intent, "Choose One"));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
          //  out.close();
            // **Warning:** This will fail for API > 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public void getbmpfromURL(String surl){
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
            // Bitmap mIcon = BitmapFactory.decodeStream(in);
            //imageView.setImageBitmap(mIcon);
            //return  mIcon;
        } catch (Exception e) {
            Log.e("Error1", e.getMessage());
            e.printStackTrace();

        }

    }
}
