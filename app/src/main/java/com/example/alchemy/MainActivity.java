package com.example.alchemy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private TextView textViewSelectFile;
    private Button buttonSelectFile;
    private EditText editTextSearchByName;
    private EditText editTextSearchById;
    private RecyclerView recyclerView;
    private GridView gridView;
    private ArrayList<ImageItemModel> listAllImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listSelectedImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listOfSearchResult = new ArrayList<ImageItemModel>();
    private SearchItemAdapter searchItemAdapter;
    private ImageItemAdapter imageItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewSelectFile = findViewById(R.id.text_view_file);
        buttonSelectFile = findViewById(R.id.button_select_file);
        editTextSearchByName = findViewById(R.id.edit_text_search_by_name);
        editTextSearchById = findViewById(R.id.edit_text_search_by_id);
        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        populateLists();

        searchItemAdapter = new SearchItemAdapter(this, listOfSearchResult);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchItemAdapter);
        if (listOfSearchResult.size() == 0) {
            //recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        imageItemAdapter = new ImageItemAdapter(this, listSelectedImages);
        gridView.setAdapter(imageItemAdapter);

        editTextSearchByName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.length() < 3) {
                    //recyclerView.setVisibility(View.GONE);
                    return;
                }

                Log.i("MainActivity", s);
                List<ImageItemModel> list = listAllImages
                        .stream()
                        .filter(c -> c.getName().contains(s))
                        .collect(Collectors.toList());
                Log.i("MainActivity", list.size() + "");
                listOfSearchResult = new ArrayList<ImageItemModel>(list);
                if (listOfSearchResult.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                searchItemAdapter.notifyDataSetChanged();
            }
        });
        /*
        ImageView imageView = findViewById(R.id.image_view_1);
        ImageView imageView2 = findViewById(R.id.image_view_2);
        try {
            InputStream ims = getAssets().open("A00001.png");
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
            ims = getAssets().open("A00002.png");
            d = Drawable.createFromStream(ims, null);
            imageView2.setImageDrawable(d);
        } catch (IOException ex) {
            Log.e("I/O ERROR", "Failed when ...");
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateLists() {
        ImageItemModel imageItemModel;
        try {

            for (int i = 0; i < 40; i++) {
                imageItemModel = new ImageItemModel("");
                imageItemModel.setId("");
                imageItemModel.setName("");
                imageItemModel.setValue(0);
                listSelectedImages.add(imageItemModel);
            }

            imageItemModel = new ImageItemModel("A00001.png");
            imageItemModel.setId("10266");
            imageItemModel.setName("Big Boss Winn's Request Reward I");
            imageItemModel.setValue(8);
            listAllImages.add(imageItemModel);

            imageItemModel = new ImageItemModel("A00001.png");
            imageItemModel.setId("10267");
            imageItemModel.setName("Big Boss Winn's Request Reward II");
            imageItemModel.setValue(8);
            listAllImages.add(imageItemModel);

            imageItemModel = new ImageItemModel("A00001.png");
            imageItemModel.setId("10268");
            imageItemModel.setName("Big Boss Winn's Request Reward III");
            imageItemModel.setValue(8);
            listAllImages.add(imageItemModel);

            imageItemModel = new ImageItemModel("A00001.png");
            imageItemModel.setId("10269");
            imageItemModel.setName("Big Boss Winn's Request Reward IV");
            imageItemModel.setValue(8);
            listAllImages.add(imageItemModel);


            List<ImageItemModel> list = listAllImages
                    .stream()
                    .filter(c -> c.getName().contains("Big"))
                    .collect(Collectors.toList());
            listOfSearchResult = new ArrayList<ImageItemModel>(list);
            searchItemAdapter.notifyDataSetChanged();

        } catch (Exception exception) {

        }
    }
}