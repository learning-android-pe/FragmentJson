package com.emedina.android.samples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.emedina.android.samples.adapters.ResultListAdapter;
import com.emedina.android.samples.callbacks.ResultListCallback;
import com.emedina.android.samples.model.Category;
import com.emedina.android.samples.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ResultListCallback {

    private List<Item> items;
    private ListView lviItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app();
    }

    private void app() {
        ui();
        mock();
        populateItems();
    }

    private void populateItems() {
        ResultListAdapter resultListAdapter= new ResultListAdapter(this,this,items);
        lviItems.setAdapter(resultListAdapter);
    }

    private void ui() {
        lviItems= (ListView) findViewById(R.id.lviItems);
    }

    private void mock(){
        items= new ArrayList<>();
        Item item1= new Item("1", "Item 1 ", "Lima Perú");
        item1.setVerification(true);
        Item item2= new Item("2", "Item 2 ", "Lima Perú");
        Item item3= new Item("3", "Item 3 ", "Lima Perú");
        Item item4= new Item("4", "Item 4 ", "Lima Perú");
        Item item5= new Item("5", "Item 5 ", "Lima Perú");
        item5.setVerification(true);
        Item item6= new Item("6", "Item 6 ", "Lima Perú");
        item6.setVerification(true);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
    }

    private void showMessage(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResultItemSelected(Item item) {
        Item mItem= item;
        String message= (mItem==null)?("Item no existe"):(item.toString());
        showMessage(message);
    }


    @Override
    public void onGotoBrowser(String url) {
        showMessage("gotobrowser url "+url);
    }
}
