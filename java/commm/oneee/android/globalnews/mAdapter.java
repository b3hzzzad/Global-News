package commm.oneee.android.globalnews;

import android.content.Context;
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

public class mAdapter extends RecyclerView.Adapter<mAdapter.MyViewHolder> {

    List<mDATA> list;
    Context context;
    mDATA mDATA_xz;

    public mAdapter(List<mDATA> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mDATA_xz = list.get(position);

        holder.title.setText(mDATA_xz.getTitle());
        holder.description.setText(mDATA_xz.getDescription());
        holder.date.setText(mDATA_xz.getDate());

        Glide.with(context)
                //.asGif()
                .load(mDATA_xz.getImage())
                .centerCrop()
                .placeholder(R.drawable.loading_image)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, webView.class);
                intent.putExtra("xz_key",mDATA_xz.getmURL());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, date;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
            date = itemView.findViewById(R.id.date);
        }
    }
}
