package com.codepath.imagesearch.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by abgandhi on 2/28/15.
 */
public class ImageResult implements Serializable {
    public String title;
    public String tburl;
    public String fullurl;
    public int start;

    public ImageResult(JSONObject ImageResultItem, int pstart) {

        try {
            title = ImageResultItem.getString("title");
            tburl = ImageResultItem.getString("tbUrl");
            fullurl = ImageResultItem.getString("url");
            start = pstart;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> getArrayFromJson(JSONObject ImageResult) {
        ArrayList<ImageResult> ImageresultArray = new ArrayList<ImageResult>();
        try {
            JSONArray ImageArray = ImageResult.getJSONArray("results");
            JSONArray pagesArray =  ImageResult.getJSONObject("cursor").getJSONArray("pages");
            Log.i("DEBUG","length" + ImageArray.length());
            for (int i = 0 ;i < ImageArray.length(); i++) {
                ImageresultArray.add(new ImageResult(ImageArray.getJSONObject(i), pagesArray.getJSONObject(1).getInt("start")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ImageresultArray;
    }
}
