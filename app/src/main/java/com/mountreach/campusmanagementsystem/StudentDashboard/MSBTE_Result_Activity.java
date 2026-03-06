package com.mountreach.campusmanagementsystem.StudentDashboard;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mountreach.campusmanagementsystem.R;

public class MSBTE_Result_Activity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msbte_result);

        webView = findViewById(R.id.webViewMSBTE);

        // WebView settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true); // enable zoom
        webSettings.setDisplayZoomControls(false); // hide zoom buttons
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // Desktop User-Agent to load PC version
        String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/118.0.5993.90 Safari/537.36";
        webSettings.setUserAgentString(desktopUserAgent);

        // Keep navigation inside WebView
        webView.setWebViewClient(new WebViewClient());

        // Load MSBTE result URL
        webView.loadUrl("https://result.msbte.ac.in/pcwebBTRes/pcResult01/pcfrmViewMSBTEResult.aspx");

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Handle back press to navigate WebView history
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
