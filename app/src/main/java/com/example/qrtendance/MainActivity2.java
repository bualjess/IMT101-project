package com.example.qrtendance;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.net.URL;

public class MainActivity2 extends AppCompatActivity {
    Button ScanQr,SignOut;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView name,email;
    ImageView profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //textview objects
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        //button objects
        SignOut = findViewById(R.id.btnSignOut);
        ScanQr = findViewById(R.id.btnScanQR);

        //imageview objects
        profilePic = findViewById(R.id.picture);

        //setting up google login here too
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String imageUrl = String.valueOf(acct.getPhotoUrl());
            name.setText(personName);
            email.setText(personEmail);
            Glide.with(this).load(imageUrl).into(profilePic);
        }



        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        ScanQr.setOnClickListener(v->{
            scanCameraCode();
    });
    }
    void signOut (){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( Task<Void> task) {
                finish();
                startActivity(new Intent(MainActivity2.this, MainActivity.class));
            }
        });
    }
    //options for Qr scanner
    private void scanCameraCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    //read  qr code content
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        //checking for an OK string
        if (result.getContents().contains("OK")) {
            //Success, it contained the string "OK"
            //do something when qr code is scanned
            //login but make sure @hnu.edu.ph email will be allowed
            //if successful, make sure student registered in class will be allowed to open form
            AlertDialog.Builder builder = new AlertDialog.Builder (this);
            builder.setTitle("Result");
            builder.setMessage(result.getContents());
            builder.setCancelable(true);
            //alert user with choice to login or cancel
            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //open a link
                    openLink("www.youtube.com");
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        }
        else {
            //Scanned QR did not contain the OK
            AlertDialog.Builder builder = new AlertDialog.Builder (this);
            builder.setTitle("ERROR!");
            builder.setMessage("Invalid QR Code");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();


        }
    });
    void openLink(String link){
        Intent intent = new Intent(this,WebBrowser.class);
        intent.putExtra("mylink",link);
        startActivity(intent);
    }


}