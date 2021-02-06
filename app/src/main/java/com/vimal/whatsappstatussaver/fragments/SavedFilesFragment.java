package com.vimal.whatsappstatussaver.fragments;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.adapter.FilesAdapter;
import com.vimal.whatsappstatussaver.interfaces.FragmentInterface;
import com.vimal.whatsappstatussaver.models.Status;
import com.vimal.whatsappstatussaver.utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SavedFilesFragment extends Fragment implements FragmentInterface {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Status> savedFilesList = new ArrayList<>();
    private ArrayList<String> photoList = new ArrayList<>();
    private Handler handler = new Handler();
    private FilesAdapter filesAdapter;
    private TextView no_files_found;

    static SavedFilesFragment savedFilesFragment = null;

    public static SavedFilesFragment newInstance() {
        if (savedFilesFragment == null)
            savedFilesFragment = new SavedFilesFragment();
        return savedFilesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.ss_fragment_saved, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFiles);
        progressBar = view.findViewById(R.id.progressBar);
        no_files_found = view.findViewById(R.id.no_files_found);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("vml", "resume");
        getFiles();
    }


    @Override
    public void updateView() {
        getFiles();
    }


    private void getFiles() {

        try {
            final File app_dir = new File(Common.APP_DIR);

            if (app_dir.exists()) {

                no_files_found.setVisibility(View.GONE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File[] savedFiles = null;
                        savedFiles = app_dir.listFiles();
                        savedFilesList.clear();
                        photoList.clear();

                        if (savedFiles != null && savedFiles.length > 0) {

                            Arrays.sort(savedFiles);
                            for (File file : savedFiles) {
                                Status status = new Status(file, file.getName(), file.getAbsolutePath());

                                if (status.isVideo())
                                    status.setThumbnail(getThumbnail(status));
                                savedFilesList.add(status);
                                photoList.add(status.getPath());
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (savedFilesList.size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        filesAdapter = new FilesAdapter(savedFilesList, photoList);
                                        recyclerView.setAdapter(filesAdapter);
                                        progressBar.setVisibility(View.GONE);
                                        no_files_found.setVisibility(View.GONE);
                                        filesAdapter.notifyDataSetChanged();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                        no_files_found.setVisibility(View.VISIBLE);
                                    }

                                }
                            });

                        } else {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    no_files_found.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    }
                }).start();

            } else {
                no_files_found.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }


    }

    private Bitmap getThumbnail(Status status) {
        return ThumbnailUtils.createVideoThumbnail(status.getFile().getAbsolutePath(),
                3);
    }
}
