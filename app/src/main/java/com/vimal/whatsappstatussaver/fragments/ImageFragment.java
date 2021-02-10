package com.vimal.whatsappstatussaver.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
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
import com.vimal.whatsappstatussaver.adapter.ImageAdapter;
import com.vimal.whatsappstatussaver.interfaces.FragmentInterface;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageFragment extends Fragment implements FragmentInterface {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Status> imagesList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private Handler handler = new Handler();
    private ImageAdapter imageAdapter;
    private RelativeLayout container;

    static ImageFragment imageFragment = null;

    public static ImageFragment newInstance() {
        if (imageFragment == null)
            imageFragment = new ImageFragment();
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ss_fragment_images, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewImage);
        progressBar = view.findViewById(R.id.prgressBarImage);
        container = view.findViewById(R.id.image_container);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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
            Log.e("vml", Common.STATUS_DIRECTORY + " getStatus");

                    File[] statusFiles = null;
                    statusFiles = Common.STATUS_DIRECTORY.listFiles();
                    imagesList.clear();
                    photoList.clear();

                    if (statusFiles != null && statusFiles.length > 0) {

                        Arrays.sort(statusFiles);
                        for (File file : statusFiles) {
                            Status status = new Status(file, file.getName(), file.getAbsolutePath());

                            if (!status.isVideo()) {
                                if (!status.isNoload()) {
                                    imagesList.add(status);
                                    photoList.add(file.getAbsolutePath());
                                }

                            }

                        }



                                imageAdapter = new ImageAdapter(imagesList, container, photoList);
                                recyclerView.setAdapter(imageAdapter);
                                imageAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);


                    } else {


                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Dir doest not exists", Toast.LENGTH_SHORT).show();


                    }


        } else {
            Toast.makeText(getActivity(), "Cant find WhatsApp Dir", Toast.LENGTH_SHORT).show();
        }

    }

}
