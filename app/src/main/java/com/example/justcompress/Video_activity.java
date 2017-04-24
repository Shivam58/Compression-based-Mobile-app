package com.example.justcompress;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.netcompss.ffmpeg4android.CommandValidationException;
import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Video_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,About.OnFragmentInteractionListener{
    static VideoView vv;
    static VideoView videoView2;
public static boolean i;
    static String filepath,videopath;
    public static TextView textView1,textView2,textView7,textView8,textView9,textView10;
   private GoogleApiClient client;
    private GoogleApiClient client2;
    private GoogleApiClient client3;
    public static String destination;
    public static ProgressDialog pd;
    public static Dialog dialog,dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_activity);
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
        vv = (VideoView) findViewById(R.id.videoView);
        filepath = this.getIntent().getStringExtra("video");
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);
        textView7=(TextView)findViewById(R.id.textView7);
        textView8=(TextView)findViewById(R.id.textView8);
        textView9=(TextView)findViewById(R.id.textView9);
        textView10=(TextView)findViewById(R.id.textView10);
        /*Bitmap bitmap=(Bitmap)this.getIntent().getParcelableExtra("video");*/
        vv.setVideoURI(Uri.parse(getIntent().getStringExtra("video")));
        MediaController mc = new MediaController(this);
        mc.setAnchorView(vv);
        vv.start();
        vv.setMediaController(mc);
        videoView2 = (VideoView) findViewById(R.id.videoView2);
        MediaController mc1 = new MediaController(this);
        mc1.setAnchorView(videoView2);

        videoView2.setMediaController(mc1);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Bitmap bmp = null;
        File file=new File(filepath);
        Long length=file.length();
        retriever.setDataSource(filepath);
        bmp=retriever.getFrameAtTime();
        int width=bmp.getWidth();
        int height=bmp.getHeight();
        length=length/1024;
        textView1.setText(" "+length+" KB");
        textView7.setText("Width : "+width);
        textView8.setText("Height : "+height);
        destination = filepath.substring(filepath.lastIndexOf("/") + 1);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(Video_activity.this);
                dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_save_video);
                dialog.show();

                Button button2=(Button)dialog.findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        new LongOperation().execute("");
                    }

                });

                Button button4=(Button)dialog.findViewById(R.id.button4);
                button4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new LongOperation1().execute("");
                    }

                });

                Button button5=(Button)dialog.findViewById(R.id.button5);
                button5.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new LongOperation2().execute("");
                    }

                });
                Button button6=(Button)dialog.findViewById(R.id.button6);
                button6.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new LongOperation3().execute("");
                    }

                });
                Button button7=(Button)dialog.findViewById(R.id.button7);
                button7.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        new LongOperation4().execute("");
                    }

                });


            }

        });

        /*vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(Video_activity.this);
                //dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_video);
                dialog.show();
                VideoView videoView=(VideoView)dialog.findViewById(R.id.videoView3);
                vv.setVideoURI(Uri.parse(filepath));
                vv.start();

            }
        });
        videoView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(Video_activity.this);
                //dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_video);
                dialog.show();
                VideoView videoView=(VideoView)dialog.findViewById(R.id.videoView3);
                vv.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory()+"/Download/"+destination));
                vv.start();

            }
        });
*/
    }

    public void compress_run(int i)
    {      // vv.stopPlayback();
            if(i==1)
        videopath = Environment.getExternalStorageDirectory() + "/Download/" + destination;
        else
                videopath = Environment.getExternalStorageDirectory() + "/Download/" + destination + ".zip";

        File file=new File(videopath);
        Long length=file.length();
        length=length/1024;
        textView2.setText(" "+length+" KB");
        if(i==1)
            run();

    }
    public void run()
    {
        Uri video = Uri.parse(Environment.getExternalStorageDirectory() + "/Download/" + destination);
        videoView2.setVideoURI(video);
        videoView2.setVideoPath(videopath);
        videoView2.requestFocus();
        videoView2.start();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        Bitmap bmp = null;
        retriever.setDataSource(videopath);
        bmp=retriever.getFrameAtTime();
        int width=bmp.getWidth();
        int height=bmp.getHeight();
        textView9.setText("Width : "+width);
        textView10.setText("Height : "+height);

    }


    static boolean zip() throws IOException {
                boolean success = false;
                byte[] buffer = new byte[5000000];
                String filepath1 = Environment.getExternalStorageDirectory() + "/Download/" + destination + ".zip";
                Log.e("Filepath is: ", filepath);
                Log.e("Filepath1 is: ", filepath1);
                Log.e("Destination is: ", destination);
                FileOutputStream fos = new FileOutputStream(filepath1);
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry(destination);
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(filepath);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                    Log.e("y1", "adad");
                    success = true;
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
                getMenuInflater().inflate(R.menu.video_activity, menu);
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
                    Intent i = new Intent(Video_activity.this, MainActivity.class);
                    startActivity(i);
                } else if (id == R.id.about) {
                    Fragment fragment = new About();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.navig_layout, fragment, "About");
                    fragmentTransaction.commitAllowingStateLoss();
                } else if (id == R.id.rate_me) {
                    dialog1=new Dialog(Video_activity.this);
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
                i=Video_activity.zip();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();

            Toast.makeText(Video_activity.this, "Video Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Zip formed with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Video saving", Toast.LENGTH_LONG).show();
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

    private class LongOperation1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Video_activity.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    /*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    */
                String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-strict", "experimental", "-s", "1024x576", "-r", "25", "-vcodec", "mpeg4", "-b", "2176k", "-ab", "48000", "-ac", "2", "-ar", "22050", Environment.getExternalStorageDirectory() + "/Download/"+destination};
                vk.run(complexCommand, workFolder, getApplicationContext());
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

            Toast.makeText(Video_activity.this, "Video Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Video Saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Video saving", Toast.LENGTH_LONG).show();
            }
            compress_run(1);
        }

        @Override
        protected void onPreExecute() {
            i=false;
            pd.setMessage(".....Compressing ");
            pd.show();

            pd.setCancelable(false);
            dialog.cancel();

        }

    }
    private class LongOperation2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Video_activity.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    /*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    */
                String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-strict", "experimental", "-s", "828x480", "-r", "25", "-vcodec", "mpeg4", "-b", "1536k", "-ab", "48000", "-ac", "2", "-ar", "22050",  Environment.getExternalStorageDirectory() + "/Download/"+destination};
                vk.run(complexCommand, workFolder, getApplicationContext());
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

            Toast.makeText(Video_activity.this, "Video Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Video Saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Video saving", Toast.LENGTH_LONG).show();
            }
            compress_run(1);
        }

        @Override
        protected void onPreExecute() {
            i=false;
            pd.setMessage(".....Compressing ");
            pd.show();

            pd.setCancelable(false);
            dialog.cancel();

        }

    }
    private class LongOperation3 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Video_activity.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    /*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    */
                String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-strict", "experimental", "-s", "640x360", "-r", "25", "-vcodec", "mpeg4", "-b", "896k", "-ab", "48000", "-ac", "2", "-ar", "22050", Environment.getExternalStorageDirectory() + "/Download/"+destination};
                vk.run(complexCommand, workFolder, getApplicationContext());
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

            Toast.makeText(Video_activity.this, "Video Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Video Saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Video saving", Toast.LENGTH_LONG).show();
            }
            compress_run(1);
        }

        @Override
        protected void onPreExecute() {
            i=false;
            pd.setMessage(".....Compressing ");
            pd.show();

            pd.setCancelable(false);
            dialog.cancel();

        }

    }
    private class LongOperation4 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            GeneralUtils.checkForPermissionsMAndAbove(Video_activity.this, true);
            LoadJNI vk = new LoadJNI();
            try {
                String workFolder = getApplicationContext().getFilesDir().getAbsolutePath();
                    /*String[] complexCommand = {"ffmpeg","-i", "/sdcard/videokit/in.mp4"};
                    */
                String[] complexCommand = {"ffmpeg", "-y", "-i", filepath, "-strict", "experimental", "-s", "424x240", "-r", "25", "-vcodec", "mpeg4", "-b", "576k", "-ab", "48000", "-ac", "2", "-ar", "22050", Environment.getExternalStorageDirectory() + "/Download/"+destination};
                vk.run(complexCommand, workFolder, getApplicationContext());
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

            Toast.makeText(Video_activity.this, "Video Compressed Successfully", Toast.LENGTH_LONG).show();
            if (i) {
                Toast.makeText(getApplicationContext(),  "Video Saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error during Video saving", Toast.LENGTH_LONG).show();
            }
            compress_run(1);
        }

        @Override
        protected void onPreExecute() {
            i=false;
            pd.setMessage(".....Compressing ");
            pd.show();
            pd.setCancelable(false);
            dialog.cancel();

        }

    }


}
