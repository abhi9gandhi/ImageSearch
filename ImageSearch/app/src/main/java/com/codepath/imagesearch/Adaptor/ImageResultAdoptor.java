package com.codepath.imagesearch.Adaptor;

import android.content.Context;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.imagesearch.Model.ImageResult;
import com.codepath.imagesearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abgandhi on 2/28/15.
 */
public class ImageResultAdoptor extends ArrayAdapter<ImageResult> {
    public ImageResultAdoptor(Context context, List<ImageResult> objects) {
        super(context, R.layout.image_result_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_result_item, parent, false);
        }

        TextView title = (TextView)convertView.findViewById(R.id.Tvtitle);
        ImageView thub = (ImageView) convertView.findViewById(R.id.IvImage);
        title.setText(Html.fromHtml(item.title));
        thub.setImageResource(0);
        Picasso.with(getContext()).load(item.tburl).into(thub);

        return convertView;
    }
}
