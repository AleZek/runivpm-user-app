package com.ids.idsuserapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by zek on 16/04/18.
 */

public class FileHelper {
    Context context;
    public FileHelper(Context context) {
        this.context = context;
    }

    public byte[] readFromFile(Uri uri) {
        InputStream iStream = null;
        try {
            iStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            Toast toast = Toast.makeText(context, "Errore durante la lettura del file!", Toast.LENGTH_SHORT);
            toast.show();
        }
        byte[] inputData = getBytes(iStream);

        return inputData;
    }

    private byte[] getBytes(InputStream iStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        try {
            while ((len = iStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();
    }

    public void saveFile(String filename, String dir, byte[] file_bytes){
        String root = Environment.getExternalStorageDirectory().toString();
        File absDir = new File(root + "/" + dir);
        absDir.mkdirs();
        if(absDir.isDirectory()) {
            String fname = filename;
            File file = new File(absDir, fname);
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(file_bytes);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String base64Encode(byte[] data){
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public byte[] base64Decode(String data){
        return Base64.decode(data, Base64.DEFAULT);
    }

    public Bitmap getBitmapFromFile(String subdir, String filename){
        File storageDir = Environment.getExternalStorageDirectory();
        File imageFile = new File(storageDir + subdir + filename);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(),bmOptions);

    }

    public boolean fileExists(String path){
        File file = new File(path);
        return file.exists();
    }

    public boolean deleteFile(String path){
        File file = new File(path);
        return file.delete();
    }

}
