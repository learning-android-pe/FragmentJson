package com.orimex.orimex.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.orimex.orimex.MapActivity;
import com.orimex.orimex.R;
import com.orimex.orimex.adapter.ImagePagerAdapter;
import com.orimex.orimex.model.Item;
import com.orimex.orimex.util.PhoneCallDialog;
import com.orimex.orimex.util.UtilMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.orimex.orimex.R.styleable.ExpandableTextView;
import static com.orimex.orimex.util.Constants.JF_DATE;
import static com.orimex.orimex.util.Constants.JF_NAME;
import static com.orimex.orimex.util.Constants.JF_RATING_ARRAY;
import static com.orimex.orimex.util.Constants.JF_REVIEW;
import static com.orimex.orimex.util.Constants.JF_USER_RATING;
import static com.orimex.orimex.util.Constants.MSG_RATING_SUCCESSFUL;
import static com.orimex.orimex.util.Constants.NO_DATA_FOUND;
import static com.orimex.orimex.util.Constants.NULL_LOCATION;
import static com.orimex.orimex.util.UtilMethods.APP_MAP_MODE;
import static com.orimex.orimex.util.UtilMethods.browseUrl;
import static com.orimex.orimex.util.UtilMethods.getPreferenceString;
import static com.orimex.orimex.util.UtilMethods.isConnectedToInternet;
import static com.orimex.orimex.util.UtilMethods.isDeviceCallSupported;
import static com.orimex.orimex.util.UtilMethods.isGpsEnable;
import static com.orimex.orimex.util.UtilMethods.isUserSignedIn;
import static com.orimex.orimex.util.UtilMethods.loadJSONFromAsset;
import static com.orimex.orimex.util.UtilMethods.mailTo;
import static com.orimex.orimex.util.UtilMethods.phoneCall;
import static com.orimex.orimex.util.UtilMethods.showNoGpsDialog;
import static com.orimex.orimex.util.UtilMethods.showNoInternetDialog;

/**
 * Created by Mi sesión on 15/11/2016.
 */

public class DetailViewFragment extends Fragment implements UtilMethods.InternetConnectionListener {


    public static Item itemDetails;
    private static AlertDialog dialog = null;
    private final int BROWSER_ACTION = 1;
    private final int MAP_ACTION = 2;
    private final int RATE_NOW_ACTION = 3;
    SimpleDateFormat appViewFormat;
    SimpleDateFormat serverFormat;
    private ViewPager imagePager;
    private ImageView prevImgView;
    private ImageView nextImgView;
    private UtilMethods.InternetConnectionListener internetConnectionListener;
    private int googlePlayServiceStatus;
    private ArrayList<Comment> commentList;
    private TextView countRatingTV;
    private TextView allRatingTV;
    private LayoutInflater inflater;
    private LinearLayout commentLayout;
    private String phoneString = null;


    public DetailViewFragment() {

    }

    public static DetailViewFragment newInstance(Item item) {
        DetailViewFragment fragment = new DetailViewFragment();
        itemDetails = item;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);
        commentLayout = (LinearLayout) rootView.findViewById(R.id.commentLayout);
        if (itemDetails != null) {
            imagePager = ((ViewPager) rootView.findViewById(R.id.detailHeadingImageViewPager));
            prevImgView = (ImageView) rootView.findViewById(R.id.prevImgView);
            nextImgView = (ImageView) rootView.findViewById(R.id.nextImgView);
            countRatingTV = (TextView) rootView.findViewById(R.id.countRatingTV);
            allRatingTV = (TextView) rootView.findViewById(R.id.allRatingTV);

            //! viewpager to show images with horizontal scrolling.
            imagePager.setAdapter(new ImagePagerAdapter(getActivity(), itemDetails.getImageLargeUrls()));

            //! hide previous and next arrow if adapter size is less then 2
            if (imagePager.getAdapter().getCount() <= 1) {
                prevImgView.setVisibility(View.INVISIBLE);
                nextImgView.setVisibility(View.INVISIBLE);
            }


            ((ViewPager) rootView.findViewById(R.id.detailHeadingImageViewPager)).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (imagePager.getAdapter().getCount() > 1) {
                        if (position == 0) {
                            nextImgView.setVisibility(View.VISIBLE);
                            prevImgView.setVisibility(View.INVISIBLE);
                        } else if (position == imagePager.getAdapter().getCount() - 1) {
                            prevImgView.setVisibility(View.VISIBLE);
                            nextImgView.setVisibility(View.INVISIBLE);
                        } else {
                            prevImgView.setVisibility(View.VISIBLE);
                            nextImgView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if (itemDetails.isVerified()) {
                rootView.findViewById(R.id.verificationImgView).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.verificationImgView).setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(itemDetails.getTitle())) {
                ((TextView) rootView.findViewById(R.id.itemNameTV)).setText(itemDetails.getTitle());
            }
            if (!TextUtils.isEmpty(itemDetails.getMobileNumber()) &&
                    !itemDetails.getMobileNumber().equals(NO_DATA_FOUND)) {
                phoneString = itemDetails.getMobileNumber();
            }
            if (!TextUtils.isEmpty(itemDetails.getTelephoneNumber()) &&
                    !itemDetails.getTelephoneNumber().equals(NO_DATA_FOUND)) {
                if (!TextUtils.isEmpty(phoneString)) {
                    phoneString += ",";
                    phoneString += itemDetails.getTelephoneNumber();
                } else {
                    phoneString = itemDetails.getTelephoneNumber();
                }
            }
            if (!TextUtils.isEmpty(itemDetails.getContactPhoneNumber()) &&
                    !itemDetails.getContactPhoneNumber().equals(NO_DATA_FOUND)) {
                if (!TextUtils.isEmpty(phoneString)) {
                    phoneString += ",";
                    phoneString += itemDetails.getContactPhoneNumber();
                } else {
                    phoneString = itemDetails.getContactPhoneNumber();
                }
            }
            if (!TextUtils.isEmpty(phoneString))
                ((TextView) rootView.findViewById(R.id.itemPhoneTV)).setText(phoneString);
            if (!TextUtils.isEmpty(itemDetails.getAddress()))
                ((TextView) rootView.findViewById(R.id.itemLocationTV)).setText(itemDetails.getAddress());
            if (!TextUtils.isEmpty(itemDetails.getTag()))
            ((TextView) rootView.findViewById(R.id.btnWeb)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemDetails.getWebUrl())) {
                        if (isConnectedToInternet(getActivity())) {
                            browseUrl(getActivity(), itemDetails.getWebUrl());
                        } else {
                            internetConnectionListener = (UtilMethods.InternetConnectionListener) DetailViewFragment.this;
                            showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
                                    getResources().getString(R.string.no_internet_text),
                                    getResources().getString(R.string.retry_string),
                                    getResources().getString(R.string.cancel_string), BROWSER_ACTION);
                        }

                    } else
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_website), Toast.LENGTH_SHORT).show();
                }
            });

            ((TextView) rootView.findViewById(R.id.btnEmail)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(itemDetails.getEmailAddress()))
                        mailTo(getActivity(), itemDetails.getTitle(), itemDetails.getEmailAddress());
                    else
                        Toast.makeText(getActivity(), getResources().getString(R.string.no_email), Toast.LENGTH_SHORT).show();

                }
            });

            ((TextView) rootView.findViewById(R.id.btnMap)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMap();
                }
            });



            ((TextView) rootView.findViewById(R.id.itemPhoneTV)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(phoneString)) {
                        if (isDeviceCallSupported(getActivity())) {
                            if (phoneString.contains(",")) {
                                PhoneCallDialog.showPhoneCallDialog(getActivity(),
                                        phoneString.split(","));
                            } else {
                                phoneCall(getActivity(), phoneString);
                            }
                        }
                    }
                }
            });

            ((TextView) rootView.findViewById(R.id.itemLocationTV)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMap();
                }
            });

        }
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.findItem(R.id.action_filter).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
        }
    }

    private void showMap() {
        if (itemDetails.getLatitude() != NULL_LOCATION && itemDetails.getLongitude() != NULL_LOCATION) {
            /** set APP_MAP_MODE to true to enable internet checking
             * because map needs internet connection
             * to  show user and business location as well as their distance
             */
            APP_MAP_MODE = true;
            if (isConnectedToInternet(getActivity())) {
                if (isGooglePlayServicesAvailable()) {
                    if (isGpsEnable(getActivity())) {
                        startActivity(new Intent(getActivity(), MapActivity.class));
                    } else {
                        showNoGpsDialog(getActivity(), getResources().getString(R.string.no_gps),
                                getResources().getString(R.string.no_gps_message),
                                getResources().getString(R.string.no_gps_positive_text),
                                getResources().getString(R.string.no_gps_negative_text));
                    }
                } else {
                    showGooglePlayServiceUnavailableDialog();
                }
            } else {
                internetConnectionListener = (UtilMethods.InternetConnectionListener) DetailViewFragment.this;
                showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.no_internet_text),
                        getResources().getString(R.string.retry_string),
                        getResources().getString(R.string.cancel_string), MAP_ACTION);
            }

        } else
            Toast.makeText(getActivity(), getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
    }





    private String dateTimeFormatter(String serverDate) {
        appViewFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a");
        serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return appViewFormat.format(serverFormat.parse(serverDate));
        } catch (ParseException e) {
            return serverDate;
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        googlePlayServiceStatus = GooglePlayServicesUtil.
                isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == googlePlayServiceStatus) {
            return true;
        } else {
            return false;
        }
    }

    private void showGooglePlayServiceUnavailableDialog() {
        GooglePlayServicesUtil.getErrorDialog(googlePlayServiceStatus, getActivity(), 0).show();
    }


    @Override
    public void onConnectionEstablished(int code) {

    }

    @Override
    public void onUserCanceled(int code) {

    }

    @Override
    public void onSuccessResponse(String tag, String jsonString) {

    }

    @Override
    public void onFailureResponse(String tag) {

    }
}
