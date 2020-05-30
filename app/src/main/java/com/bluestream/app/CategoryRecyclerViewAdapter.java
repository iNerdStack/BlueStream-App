package com.bluestream.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import static com.bluestream.app.MainActivity.URL_DATA;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {

    private List<CategoryModel> itemList;
    private Context context;

    public CategoryRecyclerViewAdapter(Context context, List<CategoryModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public CategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, null);
        CategoryListViewHolder rcv = new CategoryListViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(CategoryListViewHolder holder, final int position) {



        holder.CategoryName.setText(itemList.get(position).getCategoryName());

        String ImageUrl;

        ImageUrl = URL_DATA + "/posts/category/" + itemList.get(position).getCategoryImageUrl();


        Picasso.get()
                .load(ImageUrl)
                .resize(500,500)
                .into(holder.CategoryImageUrl);


        holder.CategoryItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CategoryList.class);

                GeneralVariables.CategoryID = itemList.get(position).getCategoryId();
                intent.putExtra("CategoryName", itemList.get(position).getCategoryName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }





// Clean all elements of the recycler

    public void clear() {

        itemList.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<CategoryModel> list) {

        itemList.addAll(list);

        notifyDataSetChanged();

    }
}