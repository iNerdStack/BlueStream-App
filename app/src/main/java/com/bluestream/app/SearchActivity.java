package com.bluestream.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import okio.Utf8;

import static android.view.View.GONE;
import static com.bluestream.app.MainActivity.URL_DATA;
import static java.net.URLEncoder.encode;

public class SearchActivity extends AppCompatActivity {

    private MaterialSearchBar SearchBar;
    private String SearchQuery;
    private List<String> lastSearches;
    private RecyclerView rView;
    private LinearLayoutManager lLayout;
    private SearchAdapter adapter;
    private List<SearchListModel> searchItems;
    private ProgressBar SearchProgressBar;
    SwipeRefreshLayout SearchSwipeContainer;
    LinearLayout SearchProgressBarLayout;
    private RelativeLayout searchListLayout;
    private int PageCount = 0;
    private String EncodedQuery;
    private static boolean isSearchDone = false;



    public void LoadPosts()
    {

        SearchProgressBar.setVisibility(View.VISIBLE);



// Encoded Query
       try{
           EncodedQuery = URLEncoder.encode(SearchQuery, "utf-8");
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/search/"+EncodedQuery+"/0",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        SearchProgressBar.setVisibility(GONE);
                        try {
                            JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                            JSONArray array = jsonObject.getJSONArray("result"); // get array from a defined json bracket.

                            //Now we sort each content of the array
                            for(int i = 0; i<array.length(); i++)
                            {

                                JSONObject o = array.getJSONObject(i);
                                // Now we create a new List Item to arrange them into list.
                                SearchListModel searchListModel = new SearchListModel(
                                        o.getString("post_title"),
                                        o.getString("post_body"),
                                        o.getString("post_desc"),
                                        o.getString("post_image"),
                                        o.getString("created_at")
                                );

                                if(searchItems.contains(searchListModel)){

                                }
                                else
                                {
                                    searchItems.add(searchListModel);
                                }
                            }

                            adapter = new SearchAdapter(searchItems,getApplicationContext());
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
                        SearchProgressBar.setVisibility(GONE);

                        Snackbar.make(SearchSwipeContainer, "Network failed: Unable to fetch data from server",
                                Snackbar.LENGTH_LONG)
                                .show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

 public void LoadPost()
{

    try {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = URL_DATA + "/app/searchs/";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("Search", SearchQuery);
        final String requestBody = jsonBody.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    } catch (JSONException e) {
        e.printStackTrace();
    }
}

    public void SearchEndlessScroll()
    {
        rView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                PageCount ++;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA+"/app/search/"+EncodedQuery+"/"+PageCount,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s); //We create a Json Object.
                                    JSONArray array = jsonObject.getJSONArray("result"); // get array from a defined json bracket.

                                    //Now we sort each content of the array
                                    for(int i = 0; i<array.length(); i++)
                                    {
                                        JSONObject o = array.getJSONObject(i);
                                        // Now we create a new List Item to arrange them into list.

                                        SearchListModel searchListModel = new SearchListModel(
                                                o.getString("post_title"),
                                                o.getString("post_body"),
                                                o.getString("post_desc"),
                                                o.getString("post_image"),
                                                o.getString("created_at")
                                        );
//Checks for dobuble entry

                                        if(searchItems.contains(searchListModel)){

                                        }
                                        else
                                        {

                                            searchItems.add(searchListModel);

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

                                Snackbar.make(SearchSwipeContainer, "Network failed: Unable to fetch data from server",
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
        setContentView(R.layout.activity_search);


        SearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);


        SearchQuery = getIntent().getStringExtra("Search_Query");

        SearchBar.setText(SearchQuery);


        SearchSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.SearchLayout);

        SearchProgressBar = (ProgressBar)findViewById(R.id.SearchProgressbar);
        SearchProgressBarLayout = (LinearLayout)findViewById(R.id.SearchProgressBarLayout);
        searchListLayout = (RelativeLayout)findViewById(R.id.SearchListLayout);

        lLayout = new LinearLayoutManager((this));



        SearchBar.setHint(SearchQuery);






        SearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {


            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                SearchQuery = text.toString();

                searchItems.clear();


                PageCount = 0;



                try{

                    adapter.Clear();

                    adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                }
                catch (Exception ex)
                {

                }


                LoadPosts();
                SearchEndlessScroll();



            }


            @Override
            public void onButtonClicked(int buttonCode) {


            }

        });

        SearchSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)


                searchItems.clear();


                PageCount = 0;



                try{

                    adapter.Clear();

                    adapter.notifyDataSetChanged(); // or notifyItemRangeRemoved
                }
                catch (Exception ex)
                {

                }


                LoadPosts();
                SearchEndlessScroll();

                // ...the data has come back, add new items to your adapter...

                //     adapter.addAll(rowListItem);

                // Now we call setRefreshing(false) to signal refresh has finished

                SearchSwipeContainer.setRefreshing(false);

            }



        });


        SearchSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rView = (RecyclerView)findViewById(R.id.Search_recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);



        searchItems = new ArrayList<>();

        LoadPosts();
        SearchEndlessScroll();

    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //save last queries to disk
       // saveSearchSuggestionToDisk(SearchBar.getLastSuggestions());
    }



}
