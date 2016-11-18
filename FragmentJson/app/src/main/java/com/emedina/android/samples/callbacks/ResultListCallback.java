package com.emedina.android.samples.callbacks;

import com.emedina.android.samples.model.Item;

/**
 * Created by eduardo on 18/11/16.
 */
public interface ResultListCallback {

    void onResultItemSelected(Item item);

    void onGotoBrowser(String url);
}
