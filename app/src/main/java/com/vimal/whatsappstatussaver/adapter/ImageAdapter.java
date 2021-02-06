package com.vimal.whatsappstatussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.activity.PreviewActivity;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {

    private List<Status> data;
    private Context context;
    private ArrayList<String> list = new ArrayList<>();
    private RelativeLayout container;

    public ImageAdapter(List<Status> data, RelativeLayout container, ArrayList<String> list) {
        this.data = data;
        this.container = container;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ss_item_status, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        final Status status = data.get(position);
        Picasso.get().load(status.getFile()).into(holder.imageView);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.copyFile(status, context, container);

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("image/jpg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(status.getFile().getAbsolutePath()));
                context.startActivity(Intent.createChooser(shareIntent, "Share image"));

            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("path", data.get(position).getPath());
                intent.putExtra("position", position);
                intent.putExtra("act", 0);
                intent.putExtra("arraylist", list);
                context.startActivity(intent);

            }
        });

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView save, share;
        public ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivThumbnail);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);


        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
