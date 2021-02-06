package com.vimal.whatsappstatussaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.adapter.PreviewAdapter;
import com.vimal.whatsappstatussaver.adapter.PreviewPhotoAdapter;
import com.vimal.whatsappstatussaver.adapter.PreviewVideoAdapter;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;
import com.vimal.whatsappstatussaver.utils.FileHelper;
import com.vimal.whatsappstatussaver.utils.OnSingleClickListener;
import com.vimal.whatsappstatussaver.utils.ShareHelper;

import java.io.File;
import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {

    private String TAG = "vml";
    private String path = "";
    private int current_position = 0;
    private int act = 0;
    private ArrayList<String> photo_list = new ArrayList<>();
    private PreviewAdapter adapterall;
    private PreviewPhotoAdapter adapterphoto;
    private PreviewVideoAdapter adaptervideo;
    private ViewPager viewPager;
    private ImageView imgBack, ivShare, ivSaveDelete, ivRepost;
    private TextView tvTitle;
    private SpinKitView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        viewPager = findViewById(R.id.pager);
        imgBack = findViewById(R.id.iv_back);
        ivRepost = findViewById(R.id.iv_repost);
        ivShare = findViewById(R.id.iv_share);
        ivSaveDelete = findViewById(R.id.iv_save_delete);
        progressBar = findViewById(R.id.progressBar);
        tvTitle = findViewById(R.id.tv_title);


        Intent i = getIntent();
        current_position = i.getIntExtra("position", 0);
        act = i.getIntExtra("act", 0);
        photo_list = i.getStringArrayListExtra("arraylist");
        if (!photo_list.isEmpty()) {
            try {
                if (act == 0) {
                    adapterphoto = new PreviewPhotoAdapter(PreviewActivity.this, photo_list);
                    viewPager.setAdapter(adapterphoto);
                    ivSaveDelete.setImageResource(R.drawable.download);
                    ivRepost.setImageResource(R.drawable.whatsapp);
                } else if (act == 1) {
                    adaptervideo = new PreviewVideoAdapter(PreviewActivity.this, photo_list, current_position);
                    viewPager.setAdapter(adaptervideo);
                    ivSaveDelete.setImageResource(R.drawable.download);
                    ivRepost.setImageResource(R.drawable.whatsapp);
                } else if (act == 2) {
                    adapterall = new PreviewAdapter(PreviewActivity.this, photo_list, current_position);
                    viewPager.setAdapter(adapterall);
                    ivSaveDelete.setImageResource(R.drawable.download);
                    ivRepost.setImageResource(R.drawable.whatsapp);
                } else {
                    ivSaveDelete.setImageResource(R.drawable.delete);
                    ivRepost.setImageResource(R.drawable.insta);
                    adapterall = new PreviewAdapter(PreviewActivity.this, photo_list, current_position);
                    viewPager.setAdapter(adapterall);
                }

                viewPager.setCurrentItem(current_position);
                path = photo_list.get(current_position);
            } catch (Exception ignored) {
                onBackPressed();
                Toast.makeText(PreviewActivity.this, getString(R.string.went_wrong),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            onBackPressed();
            Toast.makeText(PreviewActivity.this, getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
        }

        imgBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });

        ivSaveDelete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (act == 0 || act == 1) {
                    File file = new File(path);
                    Status status = new Status(file, file.getName(), file.getAbsolutePath());
                    Common.copyFileNonotification(status, PreviewActivity.this);
                } else {
                    deleteImage(current_position);
                }
            }
        });

        ivRepost.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (act == 3) {
                    File file = new File(path);
                    Status status = new Status(file, file.getName(), file.getAbsolutePath());
                    if (status.isVideo()) {
                        ShareHelper.shareVideo(PreviewActivity.this, path, ShareHelper.INSTAGRAM);
                    } else {
                        ShareHelper.shareImage(PreviewActivity.this, path, ShareHelper.INSTAGRAM);
                    }
                } else {
                    File file = new File(path);
                    Status status = new Status(file, file.getName(), file.getAbsolutePath());
                    if (status.isVideo()) {
                        ShareHelper.shareVideo(PreviewActivity.this, path, ShareHelper.WHATSAPP);
                    } else {
                        ShareHelper.shareImage(PreviewActivity.this, path, ShareHelper.WHATSAPP);
                    }
                }

            }
        });

        ivShare.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                File file = new File(path);
                Status status = new Status(file, file.getName(), file.getAbsolutePath());
                if (status.isVideo()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/mp4");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                    startActivity(Intent.createChooser(shareIntent, "Share Video"));
                } else {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                    startActivity(Intent.createChooser(shareIntent, "Share image"));
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_position = position;
                path = photo_list.get(position);
                if (act == 0) {

                } else if (act == 1) {
                    adaptervideo.setPosition(position, true);
                } else {
                    adapterall.setPosition(position, true);
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public interface OnFinished {
        void onFinish(String path);
    }

    private void deleteImage(int position) {
        path = photo_list.get(position);
        current_position = position;
        FileHelper.delete(path);
        Toast.makeText(PreviewActivity.this, getString(R.string.del_img), Toast.LENGTH_SHORT).show();
        if (act == 0) {
            adapterphoto.removeView(position);
            if (position == adapterphoto.getCount()) position--;
            viewPager.setCurrentItem(position);
            if (adapterphoto.getCount() < 1) {
                finish();
            }
        } else if (act == 1) {
            adaptervideo.removeView(position);
            if (position == adaptervideo.getCount()) position--;
            viewPager.setCurrentItem(position);
            if (adaptervideo.getCount() < 1) {
                finish();
            }
        } else {
            adapterall.removeView(position);
            if (position == adapterall.getCount()) position--;
            viewPager.setCurrentItem(position);
            if (adapterall.getCount() < 1) {
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        Glide.with(getApplicationContext()).pauseRequests();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}