package com.example.qrtendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WebBrowser extends AppCompatActivity {
     ImageView refreshUrl,closeBrowser, more;
     WebView webView;
     ProgressBar progressBar;
     EditText urlIn;
    AlertDialog.Builder warning;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        refreshUrl = findViewById(R.id.btnRefreshWebView);
        closeBrowser =findViewById(R.id.btnCloseWebBroswer);
        webView = findViewById(R.id.web_View);
        progressBar = findViewById(R.id.progressBar);
        urlIn = findViewById(R.id.urlInput);
        more = findViewById(R.id.btnMore);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });


        //setting websettings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //setting textview
        urlIn.setEnabled(false);
      //
        //setting webview
        webView.setWebViewClient(new myNewWebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        //load this webpage
        Intent intent = getIntent();
        String url = intent.getStringExtra("mylink");
        loadMyUrl(url);


     closeBrowser.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //alert the user if they want to close the broswer or not
             warning  = new AlertDialog.Builder (WebBrowser.this);
             warning.setTitle("Warning!");
             warning.setMessage("Are you sure you want to close the browser, you will have to scan qr again.");
             warning.setCancelable(true);
             warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     Intent intent = new Intent(WebBrowser.this, MainActivity2.class);
                     startActivity(intent);
                 }
             });
             warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     dialogInterface.cancel();
                 }
             }).show();

         }
     });
  refreshUrl.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          //reload the page
          webView.reload();
      }
  });


        }
        //make a popup menu
        public void showPopup(View v){
            PopupMenu popupMenu = new PopupMenu(this,v );
            popupMenu.getMenuInflater().inflate(R.menu.browser_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    //when user clicks one of the buttons
                    switch (menuItem.getItemId()) {
                        case R.id.btnBack:
                            webView.goBack();
                            return true;
                        case R.id.btnForward:
                            webView.goForward();
                            return true;
                    }
                    return true;
                }
            });
            popupMenu.show();
        }

    @Override
    public void onBackPressed() {
        //if the user presses the back button, alert them
          if (webView.canGoBack()==true){
              webView.goBack();
          }
           else {
              warning  = new AlertDialog.Builder (WebBrowser.this);
              warning.setTitle("Warning!");
              warning.setMessage("Are you sure you want to close the browser, you will have to scan qr again.");
              warning.setCancelable(true);
              //alert user with choice to login or cancel
              warning.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      Intent intent = new Intent(WebBrowser.this, MainActivity2.class);
                      startActivity(intent);
                  }
              });
              warning.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.cancel();
                  }
              }).show();

          }



    }

    public void loadMyUrl (String url){
        boolean matchUrl = Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl){
            webView.loadUrl(url);
            urlIn.setText(url);
        } else {
            webView.loadUrl("google.com/search?q="+url);
        }


    }
    class myNewWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            urlIn.setText(url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.INVISIBLE);

        }

    }
}