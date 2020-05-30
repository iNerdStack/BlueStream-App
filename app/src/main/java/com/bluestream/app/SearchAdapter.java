package com.bluestream.app;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import static android.view.View.inflate;
import static com.bluestream.app.MainActivity.URL_DATA;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {

    private List<SearchListModel> searchList;
    private Context context;


    public SearchAdapter(List<SearchListModel> searchListModel, Context context)
    {

        this.searchList = searchListModel;
        this.context = context;



    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);



         return new ViewHolder(v); // Binds our list_item to the adapter


    }

    @Nullable
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        //It gives the specific position of list items

        final SearchListModel listItem = searchList.get(position);

        holder.textViewhead.setText(listItem.getHead());
        holder.textViewDesc.setText(listItem.getPostDesc());


        String ImageUrl;

        ImageUrl = URL_DATA + "/posts/" + searchList.get(position).getPostImageUrl();

        Picasso.get()
                .load(ImageUrl)
                .resize(100,100)
                .into(holder.SearchImage);

        holder.SearchListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(context, PostActivity.class);

                //GeneralVariables.PostId = listItem.getPostId();
                GeneralVariables.PostDescString = listItem.getDesc();
                GeneralVariables.PostHeadString = listItem.getHead();
                GeneralVariables.PostContentString = listItem.getPostDesc();
                GeneralVariables.PostUrlString = listItem.getPostImageUrl();
                GeneralVariables.isPostOffline = false;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //this controls number of items to recycle

        return searchList.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{

        //All View Objects are defined here

        public TextView textViewhead;
        public TextView textViewDesc;
        public ImageView SearchImage;
        public RelativeLayout SearchListLayout;
        public RecyclerView Search_Recylerview;
        private View mView;;


        public ViewHolder(View itemView) {
            super(itemView);

            //This is where we define all necessary information from the list view item xml

            textViewhead = (TextView)itemView.findViewById(R.id.searchheader);
            textViewDesc = (TextView)itemView.findViewById(R.id.searchcontent);
            SearchImage = (ImageView)itemView.findViewById(R.id.search_image);
            SearchListLayout = (RelativeLayout)itemView.findViewById(R.id.SearchListLayout);

            Search_Recylerview =(RecyclerView)itemView.findViewById(R.id.Search_recyclerView);


        }
    }


    public void Clear() {

        searchList.clear();
        notifyDataSetChanged();

    }

    public void addAll(List<CategoryModel> list) {

        // homeList.addAll(list);

        notifyDataSetChanged();

    }
}