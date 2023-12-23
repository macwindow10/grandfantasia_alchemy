package com.example.alchemy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    private static String TAG = SearchItemAdapter.class.getName();
    private Context context;
    private ArrayList<ImageItemModel> arrayList;

    public SearchItemAdapter(Context context, ArrayList<ImageItemModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public SearchItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewId.setText("" + arrayList.get(position).getId());
        viewHolder.textViewIcon.setText("" + arrayList.get(position).getIcon());
        viewHolder.textViewName.setText("" + arrayList.get(position).getName());
        viewHolder.textViewValue.setText("" + arrayList.get(position).getValue());
        try {
            InputStream ims = context.getAssets().open(arrayList.get(position).getIcon());
            Drawable d = Drawable.createFromStream(ims, null);
            viewHolder.imageView.setImageDrawable(d);
        } catch (IOException ex) {
            Log.e(TAG, "Failed when ...");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewId;
        public TextView textViewIcon;
        public TextView textViewName;
        public TextView textViewValue;
        public ImageView imageView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewId = itemLayoutView.findViewById(R.id.text_view_id);
            textViewIcon = itemLayoutView.findViewById(R.id.text_view_icon);
            textViewName = itemLayoutView.findViewById(R.id.text_view_name);
            textViewValue = itemLayoutView.findViewById(R.id.text_view_value);
            imageView = itemLayoutView.findViewById(R.id.image_view);

            itemLayoutView.setLongClickable(true);
            itemLayoutView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getLayoutPosition();
                    ((MainActivity)context).addInSelectedImagesList(arrayList.get(position));
                    // Toast.makeText(view.getContext(), "Item added", Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        }
    }
}
