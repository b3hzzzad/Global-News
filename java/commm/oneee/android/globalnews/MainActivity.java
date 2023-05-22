package commm.oneee.android.globalnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private NewsApiServiceQuery newsApiServiceQuery;
    String searchQuery;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        // Retrieve the SearchView and set up search functionality
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search operation with the submitted query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes while typing, if needed
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.aboutItem:
                Intent intent = new Intent(MainActivity.this, about.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsApiService = retrofit.create(NewsApiService.class);
        newsApiServiceQuery = retrofit.create(NewsApiServiceQuery.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.notify();

        newsAdapter = new NewsAdapter(newsArticles);
        recyclerView.setAdapter(newsAdapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutMain);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {

                    getData();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(MainActivity.this, "Network Error !!!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

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

    interface NewsApiServiceQuery {
        @GET("everything")
        Call<NewsResponse> getTopHeadlines(
                @Query("apiKey") String apiKey,
                @Query("q") String query
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void performSearch(String query) {
        searchQuery = query;

        Call<NewsResponse> call = newsApiServiceQuery.getTopHeadlines(API_KEY, searchQuery);
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
            }
        });

        // Handle search functionality here
        // This method will be called when the user submits the search query
        // Implement your logic to perform the search operation, filter data, or update the RecyclerView accordingly
    }
}
