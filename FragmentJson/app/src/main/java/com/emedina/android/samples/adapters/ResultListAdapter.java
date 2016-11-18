package com.emedina.android.samples.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emedina.android.samples.R;
import com.emedina.android.samples.callbacks.ResultListCallback;
import com.emedina.android.samples.model.Item;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mi sesión on 15/11/2016.
 */

public class ResultListAdapter extends ArrayAdapter<Item> implements View.OnClickListener {

    private final String NO_IMAGE_FOUND= "";
    private final LayoutInflater inflater;
    private final List<Item> searchResultList;
    private ResultListCallback mCallbacks;

    public ResultListAdapter(Context context, ResultListCallback mCallbacks, List<Item> searchResultList) {
        super(context, R.layout.layout_result_list);
        this.inflater = LayoutInflater.from(context);
        this.searchResultList = searchResultList;
        this.mCallbacks = mCallbacks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder row;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_result_list, null);
            row = new ViewHolder();
            row.parentView = (RelativeLayout) convertView.findViewById(R.id.parentView);
            row.itemIconImgView = (ImageView) convertView.findViewById(R.id.itemIconImgView);
            row.itemTitleTV = (TextView) convertView.findViewById(R.id.itemTitleTV);
            row.itemAddressTV = (TextView) convertView.findViewById(R.id.itemAddressTV);
            row.verificationImgView = (ImageView) convertView.findViewById(R.id.verificationImgView);
            row.parentView.setOnClickListener(this);
            convertView.setTag(row);
        } else {
            row = (ViewHolder) convertView.getTag();
        }
        Item item = searchResultList.get(position);
        String[] imagesUrl= item.getImageThumbUrls();
        String imageUrl="";
        if(imagesUrl!=null) imageUrl = (imagesUrl.length > 0) ? imagesUrl[0]: NO_IMAGE_FOUND;

        /*Picasso.with(activity).load(imageUrl).placeholder(R.drawable.ic_placeholder).tag(imageUrl)
                .into(row.itemIconImgView);*/
        row.itemTitleTV.setText(item.getTitle());
        row.itemAddressTV.setText(item.getAddress());
        if (item.isVerified()) {
            row.verificationImgView.setVisibility(View.VISIBLE);
        } else {
            row.verificationImgView.setVisibility(View.INVISIBLE);
        }
        row.parentView.setTag(position);
        return convertView;
    }

    @Override
    public int getCount() {
        return searchResultList.size();
    }

    @Override
    public void onClick(View v) {
        int position = Integer.parseInt(v.getTag().toString());
        if (v.getId() == R.id.parentView) {
            mCallbacks.onResultItemSelected(searchResultList.get(position));
        } else {
            //browseUrl(activity, searchResultList.get(position).getAdUrl());
            browseUrl(searchResultList.get(position).getAdUrl());
        }
    }

    private void browseUrl(String url){
        mCallbacks.onGotoBrowser(url);
    }
    private static class ViewHolder {
        public RelativeLayout parentView;
        public ImageView itemIconImgView;
        public TextView itemTitleTV;
        public TextView itemAddressTV;
        public ImageView verificationImgView;
    }
}
