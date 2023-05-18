package commm.oneee.android.globalnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "648805f67bdb4aa6a454d4f6480ae35d";
    List<NewsArticle> newsArticles = new ArrayList<>();
    private NewsApiService newsApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsApiService = retrofit.create(NewsApiService.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(newsArticles);
        recyclerView.setAdapter(newsAdapter);

        if (isNetworkAvailable()) {
            getData();
        } else {
            Toast.makeText(this, "Network Error !!!", Toast.LENGTH_SHORT).show();
        }

    }

    void getData() {
        Call<NewsResponse> call = newsApiService.getTopHeadlines("us", API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    NewsResponse newsResponse = response.body();
                    List<NewsArticle> articles = newsResponse.getArticles();

                    // Clear the existing list
                    newsArticles.clear();

                    // Add new articles to the list
                    newsArticles.addAll(articles);

                    // Notify the adapter of the data changes
                    newsAdapter.notifyDataSetChanged();
                } else {
                    // Handle API error
                    Log.e("API Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Handle request failure
            }
        });
    }

    interface NewsApiService {
        @GET("top-headlines")
        Call<NewsResponse> getTopHeadlines(
                @Query("country") String country,
                @Query("apiKey") String apiKey
        );
    }

    class NewsResponse {
        private List<NewsArticle> articles;

        public List<NewsArticle> getArticles() {
            return articles;
        }

        public void setArticles(List<NewsArticle> articles) {
            this.articles = articles;
        }
    }

    class NewsArticle {
        private String description, url, title, urlToImage, content, publishedAt;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getUrl() {
            return url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public String getContent() {
            return content;
        }

        public String getPublishedAt() {
            return publishedAt;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
