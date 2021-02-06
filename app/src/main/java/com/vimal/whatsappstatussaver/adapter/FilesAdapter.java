package com.vimal.whatsappstatussaver.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.activity.PreviewActivity;
import com.vimal.whatsappstatussaver.models.Status;

import java.util.ArrayList;
import java.util.List;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ItemViewHolder> {

    private List<Status> data;
    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    public FilesAdapter(List<Status> data, ArrayList<String> list) {
        this.data = data;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ss_item_saved, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {

        holder.share.setVisibility(View.VISIBLE);
        holder.delete.setVisibility(View.VISIBLE);
        holder.whatsapp.setVisibility(View.GONE);
        holder.insta.setVisibility(View.GONE);

        final Status status = data.get(position);

        holder.whatsapp.setVisibility(View.VISIBLE);
        holder.insta.setVisibility(View.GONE);


        if (status.isVideo())
            holder.imageView.setImageBitmap(status.getThumbnail());
        else
            Picasso.get().load(status.getFile()).into(holder.imageView);

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                if (status.isVideo())
                    shareIntent.setType("image/mp4");
                else
                    shareIntent.setType("image/jpg");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(status.getFile().getAbsolutePath()));
                context.startActivity(Intent.createChooser(shareIntent, "Share image"));

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.getFile().delete();
                data.remove(position);
                notifyDataSetChanged();

            }
        });

        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setPackage("com.whatsapp");
                if (status.isVideo())
                    whatsappIntent.setType("image/mp4");
                else
                    whatsappIntent.setType("image/jpg");

                whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
                //context.startActivity(Intent.createChooser(whatsappIntent, "Share image"));
                try {
                    context.startActivity(Intent.createChooser(whatsappIntent, "Share image"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apppackage = "com.instagram.android";
                try {
                    Intent i = context.getPackageManager().getLaunchIntentForPackage(apppackage);
                    context.startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(context, "Sorry, Instagram App Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("vml", "click");

                Intent intent = new Intent(context, PreviewActivity.class);
                intent.putExtra("path", data.get(position).getPath());
                intent.putExtra("position", position);
                intent.putExtra("act", 2);
                intent.putExtra("arraylist", list);
                context.startActivity(intent);

            }
        });

    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView save, share, delete, whatsapp, insta;
        public ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivThumbnail);
            save = itemView.findViewById(R.id.save);
            share = itemView.findViewById(R.id.share);
            delete = itemView.findViewById(R.id.delte);
            whatsapp = itemView.findViewById(R.id.whatsapp);
            insta = itemView.findViewById(R.id.insta);

        }
    }

    public void removeAt(int position1) {
        data.remove(position1);
        notifyItemRemoved(position1);
        notifyItemRangeChanged(position1, data.size());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
