package com.bluestream.app;



import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.bluestream.app.MainActivity.URL_DATA;


/**
 * A simple {@link Fragment} subclass.
 */
public class One extends Fragment {

    private RecyclerView rView;
    private LinearLayoutManager lLayout;
    private PostAdapter adapter;
    private List<HomeListModel> homeItems;
    private ProgressBar PostProgressBar;
    SwipeRefreshLayout PostSwipeContainer;
    LinearLayout PostProgressBarLayout;
    private RelativeLayout homeListLayout;


    private int PageCount = 0;


    public One() {
        // Required empty public constructor
    }


    public void LoadPosts()
    {

        PostProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/home/0",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        PostProgressBar.setVisibility(GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                            JSONArray array = jsonObject.getJSONArray("home"); // get array from a defined json bracket.

                            //Now we sort each content of the array
                            for(int i = 0; i<array.length(); i++)
                            {

                                JSONObject o = array.getJSONObject(i);
                                // Now we create a new List Item to arrange them into list.
                                HomeListModel homeListModel = new HomeListModel(
                                        o.getString("post_title"),
                                        o.getString("post_body"),
                                        o.getString("post_desc"),
                                        o.getString("post_image"),
                                        o.getString("created_at"),
                                        o.getString("id"),
                                        o.getString("post_slug")
                                );

                                if(homeItems.contains(homeListModel)){

                                }
                                else
                                {
                                    homeItems.add(homeListModel);
                                }
                            }

                            adapter = new PostAdapter(homeItems,getContext());
                            rView.setAdapter(adapter);

                            adapter.notifyItemInserted(array.length());
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Volley requires an error method
                        PostProgressBar.setVisibility(GONE);

                        Snackbar.make(PostSwipeContainer, "Network failed: Unable to fetch post from server",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }






    public void PostEndlessScroll()
    {
        rView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                PageCount ++;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/home/"+PageCount,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                                    JSONArray array = jsonObject.getJSONArray("home"); // get array from a defined json bracket.

                                    //Now we sort each content of the array
                                    for(int i = 0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        // Now we create a new List Item to arrange them into list.

                                        HomeListModel homeListModel = new HomeListModel(
                                                o.getString("post_title"),
                                                o.getString("post_body"),
                                                o.getString("post_desc"),
                                                o.getString("post_image"),
                                                o.getString("created_at"),
                                        o.getString("id"),
                                                o.getString("post_slug")

                                        );
//Checks for dobuble entry

                                        if(homeItems.contains(homeListModel)){

                                        }
                                        else
                                        {

                                            homeItems.add(homeListModel);

                                        }
                                    }
                                    adapter.notifyItemInserted(array.length());
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //Volley requires an error method
//                                Toast.makeText(getActivity().getApplicationContext(), volleyError.getMessage(),Toast.LENGTH_LONG).show();

                                Snackbar.make(PostSwipeContainer, "Network failed: Unable to fetch data from server",
                                        Snackbar.LENGTH_LONG)
                                        .show();

                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                requestQueue.add(stringRequest);

            }
        });

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_one,
                container, false);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_one, container, false);

        PostSwipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.PostSwipeRefresh);

        PostProgressBar = (ProgressBar)rootView.findViewById(R.id.PostProgressbar);
        PostProgressBarLayout = (LinearLayout)rootView.findViewById(R.id.PostProgressBarLayout);
        homeListLayout = (RelativeLayout)rootView.findViewById(R.id.HomeListLayout);

        lLayout = new LinearLayoutManager((getActivity()));


        //Onlongclick on each layout


        PostSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)


                homeItems.clear();


                PageCount = 0;



                try{

                    adapter.Clear();

                    adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                }
                catch (Exception ex)
                {

                }


                LoadPosts();
                PostEndlessScroll();

                // ...the data has come back, add new items to your adapter...

                //     adapter.addAll(rowListItem);

                // Now we call setRefreshing(false) to signal refresh has finished

                PostSwipeContainer.setRefreshing(false);
            }



        });


        PostSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rView = (RecyclerView) rootView.findViewById(R.id.Post_recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);



        homeItems = new ArrayList<>();

        LoadPosts();
        PostEndlessScroll();



        return rootView;
    }



}
