package com.example.qrtendance;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
         Button btn_scanqr;
         GoogleSignInOptions gso;
         GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting up google sign in
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        btn_scanqr = findViewById(R.id.btn_scan_qr);
        btn_scanqr.setOnClickListener(v->{
            scanCameraCode();
        });
    }
    //qr code scanner options
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
            builder.setPositiveButton("login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do the login here
                    //signIn();

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
      void signIn (){
          Intent signInIntent =  gsc.getSignInIntent();
          startActivityForResult(signInIntent,1000);
      }
    public void move_to_generate_qr(View view){
       Intent intent = new Intent(this,GenerateQR.class);
       startActivity(intent);
    }
    public void moveToWebBrowser(View view){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                /* i'm supposed to open a browser here */
                //moveTOScanQr();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }
    }
}