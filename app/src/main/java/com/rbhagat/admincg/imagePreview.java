package com.rbhagat.admincg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

public class imagePreview extends AppCompatActivity {

    ImageView back_arrow,wasteImage,sharedBtn,downloadBtn;
    TextView description;
    String wImage;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);

        back_arrow=findViewById(R.id.backArrowp);
        wasteImage=findViewById(R.id.imagePreview);
        description=findViewById(R.id.desc);
        sharedBtn=findViewById(R.id.shareImg);
        downloadBtn=findViewById(R.id.downloadImg);


         String desc=getIntent().getStringExtra("descrip");
         wImage=getIntent().getStringExtra("w_image");


        Picasso.get().load(wImage).placeholder(R.drawable.w2_img).into(wasteImage);
        description.setText(desc);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(imagePreview.this,raisedRequestActivity.class);
                startActivity(intent);
            }
        });

        sharedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImg();
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                downloadImage();
            }
        });






    }

    private void downloadImage() {
        Picasso.get().load(wImage).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Save the bitmap to the user's gallery
                try {
                    String fileName = "Image_" + System.currentTimeMillis() + ".jpg";
                    OutputStream fos = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        // Use MediaStore API for Android Q and above
                        ContentResolver resolver = getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                    } else {
                        // Use traditional file output stream
                        File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        File image = new File(imagesDir, fileName);
                        fos = new FileOutputStream(image);
                    }

                    if (fos != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();

                        progressDialog.dismiss();
                        // Notify user and refresh gallery
                        Toast.makeText(imagePreview.this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fileName))));
                    } else {
                        Toast.makeText(imagePreview.this, "Error saving image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(imagePreview.this, "Error saving image", Toast.LENGTH_SHORT).show();
                }

                // Dismiss the progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Toast.makeText(imagePreview.this, "Error loading image", Toast.LENGTH_SHORT).show();

                // Dismiss the progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Not needed in this case
            }
        });
    }


    private void shareImg() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareMessage =wImage;
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        // Start the sharing intent
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}