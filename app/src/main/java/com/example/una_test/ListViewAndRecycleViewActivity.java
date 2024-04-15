package com.example.una_test;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListViewAndRecycleViewActivity extends AppCompatActivity {
    private final ArrayList<Integer> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_recycleview);

        for (int i = 1; i <= 100; i++) {
            mData.add(i);
        }

        //ListView
        ListView lvShow = findViewById(R.id.list_view);
        lvShow.setAdapter(new ListViewAdapter(mData));
        lvShow.addFooterView(new View(this));

        //RecyclerView
        RecyclerView rvShow = findViewById(R.id.recycler_view);
        rvShow.setLayoutManager(new LinearLayoutManager(this));
        rvShow.setAdapter(new RecyclerViewAdapter(mData));

        RecyclerSpace decoration = new RecyclerSpace(RecyclerSpace
                .convertDpToPixel(15, this));
        rvShow.addItemDecoration(decoration);
    }

}

class ListViewAdapter extends BaseAdapter {
    private final ArrayList<Integer> mData;

    public ListViewAdapter(ArrayList<Integer> data) {
        this.mData = data;
    }

    static class ViewHolder {
        TextView txtLvItem;

        ViewHolder(View item) {
            txtLvItem = item.findViewById(R.id.list_view_item);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_1_view_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Integer data = mData.get(position);
        holder.txtLvItem.setText(String.valueOf(data));

        return convertView;
    }

}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final ArrayList<Integer> mData;

    public RecyclerViewAdapter(ArrayList<Integer> data) {
        this.mData = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtRvItem;

        ViewHolder(View item) {
            super(item);
            txtRvItem = item.findViewById(R.id.list_view_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_1_view_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer data = mData.get(position);
        holder.txtRvItem.setText(String.valueOf(data));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


}

class RecyclerSpace extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public RecyclerSpace(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = mSpace;
    }

    public static int convertDpToPixel(int dp, Context context) {
        float pixel;
        pixel = dp * ((float) context.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
        return (int) pixel;
    }

}

