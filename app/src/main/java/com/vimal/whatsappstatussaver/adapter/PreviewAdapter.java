package com.vimal.whatsappstatussaver.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.github.ybq.android.spinkit.SpinKitView;
import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.TouchImageView;

import java.io.File;
import java.util.ArrayList;


public class PreviewAdapter extends PagerAdapter {

    private String TAG = "vml";
    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private int curr_position;
    private int mSeekPosition = 0;
    private boolean isPlaying = true;
    TouchImageView imgDisplay;
    FrameLayout image, video;
    android.widget.VideoView vvMyCreationVideo;
    ImageView playpause;
    private MediaPlayer mMediaPlayer;

    public PreviewAdapter(Activity activity,
                          ArrayList<String> imagePaths, int curr_position) {
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.curr_position = curr_position;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.card_preview, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
        image = viewLayout.findViewById(R.id.image);
        video = viewLayout.findViewById(R.id.video);
        vvMyCreationVideo = viewLayout.findViewById(R.id.vvMyCreationVideo);
        playpause = viewLayout.findViewById(R.id.playpause);

        String path = _imagePaths.get(position);
        File file = new File(path);
        Status status = new Status(file, file.getName(), file.getAbsolutePath());

        if (status.isVideo()) {
            video.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            final SpinKitView progressBar = viewLayout.findViewById(R.id.progressbar);
            vvMyCreationVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer = mp;
                }
            });

            vvMyCreationVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mMediaPlayer.isPlaying()) {
                        playpause.setImageResource(R.drawable.playsong);
                        mMediaPlayer.pause();
                    } else {
                        playpause.setImageResource(R.drawable.icon_pause);
                        mMediaPlayer.start();
                    }
                }
            });

            if (position == curr_position) {
                vvMyCreationVideo.setVideoURI(Uri.parse(path));
                vvMyCreationVideo.start();
                playpause.setImageResource(R.drawable.icon_pause);
                progressBar.setVisibility(View.GONE);
            } else {
                vvMyCreationVideo.pause();
                playpause.setImageResource(R.drawable.playsong);
                Log.e("vils", path + " videoView.pause()");
            }
        } else {
            video.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            try {
                Bitmap bmImg = BitmapFactory.decodeFile(path);
                imgDisplay.setImageBitmap(bmImg);
            } catch (Exception e) {

            }
        }
        container.addView(viewLayout);
        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);

    }

    public void setPosition(int postion, boolean isPlaying) {
        this.curr_position = postion;
        this.isPlaying = isPlaying;
        notifyDataSetChanged();
    }

    public void removeView(int position) {
        _imagePaths.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


}
