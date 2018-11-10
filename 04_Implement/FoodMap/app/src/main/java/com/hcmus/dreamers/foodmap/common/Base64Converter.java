package com.hcmus.dreamers.foodmap.common;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

// Source: https://stackoverflow.com/questions/4830711/how-to-convert-a-image-into-base64-string
public class Base64Converter {
    public static String binary2Base64(Context context, String fileName)throws Exception{
        Uri uri = Uri.parse(fileName);
        InputStream  inputStream  = context.getContentResolver().openInputStream(uri);

//        File sdcard = Environment.getExternalStorageDirectory();
//        InputStream  inputStream = new FileInputStream(new File(sdcard, fileName));//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return  encodedString;
    }

    public static String encodeToBase64(Context context, Uri imageUri)throws FileNotFoundException {

        InputStream inputStream;
        inputStream = context.getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedImage;
    }
}
