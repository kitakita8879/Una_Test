package com.example.una_test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChargingDeviceManagementActivity extends AppCompatActivity {
    public final ArrayList<Integer> mNameData = new ArrayList<>();
    private final ArrayList<Integer> mDevIDData = new ArrayList<>();
    private final ArrayList<Boolean> mFwUpdate = new ArrayList<>();
    ;
    private final int colorBlue = Color.parseColor("#0094D6");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_device_management);

        findViewById(R.id.img_back).setOnClickListener(v -> finish());

        mNameData.add(1);
        mNameData.add(2);
        mDevIDData.add(12345678);
        mDevIDData.add(87654321);
        mFwUpdate.add(true);
        mFwUpdate.add(false);

        RecyclerView rvShow = findViewById(R.id.recycler_view_test2);
        rvShow.setLayoutManager(new LinearLayoutManager(this));
        ChargingDeviceManagementActivity.RecyclerViewAdapter rvAdapter = new
                ChargingDeviceManagementActivity.RecyclerViewAdapter(mNameData, mDevIDData, mFwUpdate);
        rvShow.setAdapter(rvAdapter);

        ChargingDeviceManagementActivity.RecyclerSpace decoration =
                new ChargingDeviceManagementActivity.RecyclerSpace(ChargingDeviceManagementActivity
                        .RecyclerSpace.convertDpToPixel(30, this));
        rvShow.addItemDecoration(decoration);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Integer> mData, mDevId;
        private final ArrayList<Boolean> mFw;
        //private ChargingDeviceManagementActivity.OnRecyclerItemClickListener mClickListener;

        public RecyclerViewAdapter(ArrayList<Integer> data, ArrayList<Integer> id,
                                   ArrayList<Boolean> fw) {
            this.mData = data;
            this.mDevId = id;
            this.mFw = fw;
        }

        //public void setOnRecyclerItemClickListener(ChargingDeviceManagementActivity.OnRecyclerItemClickListener listener) {
        //    this.mClickListener = listener;
        //}

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView txtName, txtDevID, txtVersion, txtFwUpdate;
            final ImageView imgDel, imgError;

            ViewHolder(View item) {
                super(item);
                txtName = item.findViewById(R.id.txt_name);
                txtDevID = item.findViewById(R.id.txt_dev_id);
                txtVersion = item.findViewById(R.id.txt_version);
                txtFwUpdate = item.findViewById(R.id.txt_fw_update);
                imgDel = item.findViewById(R.id.img_del);
                imgError = item.findViewById(R.id.img_error);
            }
        }

        @NonNull
        @Override
        public ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.test_2_view_item, parent, false);
            return new ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder holder, int position) {
            Integer data = mData.get(position);
            Integer id = mDevId.get(position);
            Boolean fw = mFw.get(position);
            holder.txtName.setText(String.format(getString(R.string.txt_name), data));
            holder.txtDevID.setText(String.format(getString(R.string.txt_dev_id), id));
            if (fw) {
                holder.txtFwUpdate.setText(String.format(getString(R.string.txt_fw_need_update)));
                holder.txtFwUpdate.setTextColor(colorBlue);
                holder.imgError.setVisibility(View.VISIBLE);
            } else {
                holder.txtFwUpdate.setText(String.format(getString(R.string.txt_fw_not_need_update)));
                holder.txtFwUpdate.setTextColor(Color.parseColor("#757575"));
                holder.imgError.setVisibility(View.GONE);
            }
            //holder.txtRvItem.setOnClickListener(v ->mClickListener.onRecyclerItemClick(v, holder.getAdapterPosition()));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    private static class RecyclerSpace extends RecyclerView.ItemDecoration {
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

    /*private interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);
    }*/
}
