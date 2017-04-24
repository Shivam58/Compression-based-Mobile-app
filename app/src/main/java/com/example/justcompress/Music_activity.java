package com.example.justcompress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Music_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,About.OnFragmentInteractionListener {
    public VideoView videoView2;
    static String filepath,destination;
    public static Dialog dialog,dialog1;
    public boolean i;
    public static ProgressDialog pd;
    public static TextView textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd=new ProgressDialog(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        VideoView vv=(VideoView)findViewById(R.id.videoView);
        filepath =this.getIntent().getStringExtra("music");
        destination = filepath.substring(filepath.lastIndexOf("/") + 1);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        //Bitmap bitmap=(Bitmap)this.getIntent().getParcelableExtra("music");
        vv.setVideoURI(Uri.parse(getIntent().getStringExtra("music")));
        MediaController mc=new MediaController(this);
        mc.setAnchorView(vv);
        vv.start();
        vv.setMediaController(mc);
         videoView2 = (VideoView) findViewById(R.id.videoView2);
        MediaController mc1 = new MediaController(this);
        mc1.setAnchorView(videoView2);
        File file=new File(filepath);
        Long length=file.length();
        length=length/1024;
        textView1.setText(" "+length+" KB");
        videoView2.setMediaController(mc1);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"You download is resumed",Toast.LENGTH_LONG).show();
/*
                Fragment fragment = new Music_save();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.navig_layout,fragment,"Music_save");
                fragmentTransaction.commitAllowingStateLoss();
*/
                //return false;
                dialog=new Dialog(Music_activity.this);
                dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_save_music);
                dialog.show();

                Button button1=(Button)dialog.findViewById(R.id.button1);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new LongOperation().execute("");
                    }
                });

                Button button2=(Button)dialog.findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new LongOperation2().execute("");
                    }
                });


            }
        });


       /* button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                GeneralUtils.checkForPermissionsMAndAbove(Music_activity.this, true);
                LoadJNI vk = new LoadJNI();
                boolean z = false;
                try {
                    String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    *//*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    *//*
                    //String[] complex={"ffmpeg","-y","-i",filepath,Environment.getExternalStorageDirectory()+"/Download/out.m4a"};
                    //String commandStr = "ffmpeg -y -i /sdcard/vk2/in.wav -ar 44100 -ac 2 -ab 64k -f mp3 /sdcard/videokit/out.mp3";
                    String[] commandStr ={"ffmpeg","-y","-i",filepath,"-strict","experimental","-acodec","copy","-ss","00:00:00","-t","00:02:03.000",Environment.getExternalStorageDirectory() + "/Download/out.mp3"};
                    String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-ar","44100","-ac","2","-ab","64k","-f","mp3", Environment.getExternalStorageDirectory() + "/Download/out.mp3"};
                    vk.run(commandStr, workFolder, getApplicationContext());
                    Log.i("test", "ffmpeg4android finished successfully");
                    z = true;
                } catch (Throwable e) {
                    Log.e("test", "vk run exception.", e);
                }

                Toast.makeText(Music_activity.this, "Music Compressed Successfully", Toast.LENGTH_LONG).show();
                if (z) {
                   Toast.makeText(getApplicationContext(), "Music saved with success at: " + Environment.getExternalStorageDirectory() + "/Download/out.mp3", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error during Music saving", Toast.LENGTH_LONG).show();
                }
                return false;

            }
        });
*/
    }

    public void compress_run(int i)
    {
        String videopath=null;
        if(i==1)
         videopath = Environment.getExternalStorageDirectory() + "/Download/"+destination;
        else
             videopath = Environment.getExternalStorageDirectory() + "/Download/"+destination+".zip";

        File file=new File(videopath);
        Long length=file.length();
        length=length/1024;
        textView2.setText(" "+length+" KB");



        if(i==1) {
            videopath = Environment.getExternalStorageDirectory() + "/Download/" + destination;
            Uri video = Uri.parse(Environment.getExternalStorageDirectory() + "/Download/"+destination);
            videoView2.setVideoURI(video);
            videoView2.setVideoPath(videopath);
            videoView2.start();
        }
    }

    static boolean zip()throws IOException
    { boolean success=false;
        /* final String OUTPUT_ZIP_FILE = Environment.getExternalStorageDirectory() + "/Download"+"/AXJ.zip";
         final String SOURCE_FOLDER = filepath;

        AppZip appZip = new AppZip();
        appZip.generateFileList(new File(SOURCE_FOLDER),SOURCE_FOLDER);
        success=appZip.zipIt(OUTPUT_ZIP_FILE,SOURCE_FOLDER);

*/
        byte[] buffer = new byte[1024];
        String destination=filepath.substring(filepath.lastIndexOf("/")+1);
        String filepath1 = Environment.getExternalStorageDirectory() + "/Download/"+destination+".zip";


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
        getMenuInflater().inflate(R.menu.music_activity, menu);
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
            Intent i=new Intent(Music_activity.this,MainActivity.class);
            startActivity(i);
        }   else if (id == R.id.about) {
            Fragment fragment = new About();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.navig_layout,fragment,"About");
            fragmentTransaction.commit();
        } else if (id == R.id.rate_me) {
            dialog1=new Dialog(Music_activity.this);
            dialog1.setTitle("Rate Us:- ");
            dialog1.setContentView(R.layout.rate_me);
            dialog1.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                i=Music_activity.zip();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            Toast.makeText(Music_activity.this, "Music Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Zip formed with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during music saving", Toast.LENGTH_LONG).show();
            }
            compress_run(0);
        }

        @Override
        protected void onPreExecute() {
            dialog.cancel();
            i=false;


            pd.setMessage("Forming the Zip ");
            pd.show();

            pd.setCancelable(false);
        }

    }
    private class LongOperation2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            GeneralUtils.checkForPermissionsMAndAbove(Music_activity.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    /*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    */
                //String[] complex={"ffmpeg","-y","-i",filepath,Environment.getExternalStorageDirectory()+"/Download/out.m4a"};
                //String commandStr = "ffmpeg -y -i /sdcard/vk2/in.wav -ar 44100 -ac 2 -ab 64k -f mp3 /sdcard/videokit/out.mp3";
                String[] commandStr ={"ffmpeg","-y","-i",filepath,"-strict","experimental","-acodec","copy","-ss","00:00:00","-t","00:02:03.000",Environment.getExternalStorageDirectory() + "/Download/" +destination};
                String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-ar","44100","-ac","2","-ab","64k","-f","mp3", Environment.getExternalStorageDirectory() + "/Download/out.mp3"};
                vk.run(commandStr, workFolder, getApplicationContext());
                i=true;
                Log.i("test", "ffmpeg4android finished successfully");
            } catch (Throwable e) {
                Log.e("test", "vk run exception.", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            Toast.makeText(Music_activity.this, "Music Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Music formed with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Music saving", Toast.LENGTH_LONG).show();
            }
            compress_run(1);
        }

        @Override
        protected void onPreExecute() {
            dialog.cancel();
            i=false;


            pd.setMessage(".....Compressing ");
            pd.show();

            pd.setCancelable(false);
        }

    }


}
