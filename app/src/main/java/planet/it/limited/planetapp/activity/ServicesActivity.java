package planet.it.limited.planetapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import planet.it.limited.planetapp.R;
import planet.it.limited.planetapp.utill.Constant;

public class ServicesActivity extends AppCompatActivity {
    //Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    String servicesLink = " ";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
//        toolbar = (Toolbar)findViewById(R.id.toolbar_services);
//        setSupportActionBar(toolbar);
//
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                 onBackPressed();
//
//            }
//        });

        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){

        webView = (WebView) findViewById(R.id.webview);
//        txvHeadlineToolbar = (TextView)findViewById(R.id.txv_headline_sms);
//        fontCustomization = new FontCustomization(SMSDashboardActivity.this);

        alertDialog = new AlertDialog.Builder(this).create();
        servicesLink = Constant.serviesAPI;



       // txvHeadlineToolbar.setTypeface(fontCustomization.getTexgyreHerosBold());


        progressBar = ProgressDialog.show(ServicesActivity.this,
              "please wait", "loading.......");


        loadWebView();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //  webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.isShowing();

            }
            public void onPageFinished(WebView view, String url) {


                if (progressBar.isShowing()) {
                    progressBar.dismiss();

                   // Log.d(TAG_PRO, "progressbar ------>"+"finish");

                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });
        webView.loadUrl(servicesLink);
    }
}
