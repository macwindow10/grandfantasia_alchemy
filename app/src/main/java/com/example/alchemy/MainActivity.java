package com.example.alchemy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private TextView textViewSelectFile;
    private Button buttonSelectFile;
    private ProgressBar progressBar;
    private EditText editTextSearchByName;
    private EditText editTextSearchById;
    private Button buttonSelectImagesRandomly;
    private Button buttonSave;
    private Button buttonReset;
    private RecyclerView recyclerView;
    private GridView gridView;
    private ArrayList<ImageItemModel> listAllImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listSelectedImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listOfSearchResult = new ArrayList<ImageItemModel>();
    private SearchItemAdapter searchItemAdapter;
    private ImageItemAdapter imageItemAdapter;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewSelectFile = findViewById(R.id.text_view_file);
        buttonSelectFile = findViewById(R.id.button_select_file);
        progressBar = findViewById(R.id.progress_bar);
        editTextSearchByName = findViewById(R.id.edit_text_search_by_name);
        editTextSearchById = findViewById(R.id.edit_text_search_by_id);
        buttonSelectImagesRandomly = findViewById(R.id.button_select_randomly);
        buttonSave = findViewById(R.id.button_save);
        buttonReset = findViewById(R.id.button_reset);
        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        textViewSelectFile.setText(path + "/Local DB TW.txt");

        buttonSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectFile.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(10);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        populateLists();
                    }
                });
                thread.start();
            }
        });

        buttonSelectImagesRandomly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean filtered = (editTextSearchByName.getText().toString().length() > 0 ||
                        editTextSearchById.getText().toString().length() > 0);
                if (filtered && listOfSearchResult.size() < 40) {
                    Toast.makeText(MainActivity.this,
                            "Not enough items in search result",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                ImageItemModel imageItemModel;
                int min;
                int max;
                int random;
                int i;
                if (filtered) {
                    min = 0;
                    max = listOfSearchResult.size() - 1;
                    currentPosition = 0;
                    for (i = 0; i < 40; i++) {
                        random = new Random().nextInt((max - min) + 1) + min;
                        imageItemModel = listOfSearchResult.get(random);
                        listSelectedImages.set(currentPosition, imageItemModel);
                        currentPosition++;
                    }
                } else {
                    min = 0;
                    max = listAllImages.size() - 1;
                    currentPosition = 0;
                    for (i = 0; i < 40; i++) {
                        random = new Random().nextInt((max - min) + 1) + min;
                        imageItemModel = listAllImages.get(random);
                        listSelectedImages.set(currentPosition, imageItemModel);
                        currentPosition++;
                    }
                }

                imageItemAdapter.notifyDataSetChanged();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPosition < 40) {
                    Toast.makeText(MainActivity.this,
                            "Not enough items in grid",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                ImageItemModel imageItemModel = null;
                FileOutputStream fileOutputStream = null;
                OutputStreamWriter outputStreamWriter = null;
                String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                SimpleDateFormat sdf;
                try {
                    sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
                    String dt = sdf.format(new Date());
                    String filename = String.format("output_%s.txt", dt);
                    fileOutputStream = new FileOutputStream(new File(path, filename));
                    outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    String s = "INSERT INTO high_lottery (lottery_id, item_index, \"week\", \"round\", item_id, item_amount, probability, num_replay, bulletin, probability_plus1, probability_plus2, probability_plus3, highlight, jackpot) VALUES";
                    outputStreamWriter.write(s + "\n");
                    int i;
                    int itemIndex = 1;
                    int week = 3;
                    int round = 1;
                    for (i = 0; i < listSelectedImages.size(); i++) {
                        imageItemModel = listSelectedImages.get(i);
                        s = String.format("(40362, %d, %d, %d, %s, %d, 1, -1, 0.94, 0.70, 0.78, 0, 0),",
                                itemIndex, week, round, imageItemModel.getId(),
                                imageItemModel.getValue());
                        outputStreamWriter.write(s + "\n");

                        if (((i + 1) % 8) == 0) {
                            round++;
                        }

                        itemIndex++;
                        if (itemIndex == 9) {
                            itemIndex = 1;
                        }
                    }
                    Toast.makeText(MainActivity.this, "Output file saved in Downloads", Toast.LENGTH_LONG).show();
                } catch (Exception exception) {
                    Log.e(TAG, "savefile::", exception);
                } finally {
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (IOException e) {
                            Log.e(TAG, "savefile::", e);
                        }
                    }
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageItemModel imageItemModel;

                editTextSearchByName.setText("");
                editTextSearchById.setText("");
                currentPosition = 0;
                listOfSearchResult.clear();
                listSelectedImages.clear();
                for (int i = 0; i < 40; i++) {
                    imageItemModel = new ImageItemModel("");
                    imageItemModel.setId("");
                    imageItemModel.setName("");
                    imageItemModel.setValue(0);
                    listSelectedImages.add(imageItemModel);
                }
                searchItemAdapter = new SearchItemAdapter(MainActivity.this, listOfSearchResult);
                recyclerView.setAdapter(searchItemAdapter);

                imageItemAdapter = new ImageItemAdapter(MainActivity.this, listSelectedImages);
                gridView.setAdapter(imageItemAdapter);
            }
        });

        searchItemAdapter = new SearchItemAdapter(this, listOfSearchResult);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchItemAdapter);
        if (listOfSearchResult.size() == 0) {
            recyclerView.setVisibility(View.GONE);
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
                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                Log.i(TAG, s);
                List<ImageItemModel> list = listAllImages
                        .stream()
                        .filter(c -> c.getName().toLowerCase().contains(s.toLowerCase()))
                        .collect(Collectors.toList());
                Log.i(TAG, list.size() + "");
                listOfSearchResult = new ArrayList<ImageItemModel>(list);
                if (listOfSearchResult.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                searchItemAdapter = new SearchItemAdapter(MainActivity.this, listOfSearchResult);
                recyclerView.setAdapter(searchItemAdapter);
            }
        });

        editTextSearchById.addTextChangedListener(new TextWatcher() {
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
                    recyclerView.setVisibility(View.GONE);
                    return;
                }

                List<ImageItemModel> list = listAllImages
                        .stream()
                        .filter(c -> c.getId().toLowerCase().contains(s.toLowerCase()))
                        .collect(Collectors.toList());
                listOfSearchResult = new ArrayList<ImageItemModel>(list);
                if (listOfSearchResult.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                searchItemAdapter = new SearchItemAdapter(MainActivity.this, listOfSearchResult);
                recyclerView.setAdapter(searchItemAdapter);
            }
        });
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

            BufferedReader r = null;
            try {
                String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                Log.i(TAG, "reading file::path::" + path);
                r = new BufferedReader(new FileReader(path + "/Local DB TW.txt"));
                Log.i(TAG, "reading file::start");
                int i = 1;
                String[] values;
                String line;
                while ((line = r.readLine()) != null) {
                    // Log.i(TAG, line);
                    values = line.split("\\|");
                    // Log.i(TAG, String.valueOf(values.length));
                    if (values.length != 4) {
                        continue;
                    }
                    imageItemModel = new ImageItemModel(values[1].trim());
                    imageItemModel.setId(values[0].trim());
                    imageItemModel.setName(values[2].trim());
                    imageItemModel.setValue(Integer.parseInt(values[3].trim()));
                    listAllImages.add(imageItemModel);

                    // Log.i(TAG, "reading file::record number" + String.valueOf(i));
                    i++;
                }
            } catch (Exception exception) {
                Log.e(TAG, "reading file::" + exception.getMessage());
            } finally {
                if (r != null) {
                    r.close();
                }
            }
            Log.i(TAG, "reading file::finished");

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonSelectFile.setEnabled(true);
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                    imageItemAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception exception) {
            Log.e(TAG, "populateLists", exception);
        }
    }

    public void addInSelectedImagesList(ImageItemModel imageItemModel) {
        if (currentPosition < 40) {
            listSelectedImages.set(currentPosition, imageItemModel);
            imageItemAdapter.notifyDataSetChanged();
            currentPosition++;
            Toast.makeText(this, "Item added", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Grid is full", Toast.LENGTH_LONG).show();
        }
    }
}