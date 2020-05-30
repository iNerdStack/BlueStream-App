package com.bluestream.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import static com.bluestream.app.MainActivity.URL_DATA;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {

    private List<HomeListModel> homeList;
    private Context context;

    public PostAdapter(List<HomeListModel> homeListModels, Context context)
    {

        this.homeList = homeListModels;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_list, parent, false);

        return new ViewHolder(v); // Binds our list_item to the adapter


    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //It gives the specific position of list items

        final HomeListModel listItem = homeList.get(position);

        holder.textViewhead.setText(listItem.getHead());
      holder.textViewDesc.setText(listItem.getPostDesc());


        String ImageUrl;

        ImageUrl = URL_DATA + "/posts/" + homeList.get(position).getPostImageUrl();


        Picasso.get()
                .load(ImageUrl)
                .resize(100,100)
                .into(holder.PostImage);






        holder.homeListLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //Onlong Click on an item

                BitmapDrawable drawable = (BitmapDrawable)holder.PostImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                byte[] bb = bos.toByteArray();
                holder.Postimage = com.bluestream.app.Base64.encodeBytes(bb);


                GeneralVariables.PostHeadString = listItem.getHead();
                GeneralVariables.PostDescString = listItem.getPostDesc();
                GeneralVariables.PostId = listItem.getPostId();
                GeneralVariables.PostContentString = listItem.getDesc();
                GeneralVariables.PostSlugString = listItem.getPostSlug();
                GeneralVariables.PostImage = holder.Postimage;



                View view = ((FragmentActivity)context).getLayoutInflater().inflate(R.layout.fragment_bottom_sheet, null);

                return false;
            }

        });


        holder.homeListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostActivity.class);

               GeneralVariables.PostDescString = listItem.getDesc();
               GeneralVariables.PostHeadString = listItem.getHead();
                GeneralVariables.PostContentString = listItem.getPostDesc();
               GeneralVariables.PostUrlString = listItem.getPostImageUrl();
                GeneralVariables.PostSlugString = listItem.getPostSlug();
                GeneralVariables.isPostOffline = false;
                GeneralVariables.PostId = listItem.getPostId();

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //this controls number of items to recycle

        return homeList.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        //All View Objects are defined here

        public TextView textViewhead;
        public TextView textViewDesc;
        public ImageView PostImage;
         public RelativeLayout homeListLayout;
         public String Postimage;

        public ViewHolder(View itemView) {
            super(itemView);

            //This is where we define all necessary information from the list view item xml

            textViewhead = (TextView)itemView.findViewById(R.id.postheader);
            textViewDesc = (TextView)itemView.findViewById(R.id.postcontent);
            PostImage = (ImageView)itemView.findViewById(R.id.post_image);
            homeListLayout = (RelativeLayout)itemView.findViewById(R.id.HomeListLayout);

        }
    }


    public void Clear() {

        homeList.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<CategoryModel> list) {

       // homeList.addAll(list);

        notifyDataSetChanged();

    }
}