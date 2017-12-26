package com.lordarian.lahelpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapHelper {

    public static Bitmap resize(Context context, String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "mipmap", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public static File saveBitmap(final Bitmap bitmap, Context context, String fileName, String dir, final OnFileSaveTaskFinish onFileSaveTaskFinish) {
        final File pictureFile = getOutputMediaFile(fileName, dir);
        if (pictureFile == null) {
            return null;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pictureFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    onFileSaveTaskFinish.onError();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                onFileSaveTaskFinish.onSuccess();
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return pictureFile;
    }

    private static File getOutputMediaFile(String fileName, String dir){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), dir);
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File mediaFile;
        String mImageName = fileName + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        String b64 = "";
        try {
            b64 = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
        }
        return b64;
    }
    public interface OnFileSaveTaskFinish{
        void onSuccess();
        void onError();
    }
}