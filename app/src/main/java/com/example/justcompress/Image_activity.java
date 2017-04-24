package com.example.justcompress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Image_activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static Uri imageUri;
    public static TextView textView3,textView11,textView12,textView13,textView14;
    private int compressimage=1;
    public static ImageView imageView2,imageView;
    public static String filepath,destination;
    public static Bitmap bitmap1;
    public static Bitmap bitmap2;
    public static ByteArrayOutputStream bytearrayoutputstream;
    public static byte[] BYTE;
    int PICK_IMAGE_REQUEST=1;
    public static Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent=getIntent();
        imageView=(ImageView) findViewById(R.id.imageView3);
        imageView2=(ImageView) findViewById(R.id.imageView2);
        Log.e("Image_activity","sakdsa");
        /*final String filepath=*/
        textView11=(TextView)findViewById(R.id.textView11);
        textView12=(TextView)findViewById(R.id.textView12);
        textView13=(TextView)findViewById(R.id.textView13);
        textView14=(TextView)findViewById(R.id.textView14);
        filepath =this.getIntent().getStringExtra("image");
        bitmap1=BitmapFactory.decodeFile(filepath);
        int w=bitmap1.getWidth();
        int h=bitmap1.getHeight();
        textView11.setText("Height :"+h);
        textView12.setText("Width :"+w);
        imageView.setImageBitmap(bitmap1);
        Log.e("filepath is: ",filepath);
        bytearrayoutputstream = new ByteArrayOutputStream();
        BYTE=bytearrayoutputstream.toByteArray();
        File file=new File(filepath);
        long length=file.length();
        length=length/1024;
        TextView textView2=(TextView)findViewById(R.id.textView2);
        textView2.setText(length+"KB");
        imageUri=getIntent().getData();
        imageView.setImageURI(imageUri);
        final ProgressDialog p=new ProgressDialog(this);
        Button button=(Button)findViewById(R.id.button3);
        textView3=(TextView)findViewById(R.id.textView3);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //startActivityForResult(null,compressimage);
                final Dialog dialog=new Dialog(Image_activity.this);
                dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_choose_save_type);
                dialog.show();

               /* Intent i=new Intent(Image_activity.this,Choose_save_type.class);
              *//*  i.putExtra("original",filepath);
                i.setData(imageUri);
              *//*  //startActivity(i);
*/
                Button button2=(Button)dialog.findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean i=false;

                        try {
                            imageView2.setImageDrawable(null);
                            p.setMessage("Please wait: ");
                            p.show();
                            i=Image_activity.zip();
                            p.dismiss();
                            dialog.cancel();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(Image_activity.this, "Image Compressed Successfully", Toast.LENGTH_LONG).show();
                        if (i) {
                            Toast.makeText(getApplicationContext(), "Zip saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error during image saving", Toast.LENGTH_LONG).show();
                        }
                    }

                });

                Button button4=(Button)dialog.findViewById(R.id.button4);
                button4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean i=false;
                        imageView2.setImageDrawable(null);
                        p.setMessage("Please wait: ");
                        p.show();

                        i=Image_activity.compress(80);
                        p.dismiss();
                        dialog.cancel();
                        Toast.makeText(Image_activity.this, "Image Compressed Successfully", Toast.LENGTH_LONG).show();
                        if (i) {
                            Toast.makeText(getApplicationContext(),"Image saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error during image saving", Toast.LENGTH_LONG).show();
                        }
                    }

                });
                Button button5=(Button)dialog.findViewById(R.id.button5);
                button5.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean i=false;
                        imageView2.setImageDrawable(null);
                        p.setMessage("Please wait: ");
                        p.show();

                        i=Image_activity.compress(50);
                        p.dismiss();
                        dialog.cancel();
                        Toast.makeText(Image_activity.this, "Image Compressed Successfully", Toast.LENGTH_LONG).show();
                        if (i) {
                            Toast.makeText(getApplicationContext(),"Image saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error during image saving", Toast.LENGTH_LONG).show();
                        }
                    }

                });
                Button button6=(Button)dialog.findViewById(R.id.button6);
                button6.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean i=false;
                        imageView2.setImageDrawable(null);
                        p.setMessage("Please wait: ");
                        p.show();

                        i=Image_activity.compress(20);
                        p.dismiss();
                        dialog.cancel();
                        Toast.makeText(Image_activity.this, "Image Compressed Successfully", Toast.LENGTH_LONG).show();
                        if (i) {
                            Toast.makeText(getApplicationContext(), "Image saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error during image saving", Toast.LENGTH_LONG).show();
                        }
                    }

                });
                Button button7=(Button)dialog.findViewById(R.id.button7);
                button7.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean i=false;
                        imageView2.setImageDrawable(null);
                        p.setMessage("Please wait: ");
                        p.show();

                        i=Image_activity.compress(10);
                        p.dismiss();
                        dialog.cancel();
                        Toast.makeText(Image_activity.this, "Image Compressed Successfully ", Toast.LENGTH_LONG).show();
                        if (i) {
                            Toast.makeText(getApplicationContext(), "Image saved with success at: "+Environment.getExternalStorageDirectory()+"/Download/"+destination,Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error during image saving", Toast.LENGTH_LONG).show();
                        }


                    }

                });

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(Image_activity.this);
                //dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_image1);
                dialog.show();
                ImageView imageView4=(ImageView)dialog.findViewById(R.id.imageView4);
                Bitmap b=BitmapFactory.decodeFile(filepath);
                imageView4.setImageBitmap(b);

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(Image_activity.this);
                //dialog.setTitle("Choose compress type:- ");
                dialog.setContentView(R.layout.activity_image1);
                dialog.show();
                ImageView imageView4=(ImageView)dialog.findViewById(R.id.imageView4);
                Bitmap b=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/Download/"+destination);
                imageView4.setImageBitmap(b);

            }
        });

    }

    static boolean compress(int i) {
        bitmap1.compress(Bitmap.CompressFormat.JPEG,i,bytearrayoutputstream);

        BYTE = bytearrayoutputstream.toByteArray();
        bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);
        int w=bitmap2.getWidth();
        int h=bitmap2.getHeight();
        textView13.setText("Height :"+h);
        textView14.setText("Width :"+w);
       imageView2.setImageBitmap(bitmap2);

        BitmapDrawable drawable = (BitmapDrawable) imageView2.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        destination=filepath.substring(filepath.lastIndexOf("/")+1);
        Log.e("filename is:  ",destination);
        File sdCardDirectory = new File(Environment.getExternalStorageDirectory(),"Download");
        File image = new File(sdCardDirectory, destination);
        boolean success = false;

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {

            outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG,i, outStream);
        /* 100 to keep full quality of the image */
            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filepath1 = Environment.getExternalStorageDirectory() + "/Download/" + destination;
        File file1=new File(filepath1);
        long length1=file1.length();
        Log.e("filepath is: ",filepath);
        Log.e("filepath1 is: ",filepath1);
        length1=length1/1024;
        textView3.setText(length1+"KB");
        return success;
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
        String filepath1 = Environment.getExternalStorageDirectory() + "/Download"+"/.zip";
        FileOutputStream fos = new FileOutputStream(filepath1);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry ze= new ZipEntry("test.jpg");
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
        File file1=new File(filepath1);
        long length1=file1.length();
        Log.e("filepath is: ",filepath);
        Log.e("filepath1 is: ",filepath1);
        length1=length1/1024;
        textView3.setText(length1+"KB");
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
        getMenuInflater().inflate(R.menu.image_activity, menu);
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
            // Handle the camera action
            Intent i=new Intent(Image_activity.this,MainActivity.class);
            startActivity(i);
        }  else if (id == R.id.about) {
            Fragment fragment = new About();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.navig_layout,fragment,"About");
            fragmentTransaction.commitAllowingStateLoss();

        } else if (id == R.id.rate_me) {
            dialog1=new Dialog(Image_activity.this);
            dialog1.setTitle("Rate Us:- ");
            dialog1.setContentView(R.layout.rate_me);
            dialog1.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
