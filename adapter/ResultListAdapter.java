package com.orimex.orimex.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orimex.orimex.R;
import com.orimex.orimex.fragment.ResultListFragment;
import com.orimex.orimex.model.Category;
import com.orimex.orimex.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.orimex.orimex.util.Constants.NO_IMAGE_FOUND;
import static com.orimex.orimex.util.UtilMethods.browseUrl;

/**
 * Created by Mi sesión on 15/11/2016.
 */

public class ResultListAdapter extends ArrayAdapter<Category> implements View.OnClickListener {

    private final LayoutInflater inflater;
    private final ArrayList<Item> searchResultList;
    private Activity activity;
    private ResultListFragment.ResultListCallbacks mCallbacks;

    public ResultListAdapter(Activity activity, ResultListFragment.ResultListCallbacks mCallbacks, ArrayList<Item> searchResultList) {
        super(activity, R.layout.layout_result_list);
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
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
        String imageUrl = (item.getImageThumbUrls().length > 0) ? item.getImageThumbUrls()[0]
                : NO_IMAGE_FOUND;
        Picasso.with(activity).load(imageUrl).placeholder(R.drawable.ic_placeholder).tag(imageUrl)
                .into(row.itemIconImgView);
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
            browseUrl(activity, searchResultList.get(position).getAdUrl());
        }
    }

    private static class ViewHolder {
        public RelativeLayout parentView;
        public ImageView itemIconImgView;
        public TextView itemTitleTV;
        public TextView itemAddressTV;
        public ImageView verificationImgView;
    }
}
