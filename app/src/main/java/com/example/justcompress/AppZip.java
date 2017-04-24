package com.example.justcompress;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AppZip
{
    List<String> fileList;
    public static final String OUTPUT_ZIP_FILE = Environment.getExternalStorageDirectory() + "/Download"+"/AXJ.zip";
    //public static final String SOURCE_FOLDER = filepath;

    AppZip(){
        fileList = new ArrayList<String>();
    }

    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public boolean zipIt(String zipFile,String SOURCE_FOLDER){
        boolean a=false;
        byte[] buffer = new byte[1024];

        try{

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);

            for(String file : this.fileList){

                System.out.println("File Added : " + file);
                ZipEntry ze= new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                a=true;
                }

                in.close();
            }

            zos.closeEntry();
            //remember close it
            zos.close();

            System.out.println("Done");
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return a;
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    public void generateFileList(File node,String SOURCE_FOLDER){

        //add file only
        if(node.isFile()){
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString(),SOURCE_FOLDER));
        }

        if(node.isDirectory()){
            String[] subNote = node.list();
            for(String filename : subNote){
                generateFileList(new File(node, filename),SOURCE_FOLDER);
            }
        }

    }


    private String generateZipEntry(String file,String SOURCE_FOLDER){
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}