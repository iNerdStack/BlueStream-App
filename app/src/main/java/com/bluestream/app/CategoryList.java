package com.bluestream.app;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static com.activeandroid.Cache.getContext;
import static com.bluestream.app.MainActivity.URL_DATA;

public class CategoryList extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    private CategoryPostAdapter adapter;
    private RecyclerView rView;
    private ProgressBar CategoryListProgressBar;
    SwipeRefreshLayout CategoryListSwipeContainer;
    private List<CategoryListModel> CategoryItems;
    LinearLayout CategoryProgressBarLayout;
    private RelativeLayout CategoryListLayout;
    private int PageCount = 0;

    public void LoadPosts() {

        CategoryListProgressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/category/posts/"+GeneralVariables.CategoryID+"/0",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        CategoryListProgressBar.setVisibility(GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                            JSONArray array = jsonObject.getJSONArray("categoryposts"); // get array from a defined json bracket.

                            //Now we sort each content of the array
                            for(int i = 0; i<array.length(); i++)
                            {

                                JSONObject o = array.getJSONObject(i);

                                // Now we create a new List Item to arrange them into list.

                                CategoryListModel categoryListModel = new CategoryListModel(
                                        o.getString("post_title"),
                                        o.getString("post_body"),
                                        o.getString("post_desc"),
                                        o.getString("post_image"),
                                        "10/10/2018",
                                        o.getString("id")
                                        );

                                if(CategoryItems.contains(categoryListModel)){

                                }
                                else
                                {
                                    CategoryItems.add(categoryListModel);
                                }
                            }

                            adapter = new CategoryPostAdapter(CategoryItems, getApplicationContext());
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
                        CategoryListProgressBar.setVisibility(GONE);

                        Snackbar.make(CategoryListSwipeContainer, "Network failed: Unable to fetch data from server",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void CategoryListEndlessScroll() {
        rView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                PageCount ++;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/category/posts/"+GeneralVariables.CategoryID+"/"+PageCount,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                                    JSONArray array = jsonObject.getJSONArray("categoryposts"); // get array from a defined json bracket.

                                    //Now we sort each content of the array
                                    for(int i = 0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        // Now we create a new List Item to arrange them into list.

                                        CategoryListModel categoryListModel = new CategoryListModel(
                                                o.getString("post_title"),
                                                o.getString("post_body"),
                                                o.getString("post_desc"),
                                                o.getString("post_image"),
                                                "10/10/2018",
                                                o.getString("id")
                                        );


                                        if(CategoryItems.contains(categoryListModel)){

                                        }
                                        else
                                        {
                                            CategoryItems.add(categoryListModel);
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

                                Snackbar.make(CategoryListSwipeContainer, "Network failed: Unable to fetch data from server",
                                        Snackbar.LENGTH_LONG)
                                        .show();

                            }
                        });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
      String CategoryName = getIntent().getStringExtra("CategoryName");
        getSupportActionBar().setTitle(CategoryName);

        CategoryListSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.Category_List_Layout);

        CategoryListProgressBar = (ProgressBar) findViewById(R.id.Category_List_Progressbar);
        CategoryProgressBarLayout = (LinearLayout) findViewById(R.id.Category_List_ProgressBarLayout);
        CategoryListLayout = (RelativeLayout) findViewById(R.id.CategoryListLayout);
        lLayout = new LinearLayoutManager((this));

        CategoryListSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {



            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)


                CategoryItems.clear();


                PageCount = 0;


                try {

                    adapter.Clear();

                    adapter.notifyDataSetChanged();
                } catch (Exception ex) {

                }


                LoadPosts();
                CategoryListEndlessScroll();

                // ...the data has come back, add new items to your adapter...

                //     adapter.addAll(rowListItem);

                // Now we call setRefreshing(false) to signal refresh has finished

                CategoryListSwipeContainer.setRefreshing(false);

            }


        });

        CategoryListSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rView = (RecyclerView) findViewById(R.id.Category_List_recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        CategoryItems = new ArrayList<>();

        LoadPosts();
        CategoryListEndlessScroll();

    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



}