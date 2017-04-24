package com.example.justcompress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String filepath,destination,filepath1;
    public static ProgressDialog pd;
    public static boolean i;
    public static Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        filepath =this.getIntent().getStringExtra("doc");
        pd=new ProgressDialog(this);
        TextView textView4=(TextView)findViewById(R.id.textView);
        textView4.setText(filepath);
        Button button8=(Button)findViewById(R.id.button);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LongOperation4().execute("");
            }
        });
    }


    void compress_run()
    {
        TextView textView=(TextView)findViewById(R.id.textView5);
        textView.setText(filepath1);

    }

    static boolean zip()throws IOException
    {
        boolean success=false;
        /* final String OUTPUT_ZIP_FILE = Environment.getExternalStorageDirectory() + "/Download"+"/AXJ.zip";
         final String SOURCE_FOLDER = filepath;

        AppZip appZip = new AppZip();
        appZip.generateFileList(new File(SOURCE_FOLDER),SOURCE_FOLDER);
        success=appZip.zipIt(OUTPUT_ZIP_FILE,SOURCE_FOLDER);

*/
        byte[] buffer = new byte[1024];
        destination=filepath.substring(filepath.lastIndexOf("/")+1);
        filepath1 = Environment.getExternalStorageDirectory() + "/Download/"+destination+".zip";
        Log.e("Filepath is: ",filepath);
        Log.e("Filepath1 is: ",filepath1);
        Log.e("Destination is: ",destination);
        FileOutputStream fos = new FileOutputStream(filepath1);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry ze= new ZipEntry(destination);
        zos.putNextEntry(ze);
        FileInputStream in = new FileInputStream(filepath);

        int len;
        while ((len = in.read(buffer)) > 0) {
            zos.write(buffer, 0, len);
            success=true;
        }

        in.close();
        zos.closeEntry();

        //remember close it
        zos.close();

        System.out.println("Done");
        return success;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.zip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.compress) {
            // Handle the first screen
            Intent i=new Intent(Zip.this,MainActivity.class);
            startActivity(i);
        }  else if (id == R.id.about) {
            Fragment fragment = new About();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.navig_layout,fragment,"About");
            fragmentTransaction.commitAllowingStateLoss();
            //ft.add(new About(),null);
        } else if (id == R.id.rate_me) {
            dialog=new Dialog(Zip.this);
            dialog.setTitle("Rate Us:- ");
            dialog.setContentView(R.layout.rate_me);
            dialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LongOperation4 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Zip.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                i=Zip.zip();
            } catch (Throwable e) {
                Log.e("test", "vk run exception.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            Toast.makeText(Zip.this, "File Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "File Saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during File saving", Toast.LENGTH_LONG).show();
            }
            compress_run();
        }

        @Override
        protected void onPreExecute() {
            i=false;
            pd.setMessage(".....Compressing ");
            pd.show();
            pd.setCancelable(false);
     //       dialog.cancel();

        }

    }
}
