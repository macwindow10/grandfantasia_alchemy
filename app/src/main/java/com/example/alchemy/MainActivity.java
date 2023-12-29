package com.example.alchemy;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private int PERMISSION_REQUEST_CODE = 10001;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private TextView textViewSelectFile;
    private Button buttonSelectFile;
    private ProgressBar progressBar;
    private EditText editTextSearchByName;
    private EditText editTextSearchById;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    private CheckBox checkBox6, checkBox7, checkBox8, checkBox9, checkBox10;
    private CheckBox checkBox11, checkBox12, checkBox13, checkBox14, checkBox15;
    private TextView textViewSearchResultImagesCount;
    private Button buttonSelectImagesRandomly;
    private Button buttonSave;
    private Button buttonReset;
    private RecyclerView recyclerView;
    private GridView gridView;
    private ArrayList<ImageItemModel> listAllImages = new ArrayList<ImageItemModel>();
    private ArrayList<ImageItemModel> listFilteredOnLastColumnImages = new ArrayList<ImageItemModel>();
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
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        checkBox4 = findViewById(R.id.checkbox4);
        checkBox5 = findViewById(R.id.checkbox5);
        checkBox6 = findViewById(R.id.checkbox6);
        checkBox7 = findViewById(R.id.checkbox7);
        checkBox8 = findViewById(R.id.checkbox8);
        checkBox9 = findViewById(R.id.checkbox9);
        checkBox10 = findViewById(R.id.checkbox10);
        checkBox11 = findViewById(R.id.checkbox11);
        checkBox12 = findViewById(R.id.checkbox12);
        checkBox13 = findViewById(R.id.checkbox13);
        checkBox14 = findViewById(R.id.checkbox14);
        checkBox15 = findViewById(R.id.checkbox15);
        textViewSearchResultImagesCount = findViewById(R.id.text_view_search_result_images_count);
        buttonSelectImagesRandomly = findViewById(R.id.button_select_randomly);
        buttonSave = findViewById(R.id.button_save);
        buttonReset = findViewById(R.id.button_reset);
        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
        textViewSelectFile.setText(path + "/Local DB TW.txt");

        checkBox1.setOnCheckedChangeListener(OnCheckChanged);
        checkBox2.setOnCheckedChangeListener(OnCheckChanged);
        checkBox3.setOnCheckedChangeListener(OnCheckChanged);
        checkBox4.setOnCheckedChangeListener(OnCheckChanged);
        checkBox5.setOnCheckedChangeListener(OnCheckChanged);
        checkBox6.setOnCheckedChangeListener(OnCheckChanged);
        checkBox7.setOnCheckedChangeListener(OnCheckChanged);
        checkBox8.setOnCheckedChangeListener(OnCheckChanged);
        checkBox9.setOnCheckedChangeListener(OnCheckChanged);
        checkBox10.setOnCheckedChangeListener(OnCheckChanged);
        checkBox11.setOnCheckedChangeListener(OnCheckChanged);
        checkBox12.setOnCheckedChangeListener(OnCheckChanged);
        checkBox13.setOnCheckedChangeListener(OnCheckChanged);
        checkBox14.setOnCheckedChangeListener(OnCheckChanged);
        checkBox15.setOnCheckedChangeListener(OnCheckChanged);

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
                if (listAllImages.size() == 0) {
                    Toast.makeText(MainActivity.this, "Please load file", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean filtered =
                        (editTextSearchByName.getText().toString().length() > 0 ||
                                editTextSearchById.getText().toString().length() > 0);
                if (listOfSearchResult.size() < 40) {
                    Toast.makeText(MainActivity.this, "Not enough items in search result", Toast.LENGTH_LONG).show();
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

                    populateQuantityInSelectedImages();

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
                    int probability = 12;
                    int num_replay = 1;
                    int bulletin = 0;
                    for (i = 0; i < listSelectedImages.size(); i++) {
                        imageItemModel = listSelectedImages.get(i);
                        s = String.format("(40362, %d, %d, %d, %s, %d, 12, 1, %d, 0.94, 0.70, 0.78, 0, 0),",
                                itemIndex, week, round, imageItemModel.getId(),
                                imageItemModel.getQuantity(), bulletin);
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
                if (s.length() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                searchByName(s);
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
                if (s.length() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
                searchById(s);
            }
        });

        if (!checkPermission()) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            } else {
                // below android 11
                startActivity(new Intent(this, MainActivity.class));
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
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

    private void enableControls() {
        editTextSearchByName.setEnabled(true);
        editTextSearchById.setEnabled(true);
        checkBox1.setEnabled(true);
        checkBox2.setEnabled(true);
        checkBox3.setEnabled(true);
        checkBox4.setEnabled(true);
        checkBox5.setEnabled(true);
        checkBox6.setEnabled(true);
        checkBox7.setEnabled(true);
        checkBox8.setEnabled(true);
        checkBox9.setEnabled(true);
        checkBox10.setEnabled(true);
        checkBox11.setEnabled(true);
        checkBox12.setEnabled(true);
        checkBox13.setEnabled(true);
        checkBox14.setEnabled(true);
        checkBox15.setEnabled(true);
        buttonSelectImagesRandomly.setEnabled(true);
        buttonSave.setEnabled(true);
        buttonReset.setEnabled(true);
    }

    private CompoundButton.OnCheckedChangeListener OnCheckChanged = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            listFilteredOnLastColumnImages.clear();
            listFilteredOnLastColumnImages.addAll(listAllImages);
            List<ImageItemModel> list = new ArrayList<>();
            if (compoundButton.getId() == R.id.checkbox1 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 1)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox2 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 2)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox3 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 3)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox4 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 4)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox5 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 5)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox6 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 6)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox7 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 7)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox8 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 8)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox9 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 9)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox10 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 10)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox11 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 11)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox12 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 12)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox13 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 13)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox14 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 14)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }
            if (compoundButton.getId() == R.id.checkbox15 && b) {
                list = listFilteredOnLastColumnImages
                        .stream()
                        .filter(c -> c.getValue() == 15)
                        .collect(Collectors.toList());
                listFilteredOnLastColumnImages = new ArrayList<>(list);
            }

            if (editTextSearchByName.getText().toString().length() > 0) {
                searchByName(editTextSearchByName.getText().toString());
            } else if (editTextSearchById.getText().toString().length() > 0) {
                searchById(editTextSearchById.getText().toString());
            }
        }
    };

    private void searchByName(String s) {
        List<ImageItemModel> list = listFilteredOnLastColumnImages
                .stream()
                .filter(c -> c.getName().toLowerCase().contains(s.toLowerCase()))
                .collect(Collectors.toList());
        listOfSearchResult = new ArrayList<ImageItemModel>(list);
        if (listOfSearchResult.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        searchItemAdapter = new SearchItemAdapter(MainActivity.this, listOfSearchResult);
        recyclerView.setAdapter(searchItemAdapter);

        textViewSearchResultImagesCount.setText("Search result images count: " + listOfSearchResult.size());
    }

    private void searchById(String s) {
        List<ImageItemModel> list = listFilteredOnLastColumnImages
                .stream()
                .filter(c -> c.getId().toLowerCase().contains(s.toLowerCase()))
                .collect(Collectors.toList());
        listOfSearchResult = new ArrayList<ImageItemModel>(list);
        if (listOfSearchResult.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
        }
        searchItemAdapter = new SearchItemAdapter(MainActivity.this, listOfSearchResult);
        recyclerView.setAdapter(searchItemAdapter);

        textViewSearchResultImagesCount.setText("Search result images count: " + listOfSearchResult.size());
    }

    private void populateLists() {
        ImageItemModel imageItemModel;
        try {
            listSelectedImages.clear();
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
            // clone list
            listFilteredOnLastColumnImages.addAll(listAllImages);
            Log.i(TAG, "reading file::finished");

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    enableControls();

                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.GONE);
                    imageItemAdapter.notifyDataSetChanged();
                }
            });

        } catch (Exception exception) {
            Log.e(TAG, "populateLists", exception);
        }
    }

    private void populateQuantityInSelectedImages() {
        try {
            int x = gridView.getChildCount();
            for (int i = 0; i < x; ++i) {
                View holder = gridView.getChildAt(i);
                EditText editText = holder.findViewById(R.id.edit_view_quantity);
                try {
                    listSelectedImages.get(i).setQuantity(Integer.parseInt(editText.getText().toString()));
                } catch (Exception exception2) {

                }
            }
        } catch (Exception exception) {
            Log.e(TAG, "populateQuantityInSelectedImages::", exception);
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