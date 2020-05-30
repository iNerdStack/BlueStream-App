package com.bluestream.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.webkit.WebView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.andremion.counterfab.CounterFab;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import static com.bluestream.app.MainActivity.URL_DATA;

public class PostActivity extends AppCompatActivity {

    private ImageView PostImage;
    private WebView PostDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PostDesc = (WebView) findViewById(R.id.SinglePostContent);
        PostImage = (ImageView) findViewById(R.id.PostIMG);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);


        String htmlstring =  GeneralVariables.PostDescString;

        PostDesc.loadData(htmlstring,"text/html", null);

        setTitle(GeneralVariables.PostHeadString);



        CounterFab counterFab = (CounterFab) findViewById(R.id.counter_fab);
       // counterFab.setCount(10); // Set the count value to show on badge
       // counterFab.increase(); // Increase the current count value by 1
        //counterFab.decrease(); // Decrease the current count value by 1

        if (GeneralVariables.isPostOffline == false) {

            String ImageUrl = URL_DATA + "/posts/" + GeneralVariables.PostUrlString;

            Picasso.get()
                    .load(ImageUrl)
                    .resize(300, 300)
                    .into(PostImage);
        }
        else
        {

            try
            {
                byte[] imageBytes = android.util.Base64.decode(GeneralVariables.PostImage, android.util.Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                PostImage.setImageBitmap(decodedImage);

            }
            catch (ArrayIndexOutOfBoundsException e) {

            }

        }



    }


    public void shareLink(View view)
    {
        String ShareLink = URL_DATA + "url_" + GeneralVariables.PostSlugString;

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, GeneralVariables.PostHeadString);
        share.putExtra(Intent.EXTRA_TEXT, ShareLink);

        startActivity(Intent.createChooser(share, "Share link to..."));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}
