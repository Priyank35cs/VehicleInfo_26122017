package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import app.efleet.vehicleinfo.R;

public class WebViewActivity extends Activity {
    private WebView webview;
    ProgressDialog pb;
    ImageView imgShare;
    String imgPath;
    String fileName;
    File file;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
        setContentView(R.layout.activity_web_view);
        imgShare = (ImageView) findViewById(R.id.imgShareLogo);
        back=(ImageButton)findViewById(R.id.imgback);
        pb = new ProgressDialog(this);
        pb.setMessage("Please Wait....");
        pb.setCancelable(true);
        pb.show();
        webview=(WebView)findViewById(R.id.webView);
       // String img = SalesZenPrefs.getString(getApplicationContext(), "img");
        imgPath =getIntent().getStringExtra("img");
        imgPath=imgPath.replace(" ","%20");
        String pdf = imgPath;
        fileName = pdf.substring(pdf.lastIndexOf('/') + 1);
        String newpdf = pdf.substring(pdf.length()-4);
        String Ext = ".pdf";
        if(newpdf.equalsIgnoreCase(Ext)) {
           // WebView webview = new WebView(this);
            Log.e("Workin","webviewActivity");
            getbmpfromURL(pdf);
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webview.getSettings().setJavaScriptEnabled(true);
           // new line added
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);


            WebSettings settings = webview.getSettings();
            settings.setBuiltInZoomControls(true);
            settings.setSupportZoom(true);
            //webview.loadUrl(pdf);
            webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pdf);
           // setContentView(webview);
            webview.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    pb.dismiss();
                 // String  s= new string();
                }
            });
        }
        else {
            Intent intent = new Intent(WebViewActivity.this, ImageViewerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Log.e("Go","imageActivity");
            intent.putExtra("img",imgPath);
            startActivity(intent);
            pb.dismiss();
            finish();
        }

        imgShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Uri uri =Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
//                BitmapFactory.decodeResource(getResources(), R.drawable.duedoc), null, null));
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                Uri uri=Uri.fromFile(file);
                //share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_STREAM, uri);

                // share.putExtra(Intent.EXTRA_TEXT, webview.getUrl());
               // share.setType("text/plain");
               // share.setType("*/*");
               // Uri imageUri=Uri.parse("android.resource://"+getPackageName()+"/drawable"+R.drawable.duedoc)‌​;
                // Uri uri = Uri.parse("file://" + webview.getUrl());
                //Log.e("uri",uri.toString());
               // share.putExtra(Intent.EXTRA_STREAM, webview.getUrl());
                //share.putExtra(Intent.EXTRA_STREAM,uri);

//                share.setType("image/*");
//                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageState()));

                //share.putExtra(Intent.EXTRA_STREAM, Uri.parse(webview.getUrl()));
                startActivity(Intent.createChooser(share, "Share image using"));


            }

            });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "share image");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(WebViewActivity.this, "calling code", Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="share image")
        {

            // This is the code which i am using for share intent
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/*");
            Uri uri=Uri.fromFile(file);
            //share.putExtra(Intent.EXTRA_STREAM, imageUri);
            share.putExtra(Intent.EXTRA_STREAM, uri);

            //share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageState()));
            startActivity(Intent.createChooser(share, "Share image using"));

        }else{
            return false;
        }
        return true;
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
