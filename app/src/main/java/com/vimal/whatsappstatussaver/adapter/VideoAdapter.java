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

import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.activity.PreviewActivity;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;

import java.util.ArrayList;
import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ItemViewHolder> {

    private List<Status> list;
    private Context context;
    private ArrayList<String> data = new ArrayList<>();
    private RelativeLayout container;

    public VideoAdapter(List<Status> list, RelativeLayout container, ArrayList<String> data) {
        this.list = list;
        this.container = container;
        this.data = data;
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
        final Status status = list.get(position);
        holder.imageView.setImageBitmap(status.getThumbnail());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                shareIntent.setType("image/mp4");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(status.getFile().getAbsolutePath()));
                context.startActivity(Intent.createChooser(shareIntent, "Share image"));

            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("path", list.get(position).getPath());
                intent.putExtra("position", position);
                intent.putExtra("act", 1);
                intent.putExtra("arraylist", data);
                context.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.copyFile(status, context, container);
            }
        });

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView save, share, whatsapp, insta;
        public ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivThumbnail);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
            insta = itemView.findViewById(R.id.insta);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
