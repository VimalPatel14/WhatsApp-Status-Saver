package com.vimal.whatsappstatussaver.fragments;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.adapter.VideoAdapter;
import com.vimal.whatsappstatussaver.interfaces.FragmentInterface;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VideoFragment extends Fragment implements FragmentInterface {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Status> videoList = new ArrayList<>();
    private Handler handler = new Handler();
    private VideoAdapter videoAdapter;
    private ArrayList<String> photoList = new ArrayList<>();
    private RelativeLayout container;
    static VideoFragment videoFragment = null;

    public static VideoFragment newInstance() {
        if (videoFragment == null)
            videoFragment = new VideoFragment();
        return videoFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ss_fragment_videos, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerViewVideo);
        progressBar = view.findViewById(R.id.prgressBarVideo);
        container = view.findViewById(R.id.videos_container);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getStatus();
    }

    @Override
    public void updateView() {
        getStatus();
    }

    private void getStatus() {

        if (Common.STATUS_DIRECTORY.exists()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles = Common.STATUS_DIRECTORY.listFiles();
                    videoList.clear();
                    photoList.clear();
                    if (statusFiles != null && statusFiles.length > 0) {

                        Arrays.sort(statusFiles);
                        for (File file : statusFiles) {
                            Status status = new Status(file, file.getName(), file.getAbsolutePath());

                            if (status.isVideo()) {
                                videoList.add(status);
                                photoList.add(file.getAbsolutePath());

                                status.setThumbnail(getThumbnail(status));
                            }

                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                videoAdapter = new VideoAdapter(videoList, container, photoList);
                                recyclerView.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    } else {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Dir doest not exists", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).start();

        } else {
            Toast.makeText(getActivity(), "Cant find WhatsApp Dir", Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getThumbnail(Status status) {
        return ThumbnailUtils.createVideoThumbnail(status.getFile().getAbsolutePath(),
                3);
    }

}
