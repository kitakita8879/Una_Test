package com.example.una_test;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        // TODO: 2024/4/15 adapter 相關用法
        ListView lvShow = findViewById(R.id.list_view);
        ArrayAdapter<Integer> listAdapter = new ArrayAdapter<>(this, R.layout.test_1_view_item, mData);
        lvShow.setAdapter(listAdapter);

        RecyclerView rvShow = findViewById(R.id.recycler_view);
        rvShow.setLayoutManager(new LinearLayoutManager(this));
        rvShow.setAdapter(new RecyclerViewAdapter(mData));

        RecyclerSpace decoration = new RecyclerSpace((int) RecyclerSpace.convertDpToPixel(15, this));
        rvShow.addItemDecoration(decoration);
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

    // TODO: 2024/4/15 LayoutInflater 相關用法
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_1_view_item, parent, false);
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

    // TODO: 2024/4/15 dp px 互轉用法 
    public static float convertDpToPixel(int dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}

