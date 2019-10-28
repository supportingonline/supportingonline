package com.egypt.supporting.supportingonline.Custom;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceRecycler_V extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceRecycler_V(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;


    }
}