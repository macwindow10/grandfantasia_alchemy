package com.example.alchemy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private TextView textViewSelectFile;
    private Button buttonSelectFile;
    private EditText editTextSearchByName;
    private EditText editTextSearchById;
    private RecyclerView recyclerView;
    private GridView gridView;
    private ArrayList<ImageItemModel> listSelectedImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listAllImages = new ArrayList<ImageItemModel>();
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

        populateAllImagesList();

        searchItemAdapter = new SearchItemAdapter(this, listAllImages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchItemAdapter);

        imageItemAdapter = new ImageItemAdapter(this, listSelectedImages);
        gridView.setAdapter(imageItemAdapter);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateAllImagesList() {
        try {
            ImageItemModel imageItemModel;

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
            
        } catch (Exception exception) {

        }
    }
}