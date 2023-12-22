package com.example.alchemy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
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
    private ArrayList<ImageItemModel> imageItemModelArrayList = new ArrayList<ImageItemModel>();
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

        imageItemModelArrayList.add(new ImageItemModel("A00001.png"));
        imageItemModelArrayList.add(new ImageItemModel("A00002.png"));
        imageItemModelArrayList.add(new ImageItemModel("A00001.png"));

        imageItemAdapter = new ImageItemAdapter(this, imageItemModelArrayList);
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
}