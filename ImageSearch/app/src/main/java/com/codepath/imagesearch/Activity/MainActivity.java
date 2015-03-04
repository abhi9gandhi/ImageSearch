package com.codepath.imagesearch.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.imagesearch.Adaptor.ImageResultAdoptor;
import com.codepath.imagesearch.EndlessScrollListener;
import com.codepath.imagesearch.Model.ImagePage;
import com.codepath.imagesearch.Model.ImageResult;
import com.codepath.imagesearch.Model.ImageSetting;
import com.codepath.imagesearch.R;
import com.codepath.imagesearch.SettingsFragment;
import com.etsy.android.grid.StaggeredGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements SettingsFragment.EditNameDialogListener {
    private ArrayList<ImageResult> ImageResultArray;
    private ImageResult result;
    private ArrayList<ImagePage> apage;
    JSONArray pageArray;
    private ImageResultAdoptor aresult;
  //  private GridView grid;
    private StaggeredGridView grid;
    private ImageSetting settings;
    private String searchQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        settings = new ImageSetting();
        ImageResultArray = new ArrayList<ImageResult>();
        aresult = new ImageResultAdoptor(this,ImageResultArray);

       // grid = (GridView)findViewById(R.id.GvImageGrid);
        grid = (StaggeredGridView)findViewById(R.id.GvStaggeredGridView);
        grid.setAdapter(aresult);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageResult item = (ImageResult)ImageResultArray.get(position);
                Intent i = new Intent(MainActivity.this, ImageFullScreen.class);
                i.putExtra("imageitem", item);
                startActivity(i);
            }
        });

        grid.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter

        String ImageUrl = null;
        try {
            if (offset >= 8) {
                return;
            }
            if (pageArray.getJSONObject(offset) != null && pageArray.getJSONObject(offset).getString("start") != null) {
                ImageUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + searchQuery + "&rsz=8&start=" + pageArray.getJSONObject(offset).getString("start");
            } else {
                ImageUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + searchQuery + "&rsz=8&start=";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // String ImageUrl = "https://ajax.googleapis.com/ajax/services/ßßß/images?v=1.0&q=" + searchQuery + "&rsz=8";
        fetchImages(ImageUrl);
    }

    public void fetchImages(String ImageUrl) {

        if (settings.getImageType() !=null && !settings.getImageType().isEmpty()) {
            String typeQuery = "&as_filetype=" + settings.getImageType();
            ImageUrl = ImageUrl + typeQuery;
        }
        if (settings.getColour() != null && !settings.getColour().isEmpty()) {
            String colourQuery = "&imgcolor=" + settings.getColour();
            ImageUrl = ImageUrl + colourQuery;
        }
        if (settings.getSize() != null && !settings.getSize().isEmpty()) {
            String sizeQuery = "&imgsz=" + settings.getSize();
            ImageUrl = ImageUrl + sizeQuery;
        }
        if (settings.getSite() != null && !settings.getSite().isEmpty()) {
            String siteQuery = "&as_sitesearch=" + settings.getSite();
            ImageUrl = ImageUrl + siteQuery;
        }

        Log.i("ERROR", ImageUrl);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(ImageUrl,null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG,", response.toString());
                try {
                    if (response != null && response.getInt("responseStatus") == 200 ) {
                    pageArray = response.getJSONObject("responseData").getJSONObject("cursor").getJSONArray("pages");
                   // aresult.clear();
                    aresult.addAll(ImageResult.getArrayFromJson(response.getJSONObject("responseData")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent extras) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                    if (extras.getStringExtra("type")!= null && !extras.getStringExtra("type").isEmpty()) {
                        settings.setImageType(extras.getStringExtra("type"));
                    }
                    if (extras.getStringExtra("colour")!= null && !extras.getStringExtra("colour").isEmpty()) {
                        settings.setColour(extras.getStringExtra("colour"));
                    }
                    if (extras.getStringExtra("size") != null &&!extras.getStringExtra("size").isEmpty()) {
                        settings.setSize(extras.getStringExtra("size"));
                    }
                    if (extras.getStringExtra("site") != null && !extras.getStringExtra("site").isEmpty()) {
                        settings.setSite(extras.getStringExtra("site"));
                    }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.AsImageSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
              //  if (isNetworkAvailable() == false ||  isOnline() == false) {
              //      Toast.makeText(getApplicationContext(),"Network Not Available !!!",
              //              Toast.LENGTH_LONG).show();
              //      return false;
              //  }
                searchQuery = new String(query);
                String ImageUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&rsz=8";
               // String ImageUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query;
                fetchImages(ImageUrl);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putSerializable("settings", settings);
        SettingsFragment settingsFrag = SettingsFragment.newInstance(settings);
        settingsFrag.show(fm, "fragment_edit_name");
    }


    public void onFinishEditDialog(ImageSetting setting) {
        this.settings = setting;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        //    Intent i = new Intent(MainActivity.this,ImageSettings.class);
            aresult.clear();
       //     i.putExtra("settings",settings);
        //    startActivityForResult(i, 1);
            showEditDialog();


            return true;
        }

        if (id == R.id.action_share) {
            ImageView ivImage = (ImageView) findViewById(R.id.IvImage);
            // Get access to the URI for the bitmap
            Uri bmpUri = getLocalBitmapUri(ivImage);
            if (bmpUri != null) {
                // Construct a ShareIntent with link to image
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
