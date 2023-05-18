package commm.oneee.android.globalnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class webView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ProgressBar progressBar = findViewById(R.id.progressBar2);

        Intent intent = getIntent();
        String getURL = intent.getStringExtra("xz_key");

        WebView webView = findViewById(R.id.webView);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);

                setProgress(progress * 100);

                if (progress == 100)

                    progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(getURL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

    }
}