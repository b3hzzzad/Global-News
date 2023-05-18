package commm.oneee.android.globalnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String news_url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=648805f67bdb4aa6a454d4f6480ae35d";
    OkHttpClient okHttpClient;
    String jsonDataString, title, description, image_url, date, get_url;
    Response response;
    JSONObject jsonObject;
    RecyclerView recyclerView;
    mAdapter mAdapter;
    ArrayList<mDATA> arrayList;
    getDATA getDATA;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDATA = new getDATA();

        okHttpClient = new OkHttpClient();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    finish();
                    startActivity(getIntent());
                } else {
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (isNetworkAvailable()) {
            getDATA.execute();
        } else {
            Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
        }

    }

    class getDATA extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {

            recyclerView.setAdapter(mAdapter);
            super.onPostExecute(unused);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Request request = new Request.Builder().url(news_url).build();
            try {
                response = okHttpClient.newCall(request).execute();
                jsonDataString = response.body().toString();

                jsonDataString = response.body().string();

                jsonObject = new JSONObject(jsonDataString);

                for (int i = 0; i < 20; i++) {

                    JSONObject jsonObject1 = jsonObject.getJSONArray("articles").getJSONObject(i);
                    title = jsonObject1.getString("title");
                    description = jsonObject1.getString("description");
                    image_url = jsonObject1.getString("urlToImage");
                    date = jsonObject1.getString("publishedAt");
                    get_url = jsonObject1.getString("url");

                    String mDate = date.substring(0, 10);

                    arrayList.add(new mDATA(title, description, image_url, mDate, get_url));
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            mAdapter = new mAdapter(arrayList, MainActivity.this);

            return null;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}