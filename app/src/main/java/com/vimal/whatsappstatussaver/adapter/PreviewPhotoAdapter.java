package com.vimal.whatsappstatussaver.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;


import com.vimal.whatsappstatussaver.R;
import com.vimal.whatsappstatussaver.utils.TouchImageView;

import java.util.ArrayList;


public class PreviewPhotoAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    TouchImageView imgDisplay;

    public PreviewPhotoAdapter(Activity activity, ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
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
        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.card_preview_image, container, false);

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay);
        String path = _imagePaths.get(position);

        try {
            Bitmap bmImg = BitmapFactory.decodeFile(path);
            imgDisplay.setImageBitmap(bmImg);
        } catch (Exception e) {

        }
        container.addView(viewLayout);
        return viewLayout;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);

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
