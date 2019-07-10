package com.integra.digitalisasilayanansppirt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    private WebView view;
    private String url = "https://integra.web.id/eirtp-diy/";

    private SwipeRefreshLayout mySwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = (WebView) this.findViewById(R.id.webView);
        mySwipeRefreshLayout = findViewById(R.id.swipe);


        settings();
        load_website();

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_website();

            }
        });



    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && view.canGoBack()) {
            view.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void settings(){
        WebSettings web_settings = view.getSettings();
        web_settings.setJavaScriptEnabled(true);
        web_settings.setAllowContentAccess(true);
        web_settings.setLoadsImagesAutomatically(true);
        web_settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        web_settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        web_settings.setEnableSmoothTransition(true);
        web_settings.setDomStorageEnabled(true);
        web_settings.setLoadWithOverviewMode(true);
        web_settings.setUseWideViewPort(true);
    }

    private void load_website(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if(Build.VERSION.SDK_INT >= 19){
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else{
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //Tambahkan WebChromeClient
        view.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //ProgressBar akan Terlihat saat Halaman Dimuat
                progressDialog.show();
                mySwipeRefreshLayout.setRefreshing(false);
                super.onProgressChanged(view, newProgress);
            }
        });

        view.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //ProgressBar akan Menghilang setelah Halaman Selesai Dimuat
                super.onPageFinished(view, url);
                view.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
                mySwipeRefreshLayout.setRefreshing(false);

            }
        });

        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        view.setScrollbarFadingEnabled(true);
        view.loadUrl(url);
    }

}
