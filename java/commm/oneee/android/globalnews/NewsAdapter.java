package commm.oneee.android.globalnews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<MainActivity.NewsArticle> articles;

    public NewsAdapter(List<MainActivity.NewsArticle> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recyclerviewitems, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        MainActivity.NewsArticle article = articles.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.contentTextView.setText(article.getDescription());
        holder.publishedAt.setText(article.getPublishedAt().substring(0, 10));
        holder.author.setText(article.getAuthor());

        Glide.with(holder.itemView.getContext())
                .load(article.getUrlToImage())
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = article.getUrl();
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(holder.imageView.getContext(), webview.class);
                    intent.putExtra("xz_code", article.getUrl());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView, publishedAt, author;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextview);
            imageView = itemView.findViewById(R.id.imageView);
            publishedAt = itemView.findViewById(R.id.date);
            author = itemView.findViewById(R.id.author);
        }
    }
}
