package com.bluestream.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class CategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView CategoryName;
    public ImageView CategoryImageUrl;
    public RelativeLayout CategoryItemLayout;

    public CategoryListViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        CategoryName = (TextView)itemView.findViewById(R.id.category_text);
        CategoryImageUrl = (ImageView)itemView.findViewById(R.id.category_image);
        CategoryItemLayout = (RelativeLayout)itemView.findViewById(R.id.CategoryItemLayout);
    }

    @Override
    public void onClick(View view) {


    }
}