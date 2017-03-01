package com.hackingbuzz.firebaseseries1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


// no user authenicated or..i can say its just a image uploading testing example no user ..saved in authenictation ..so inside rules tabs in storage autentication is null.


// Required Intent and Read External Storage Permission

public class MainActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    Button uploadFile;
    ProgressDialog dialog;
    public static int Request_Number = 1;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading Image. Please wait...");

        initObjects();
        initView();


        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    Log.i("Clicked","Yes");


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  // content:// (url) - External_Content_Uri ..location location of images in sd card and Phone Internal Storage (MediaStore -- sd card and phone internal storage)
                startActivityForResult(intent, 1);

            }
        });

    }

    private void initView() {

        uploadFile = (Button) findViewById(R.id.btn_upload);
    }

    private void initObjects() {

        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri uri =  data.getData();

// Creating a folder named Photos on firebase  n setting our image path to server then with putFile method we putting image (in our path uri) to server
           StorageReference filePath = mStorageRef.child("Photos").child(uri.getLastPathSegment());

            dialog.show(); // showing dialoge at the time of putting image


            // putFIle method work asynchoronously ..its gonna process n put image in background.. n attaching listeners to it..
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {  // you can add more than one interface to each other ...where you put semicolon to close means method1(listener).methodw2(listener).method3(listener);
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Image Uploaded Sucessfully",Toast.LENGTH_SHORT ).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Image Uploading Failed",Toast.LENGTH_SHORT ).show();

                }
            });


    }
}
}