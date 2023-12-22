package com.example.alchemy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ImageItemAdapter extends ArrayAdapter<ImageItemModel> {

    private Context context;

    public ImageItemAdapter(@NonNull Context context,
                            ArrayList<ImageItemModel> arrayList) {
        super(context, 0, arrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false);
        }

        ImageItemModel imageItemModel = getItem(position);
        Button button = listItemView.findViewById(R.id.button_select);
        ImageView imageView = listItemView.findViewById(R.id.image_view);
        TextView textView = listItemView.findViewById(R.id.text_view);

        try {
            InputStream ims = context.getAssets().open(imageItemModel.getName());
            Drawable d = Drawable.createFromStream(ims, null);
            imageView.setImageDrawable(d);
        } catch (IOException ex) {
            Log.e("I/O ERROR", "Failed when ...");
        }
        textView.setText("Q: " + imageItemModel.getQuantity());

        return listItemView;
    }
}
