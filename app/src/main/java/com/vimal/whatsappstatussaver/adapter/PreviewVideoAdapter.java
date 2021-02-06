package com.vimal.whatsappstatussaver.adapter;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.github.ybq.android.spinkit.SpinKitView;
import com.vimal.whatsappstatussaver.R;

import java.util.ArrayList;

public class PreviewVideoAdapter extends PagerAdapter {

    private String TAG = "vml";
    private Context mContext;
    private int curr_position;
    private ArrayList<String> videoList;
    private int mSeekPosition = 0;
    private boolean isPlaying = true;
    private LayoutInflater inflater;
    android.widget.VideoView vvMyCreationVideo;
    ImageView playpause;
    private MediaPlayer mMediaPlayer;

    public PreviewVideoAdapter(Context mContext, ArrayList<String> imagePaths, int curr_position) {
        this.mContext = mContext;
        this.videoList = imagePaths;
        this.curr_position = curr_position;
    }

    @Override
    public int getCount() {
        return this.videoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_preview_video, container, false);


        final SpinKitView progressBar = view.findViewById(R.id.progressbar);
        vvMyCreationVideo = view.findViewById(R.id.vvMyCreationVideo);
        playpause = view.findViewById(R.id.playpause);

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
                    mMediaPlayer.pause();
                    playpause.setImageResource(R.drawable.playsong);
                } else {
                    playpause.setImageResource(R.drawable.icon_pause);
                    mMediaPlayer.start();
                }
            }
        });

        String video_path = videoList.get(position);
        if (position == curr_position) {
            progressBar.setVisibility(View.GONE);
            vvMyCreationVideo.setVideoURI(Uri.parse(video_path));
            vvMyCreationVideo.start();
//            playpause.setImageResource(R.drawable.icon_pause);
        } else {
//            playpause.setImageResource(R.drawable.playsong);
            vvMyCreationVideo.pause();
        }


        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

    }

    public void removeView(int position) {
        videoList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setPosition(int postion, boolean isPlaying) {
        this.curr_position = postion;
        this.isPlaying = isPlaying;
        notifyDataSetChanged();
    }





}
