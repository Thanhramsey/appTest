package com.vnpt.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.vnpt.common.Common;
import com.vnpt.staffhddtpos58.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: Calculate an inSampleSize
 * @author:truonglt2
 * @since:Feb 7, 2014 5:22:36 PM
 * @version: 1.0
 * @since: 1.0
 */
public class ImageHelperUtil {
    static String TAG = ImageHelperUtil.class.getName();
    /**
     * chup hinh
     *
     * @param activity
     * @param requestCode
     * @return
     * @author: AnhND
     * @return: File
     * @throws:
     */
    public static File takePhoto(BaseActivity activity, int requestCode) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File retFile = getOutputMediaFileCamera();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(retFile));
        Log.d("truognlt2",""+retFile.getPath());
        StoreSharePreferences.getInstance(activity).saveStringPreferences(Common.KEY_PATH_IMAGE_SING_CAMERA,retFile.getAbsolutePath());
        activity.startActivityForResult(intent, requestCode);
        return retFile;
    }
    private String storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFileCamera();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return "";
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
            return "";
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
            return "";
        }
        return pictureFile.getAbsolutePath();
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFileCamera(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/"
                + Common.PACKAGE_NAME
                + "/SignCameraHDDT");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="CI_"+ timeStamp +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that will result in the final decoded bitmap having a width and
     * height equal to or larger than the requested width and height. This implementation does not
     * ensure a power of 2 is returned for inSampleSize which can be faster when decoding but
     * results in a larger bitmap which isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     * @author google
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

//    private Bitmap getBitmap(String path) {
//
//        Uri uri = Uri.fromFile(new File(path));
//        InputStream in = null;
//        try {
//            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
//            in = getContentResolver().openInputStream(uri);
//
//            // Decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(in, null, o);
//            in.close();
//
//
//            int scale = 1;
//            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
//                    IMAGE_MAX_SIZE) {
//                scale++;
//            }
//            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);
//
//            Bitmap b = null;
//            in = getContentResolver().openInputStream(uri);
//            if (scale > 1) {
//                scale--;
//                // scale to max possible inSampleSize that still yields an image
//                // larger than target
//                o = new BitmapFactory.Options();
//                o.inSampleSize = scale;
//                b = BitmapFactory.decodeStream(in, null, o);
//
//                // resize to desired dimensions
//                int height = b.getHeight();
//                int width = b.getWidth();
//                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);
//
//                double y = Math.sqrt(IMAGE_MAX_SIZE
//                        / (((double) width) / height));
//                double x = (y / height) * width;
//
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
//                        (int) y, true);
//                b.recycle();
//                b = scaledBitmap;
//
//                System.gc();
//            } else {
//                b = BitmapFactory.decodeStream(in);
//            }
//            in.close();
//
//            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
//                    b.getHeight());
//            return b;
//        } catch (IOException e) {
//            Log.e("", e.getMessage(), e);
//            return null;
//        }
//    }
//    public saveScaledPhotoToFile() {
//        //Convert your photo to a bitmap
//        Bitmap photoBm = (Bitmap) "your Bitmap image";
//        //get its orginal dimensions
//        int bmOriginalWidth = photoBm.getWidth();
//        int bmOriginalHeight = photoBm.getHeight();
//        double originalWidthToHeightRatio =  1.0 * bmOriginalWidth / bmOriginalHeight;
//        double originalHeightToWidthRatio =  1.0 * bmOriginalHeight / bmOriginalWidth;
//        //choose a maximum height
//        int maxHeight = 1024;
//        //choose a max width
//        int maxWidth = 1024;
//        //call the method to get the scaled bitmap
//        photoBm = getScaledBitmap(photoBm, bmOriginalWidth, bmOriginalHeight,
//                originalWidthToHeightRatio, originalHeightToWidthRatio,
//                maxHeight, maxWidth);
//
//        /**********THE REST OF THIS IS FROM Prabu's answer*******/
//        //create a byte array output stream to hold the photo's bytes
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        //compress the photo's bytes into the byte array output stream
//        photoBm.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//
//        //construct a File object to save the scaled file to
//        File f = new File(Environment.getExternalStorageDirectory()
//                + File.separator + "Imagename.jpg");
//        //create the file
//        f.createNewFile();
//
//        //create an FileOutputStream on the created file
//        FileOutputStream fo = new FileOutputStream(f);
//        //write the photo's bytes to the file
//        fo.write(bytes.toByteArray());
//
//        //finish by closing the FileOutputStream
//        fo.close();
//    }
//
//    private static Bitmap getScaledBitmap(Bitmap bm, int bmOriginalWidth, int bmOriginalHeight, double originalWidthToHeightRatio, double originalHeightToWidthRatio, int maxHeight, int maxWidth) {
//        if(bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
//            Log.v(TAG, format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight));
//
//            if(bmOriginalWidth > bmOriginalHeight) {
//                bm = scaleDeminsFromWidth(bm, maxWidth, bmOriginalHeight, originalHeightToWidthRatio);
//            } else if (bmOriginalHeight > bmOriginalWidth){
//                bm = scaleDeminsFromHeight(bm, maxHeight, bmOriginalHeight, originalWidthToHeightRatio);
//            }
//
//            Log.v(TAG, format("RESIZED bitmap TO %sx%s ", bm.getWidth(), bm.getHeight()));
//        }
//        return bm;
//    }
//
//    private static Bitmap scaleDeminsFromHeight(Bitmap bm, int maxHeight, int bmOriginalHeight, double originalWidthToHeightRatio) {
//        int newHeight = (int) Math.max(maxHeight, bmOriginalHeight * .55);
//        int newWidth = (int) (newHeight * originalWidthToHeightRatio);
//        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
//        return bm;
//    }
//
//    private static Bitmap scaleDeminsFromWidth(Bitmap bm, int maxWidth, int bmOriginalWidth, double originalHeightToWidthRatio) {
//        //scale the width
//        int newWidth = (int) Math.max(maxWidth, bmOriginalWidth * .75);
//        int newHeight = (int) (newWidth * originalHeightToWidthRatio);
//        bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
//        return bm;
//    }

}
