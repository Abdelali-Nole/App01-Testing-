package com.example.appproject01;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class clsImageUtils {



    // bitmap : is real pixels of data of image
    // Based64 : is Decoded chars real data of pixels of image
    // Uri : is not real data it is just String path of image path to file
    // it can convert between them easily


    public static String saveImageUriAsString(Uri imageUri,Context cont) {
        try {
            // Convert Uri to Bitmap
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(cont.getContentResolver(), imageUri);

            // Convert Bitmap to Base64 string
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] byteArray = outputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            return base64Image;


        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
        
        
    }


    public static Bitmap ConvertUriImageToBitmap(Uri uriImagePath, Context cont) {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            // Open the InputStream from the Uri
            inputStream = cont.getContentResolver().openInputStream(uriImagePath);

            // Decode the InputStream into a Bitmap
            if (inputStream != null) {
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the InputStream to prevent resource leaks
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }


    public static Bitmap convertBase64ToBitmap(String base64String) {
        // Decode the Base64 string into a byte array
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

        // Convert the byte array into a Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Uri convertBase64ToUriAndSaveImageBitmapAtFile(Context context, String base64String) {
        // Step 1: Decode the Base64 string to a byte array
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

        // Step 2: Convert byte array to a Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

        // Step 3: Save the Bitmap to a file
        File file = new File(context.getCacheDir(), "temp_image.png"); // or .jpg based on your preference
        Uri uri = null;

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress format and quality
            out.close();

            // Step 4: Get the Uri using FileProvider
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return uri;
    }
}
