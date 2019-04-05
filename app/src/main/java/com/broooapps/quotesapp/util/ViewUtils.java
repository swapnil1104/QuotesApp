package com.broooapps.quotesapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Swapnil Tiwari on 05/04/19.
 * swapnil.tiwari@box8.in
 */
public class ViewUtils {

    public static void setVerticalBias(final View view) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.verticalBias = (float) Util.getRandomDoubleBetweenRange(0.3f, 0.7f);
        view.setLayoutParams(params);
    }

    public static File takeScreenshot(View activityView) {

        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            activityView.setSystemUiVisibility(uiOptions);
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            activityView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(activityView.getDrawingCache());
            activityView.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            outputStream.flush();
            outputStream.close();

            return (imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void shareScreenshot(Context context, File image) {
        String pack = "com.whatsapp";

        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());

        PackageManager pm = context.getPackageManager();
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            Uri imageUri = Uri.parse(path);

            @SuppressWarnings("unused")
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_META_DATA);

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("image/*");
            waIntent.setPackage(pack);
            waIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);
            waIntent.putExtra(Intent.EXTRA_TEXT, "Read more amazing quotes on 'Motivational Quotes Daily' " +
                    "https://play.google.com/store/apps/details?id=com.broooapps.quotesapp");
            context.startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (Exception e) {
            Log.e("Error on sharing", e + " ");
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
        }
    }


}
