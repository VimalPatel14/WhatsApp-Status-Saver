package com.vimal.whatsappstatussaver.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.models.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Common {

    private static final String CHANNEL_NAME = "VIMAL";
    public static final File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
            File.separator + "WhatsApp/Media/.Statuses");
    public static String APP_DIR;
    public static String INSTA_DIR;

    public static void copyFileNonotification(Status status, Context context) {

        File file = new File(Common.APP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        File destFile = new File(file + File.separator + status.getTitle());

        if (destFile.exists()) {
            destFile.delete();
        }

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {

            InputStream source = new FileInputStream(status.getFile());
            OutputStream destination = new FileOutputStream(destFile);

            byte[] buff = new byte[1024];
            int len;

            while ((len = source.read(buff)) > 0) {
                destination.write(buff, 0, len);
            }

            Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
            source.close();
            destination.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File copyFileInsta(String targetdir, Status status, Context context, ViewGroup container){

        File file = new File(targetdir);
        if (!file.exists()){
            file.mkdirs();
        }

        File destFile = new File(file+ File.separator + status.getTitle());

        if (destFile.exists()){
            destFile.delete();
        }

        if (!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        try {

            InputStream source = new FileInputStream(status.getFile());
            OutputStream destination = new FileOutputStream(destFile);
            byte[] buff = new byte[1024];
            int len;

            while ((len = source.read(buff)) > 0){
                destination.write(buff,0,len);
            }
            showNotificationInsta(targetdir, context, container, destFile, status);
            source.close();
            destination.close();

            return destFile;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void showNotificationInsta(String filePath, Context context, ViewGroup container, File destFile, Status status){

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT, context);
        }

//        Uri data = FileProvider.getUriForFile(context, "com.vimal.whatsappinsta"  + ".provider" ,new File(destFile.getAbsolutePath()));
        Uri data = FileProvider.getUriForFile(Objects.requireNonNull(context),
                "com.vimal.whatsappstatussaver" + ".provider", destFile);
        refreshAndroidGallery(context,data);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (status.isVideo()){
            intent.setDataAndType(data, "video/*");
        }else {
            intent.setDataAndType(data, "image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.download)
                .setContentTitle(destFile.getName())
                .setContentText("File Saved to"+ filePath)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

//        Snackbar.make(container,"Saved to "+filePath, Snackbar.LENGTH_LONG).show();

    }


    public static void refreshAndroidGallery(Context mContext, Uri fileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(fileUri);
            mContext.sendBroadcast(mediaScanIntent);
        } else {
            mContext.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public static void copyFile(Status status, Context context, RelativeLayout container) {

        File file = new File(Common.APP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }

        File destFile = new File(file + File.separator + status.getTitle());

        if (destFile.exists()) {
            destFile.delete();
        }

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        try {

            InputStream source = new FileInputStream(status.getFile());
            OutputStream destination = new FileOutputStream(destFile);

            byte[] buff = new byte[1024];
            int len;

            while ((len = source.read(buff)) > 0) {
                destination.write(buff, 0, len);
            }

            showNotification(context, container, destFile, status);
            source.close();
            destination.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void showNotification(Context context, RelativeLayout container, File destFile, Status status) {

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel(CHANNEL_NAME, "Saved", NotificationManager.IMPORTANCE_DEFAULT, context);
        }

        Uri data = Uri.fromFile(destFile);

//        Uri data = FileProvider.getUriForFile(context,  "com.vimal.whatsappinsta.provider", new File(destFile.getAbsolutePath()));
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (status.isVideo()) {
            intent.setDataAndType(data, "video/*");
        } else {
            intent.setDataAndType(data, "image/*");
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, CHANNEL_NAME);

        notification.setSmallIcon(R.drawable.download)
                .setContentTitle(destFile.getName())
                .setContentText("File Saved to" + APP_DIR)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notification.build());

        Snackbar.make(container, "Saved to " + Common.APP_DIR, Snackbar.LENGTH_LONG).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void makeNotificationChannel(String id, String name, int importance, Context context) {

        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

}
