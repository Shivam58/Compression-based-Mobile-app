package com.example.justcompress;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip extends AppCompatActivity {

    public static String filepath,destination,filepath1;
    public static ProgressDialog pd;
    public static boolean i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unzip);
        filepath =this.getIntent().getStringExtra("zipped");
        destination=filepath.substring(filepath.lastIndexOf("/")+1);
        pd=new ProgressDialog(this);
        TextView textView2=(TextView)findViewById(R.id.textView2);
        textView2.setText(filepath);
        Button button=(Button)findViewById(R.id.button);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               new LongOperation4().execute("");
           }
       });
    }

    void compress_run()
    {
        TextView textView4=(TextView)findViewById(R.id.textView4);
        textView4.setText(Environment.getExternalStorageDirectory()+"/Download/"+destination);
    }


    public static boolean unzip(File zipFile, String location) throws IOException {
        boolean success=false;
        try {
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    }
                    else {
                        FileOutputStream fout = new FileOutputStream(path, false);
                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                                success=true;
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
           // Log.e(TAG, "Unzip exception", e);
        }
        return success;
    }

    /*public static boolean unzip(File zipFile, File targetDirectory) throws IOException {
        boolean success=false;
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                    success=true;
                } finally {
                    fout.close();
                }
            *//* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            *//*
            }
        } finally {
            zis.close();
        }
        return success;
    }*/


    public class LongOperation4 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Unzip.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                File file=new File(filepath);
                File target=new File(Environment.getExternalStorageDirectory()+"/Download/");
                i=Unzip.unzip(file,Environment.getExternalStorageDirectory()+"/Download");
            } catch (Throwable e) {
                Log.e("test", "vk run exception.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            if (i) {
                Toast.makeText(Unzip.this, "File Decompressed Successfully", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "File Saved with success at: " + Environment.getExternalStorageDirectory() + "/Download/" + destination, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during File saving", Toast.LENGTH_LONG).show();
            }
            compress_run();
        }

        @Override
        protected void onPreExecute() {
            i = false;
            pd.setMessage(".....DeCompressing ");
            pd.show();
            pd.setCancelable(false);
            //       dialog.cancel();

        }

    }
}
