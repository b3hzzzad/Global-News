package commm.oneee.android.globalnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String getURL = intent.getStringExtra("xz_code");

        WebView webView = findViewById(R.id.webView);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutWebView);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    webView.loadUrl(getURL);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());

                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(webview.this, "Network Error !!!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        if (isNetworkAvailable()) {
            webView.loadUrl(getURL);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());

            swipeRefreshLayout.setRefreshing(false);
        } else {
            Toast.makeText(webview.this, "Network Error !!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
