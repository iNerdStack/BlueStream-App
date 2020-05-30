package com.bluestream.app;


import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import com.bluestream.app.CategoryModel;

import static android.view.View.GONE;
import static com.bluestream.app.MainActivity.URL_DATA;


/**
 * A simple {@link Fragment} subclass.
 */
public class Two extends Fragment {

    private ProgressBar categoryProgressbar;
    private GridLayoutManager lLayout;
    private RecyclerView rView;
    List<CategoryModel> rowListItem;
    CategoryRecyclerViewAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    LinearLayout categoryProgressBarLayout;

    private int PageCount = 0;


    public Two() {
        // Required empty public constructor
    }

    public void LoadCategories()
    {

        categoryProgressbar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/category/0",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        categoryProgressbar.setVisibility(GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                            JSONArray array = jsonObject.getJSONArray("category"); // get array from a defined json bracket.

                            //Now we sort each content of the array
                            for(int i = 0; i<array.length(); i++)
                            {

                                JSONObject o = array.getJSONObject(i);
                                // Now we create a new List Item to arrange them into list.
                                CategoryModel categoryModel = new CategoryModel(
                                        o.getString("category"),
                                        o.getString("image_url"),
                                        o.getString("id")
                                );

                                  if(rowListItem.contains(categoryModel)){

                                }
                                else
                                {
                                    rowListItem.add(categoryModel);
                                }
                            }

                            adapter = new CategoryRecyclerViewAdapter(getContext(),rowListItem);
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
                        categoryProgressbar.setVisibility(GONE);

                        Snackbar.make(swipeContainer, "Network failed: Unable to fetch data from server",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);



    }


    public void EndlessScroll()
    {
        rView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                PageCount ++;

             StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/category/"+PageCount,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                                    JSONArray array = jsonObject.getJSONArray("category"); // get array from a defined json bracket.

                                    //Now we sort each content of the array
                                    for(int i = 0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        // Now we create a new List Item to arrange them into list.
                                        CategoryModel categoryModel = new CategoryModel(
                                                o.getString("category"),
                                                o.getString("image_url"),
                                        o.getString("id")
                                        );
//Checks for dobuble entry

                                        if(rowListItem.contains(categoryModel)){

       }
                                        else
                                        {

                                            rowListItem.add(categoryModel);


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

                                Snackbar.make(swipeContainer, "Network failed: Unable to fetch posts from server",
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_two,
                container, false);



        lLayout = new GridLayoutManager((getActivity()), 2);

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.CategorySwipeRefresh);

        categoryProgressbar = (ProgressBar)rootView.findViewById(R.id.CategoryProgressbar);
        categoryProgressBarLayout = (LinearLayout)rootView.findViewById(R.id.CategoryProgressBarLayout);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)


                rowListItem.clear();


                PageCount = 0;



                try{

          adapter.clear();

        adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
    }
                catch (Exception ex)
    {

    }


    LoadCategories();
    EndlessScroll();

    // ...the data has come back, add new items to your adapter...

    //     adapter.addAll(rowListItem);

    // Now we call setRefreshing(false) to signal refresh has finished

                swipeContainer.setRefreshing(false);

}



        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        rView = (RecyclerView)rootView.findViewById(R.id.Category_recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);


        rowListItem= new ArrayList<>();
        LoadCategories();
        EndlessScroll();


        return rootView;

    }

}
