package br.com.stemcell.android.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GalleryCamera {


    private static final String TAG = "GalleryCamera";
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;

    private static final CharSequence[] items = { "Tirar foto", "Escolher da galeria", "Cancelar" };


    public static void selectImage(Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Selecionar imagem");
        DialogInterfaceImage dialog = new DialogInterfaceImage(context);
        builder.setItems(items, dialog);
        builder.show();
    }

    public static void selectImage(Fragment context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
        builder.setTitle("Selecionar imagem");
        DialogInterfaceImage dialog = new DialogInterfaceImage(context);
        builder.setItems(items, dialog);
        builder.show();
    }

    public static void onResult(Activity activity, ImageView target, int requestCode, int resultCode, Intent data) {

        if (resultCode == activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, e.getMessage(), e);
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                target.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = activity.getContentResolver().query(selectedImageUri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                Bitmap bm;
                bm = BitmapFactory.decodeFile(selectedImagePath);
                try {
                    ExifInterface exif = new ExifInterface(selectedImagePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                    }
                    else if (orientation == 3) {
                        matrix.postRotate(180);
                    }
                    else if (orientation == 8) {
                        matrix.postRotate(270);
                    }
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                target.setImageBitmap(bm);
            }
        }
    }

    private static class DialogInterfaceImage implements DialogInterface.OnClickListener {

        private Activity activity;
        private Fragment fragment;

        public DialogInterfaceImage(Activity activity) {
            this.activity = activity;
            this.fragment = null;
        }

        public DialogInterfaceImage(Fragment fragment) {
            this.fragment = fragment;
            this.activity = null;
        }

        @Override
        public void onClick(DialogInterface dialog, int item) {
            if (items[item].equals("Tirar foto")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (activity != null) {
                    activity.startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    fragment.startActivityForResult(intent, REQUEST_CAMERA);
                }


            } else if (items[item].equals("Escolher da galeria")) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");

                if (activity != null) {
                    activity.startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), SELECT_FILE);
                } else {
                    fragment.startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), SELECT_FILE);
                }

            } else if (items[item].equals("Cancelar")) {
                dialog.dismiss();
            }
        }

    }


}
