package com.example.una_test;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class ChargingDeviceManagementActivity extends AppCompatActivity {
    private final ArrayList<Data> mData = new ArrayList<>();
    private ImageView imgCheck1, imgCheck2;
    private Dialog mDialogRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_device_management);

        mData.add(new Data(1, 12345678, true));
        mData.add(new Data(2, 87654321, false));

        final Boolean[] isRemoveData = {false};
        findViewById(R.id.img_back).setOnClickListener(v -> {
            if (isRemoveData[0]) {
                Intent intent = new Intent();
                intent.putExtra("name", mData.get(0).name);
                setResult(RESULT_OK, intent);
            }
            finish();
        });

        mDialogRemove = new Dialog(ChargingDeviceManagementActivity.this, R.style.dialogRemove);
        View mViewDialog = View.inflate(ChargingDeviceManagementActivity.this,
                R.layout.test_2_remove_dialog, null);
        mDialogRemove.setContentView(mViewDialog);
        imgCheck1 = mViewDialog.findViewById(R.id.img_check_1);
        imgCheck2 = mViewDialog.findViewById(R.id.img_check_2);
        TextView txtCancel = mViewDialog.findViewById(R.id.txt_cancel);
        TextView txtRemove = mViewDialog.findViewById(R.id.txt_remove);

        RecyclerView rvShow = findViewById(R.id.recycler_view_test2);
        rvShow.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(mData);
        rvAdapter.setOnRecyclerItemClickListener((view, position) -> {
            checkRemoveMode(true);
            txtRemove.setOnClickListener(v -> {
                mData.remove(position);
                rvAdapter.notifyItemRemoved(position);
                mDialogRemove.dismiss();
                isRemoveData[0] = true;
            });
        });
        rvShow.setAdapter(rvAdapter);

        ChargingDeviceManagementActivity.RecyclerSpace decoration = new
                ChargingDeviceManagementActivity.RecyclerSpace(convertDpToPixel(30, this));
        rvShow.addItemDecoration(decoration);

        txtCancel.setOnClickListener(v -> mDialogRemove.dismiss());
        imgCheck1.setOnClickListener(v -> checkRemoveMode(true));
        imgCheck2.setOnClickListener(v -> checkRemoveMode(false));
    }

    private void checkRemoveMode(boolean checkMode) {
        if (checkMode) {
            imgCheck1.setBackgroundResource(R.drawable.check_circle);
            imgCheck2.setBackgroundResource(R.drawable.test_2_check_circle);
        } else {
            imgCheck1.setBackgroundResource(R.drawable.test_2_check_circle);
            imgCheck2.setBackgroundResource(R.drawable.check_circle);
        }
    }

    private static class Data {
        int name, devId;
        boolean fwUpdate;

        Data(int name, int devId, boolean fwUpdate) {
            this.name = name;
            this.devId = devId;
            this.fwUpdate = fwUpdate;
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ChargingDeviceManagementActivity
            .RecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Data> mDataList;
        private ChargingDeviceManagementActivity.OnRecyclerItemClickListener mClickListener;

        public RecyclerViewAdapter(ArrayList<Data> dataList) {
            this.mDataList = dataList;
        }

        public void setOnRecyclerItemClickListener(ChargingDeviceManagementActivity
                                                           .OnRecyclerItemClickListener listener) {
            this.mClickListener = listener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView txtName, txtDevID, txtVersion, txtFwUpdate;
            final ImageView imgDel, imgError;
            final View viewItem;

            ViewHolder(View item) {
                super(item);
                txtName = item.findViewById(R.id.txt_name);
                txtDevID = item.findViewById(R.id.txt_dev_id);
                txtVersion = item.findViewById(R.id.txt_version);
                txtFwUpdate = item.findViewById(R.id.txt_fw_update);
                imgDel = item.findViewById(R.id.img_del);
                imgError = item.findViewById(R.id.img_error);
                viewItem = item.findViewById(R.id.view_item);
            }
        }

        @NonNull
        @Override
        public ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R
                    .layout.test_2_view_item, parent, false);
            return new ChargingDeviceManagementActivity.RecyclerViewAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ChargingDeviceManagementActivity.RecyclerViewAdapter
                                             .ViewHolder holder, int position) {
            int data = mDataList.get(position).name;
            int id = mDataList.get(position).devId;
            boolean fw = mDataList.get(position).fwUpdate;
            holder.txtName.setText(String.format(getString(R.string.txt_name), data));
            holder.txtDevID.setText(String.format(getString(R.string.txt_dev_id), id));
            if (fw) {
                holder.txtFwUpdate.setText(String.format(getString(R.string.txt_fw_need_update)));
                holder.txtFwUpdate.setTextColor(Color.parseColor("#0094D6"));
                holder.imgError.setVisibility(View.VISIBLE);
            } else {
                holder.txtFwUpdate.setText(String.format(getString(R.string.txt_fw_not_need_update)));
                holder.txtFwUpdate.setTextColor(Color.parseColor("#757575"));
                holder.imgError.setVisibility(View.GONE);
            }
            holder.imgDel.setOnClickListener(v -> {
                mClickListener.onRecyclerItemClick(v, holder.getAdapterPosition());
                mDialogRemove.show();
                Objects.requireNonNull(mDialogRemove.getWindow()).setLayout(
                        convertDpToPixel(300, ChargingDeviceManagementActivity.this),
                        WindowManager.LayoutParams.WRAP_CONTENT);
            });
            holder.viewItem.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("name", mDataList.get(holder.getAdapterPosition()).name);
                setResult(RESULT_OK, intent);
                finish();
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
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
    }

    private static int convertDpToPixel(int dp, Context context) {
        float pixel = dp * ((float) context.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
        return (int) pixel;
    }

    private interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);
    }
}
