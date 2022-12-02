package com.example.ottawamealer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class VoidCheckUploadPage extends AppCompatActivity {
    private StorageReference storageReference;
    Uri imageUri;
    Button voidCheckUploadBtn, voidCheckSubmitBtn , voidCheckBackBtn;
    ImageView voidCheckImage;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_void_check_upload_meal);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");




        //initialize Buttons
        voidCheckUploadBtn = (Button) findViewById(R.id.voidCheckUploadBtn);
        voidCheckBackBtn = (Button) findViewById(R.id.voidCheckBackBtn);
        voidCheckSubmitBtn = (Button) findViewById(R.id.voidCheckSubmitBtn);
        //initialize ImageViews
        voidCheckImage = (ImageView) findViewById(R.id.voidCheckImage);

        //upload button onClicker
        voidCheckUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);


            }
        });

        voidCheckSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference = FirebaseStorage.getInstance().getReference("Cook").child(email);
                storageReference.putFile(imageUri);



            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data!= null && data.getData()!=null){
            imageUri = data.getData();
            voidCheckImage.setImageURI(imageUri);
        }
    }
}