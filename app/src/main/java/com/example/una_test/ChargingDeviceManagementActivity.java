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
    public final ArrayList<Integer> mNameData = new ArrayList<>();
    private final ArrayList<Integer> mDevIDData = new ArrayList<>();
    private final ArrayList<Boolean> mFwUpdate = new ArrayList<>();
    private ImageView imgCheck1, imgCheck2;
    private Dialog mDialogRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_device_management);

        final Boolean[] isRemoveData = {false};
        findViewById(R.id.img_back).setOnClickListener(v -> {
            if (isRemoveData[0]) {
                Intent intent = new Intent();
                intent.putExtra("name", mNameData.get(0) - 1);
                setResult(RESULT_OK, intent);
            }
            finish();
        });

        mNameData.add(1);
        mNameData.add(2);
        mDevIDData.add(12345678);
        mDevIDData.add(87654321);
        mFwUpdate.add(true);
        mFwUpdate.add(false);

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
        ChargingDeviceManagementActivity.RecyclerViewAdapter rvAdapter = new
                ChargingDeviceManagementActivity.RecyclerViewAdapter(mNameData, mDevIDData, mFwUpdate);
        rvAdapter.setOnRecyclerItemClickListener((view, position) -> {
            CheckRemoveMode(true);
            txtRemove.setOnClickListener(v -> {
                mNameData.remove(position);
                mDevIDData.remove(position);
                mFwUpdate.remove(position);
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
        imgCheck1.setOnClickListener(v -> CheckRemoveMode(true));
        imgCheck2.setOnClickListener(v -> CheckRemoveMode(false));
    }

    private void CheckRemoveMode(boolean checkMode) {
        if (checkMode) {
            imgCheck1.setBackgroundResource(R.drawable.check_circle);
            imgCheck2.setBackgroundResource(R.drawable.test_2_check_circle);
        } else {
            imgCheck1.setBackgroundResource(R.drawable.test_2_check_circle);
            imgCheck2.setBackgroundResource(R.drawable.check_circle);
        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<ChargingDeviceManagementActivity
            .RecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Integer> mData, mDevId;
        private final ArrayList<Boolean> mFw;
        private ChargingDeviceManagementActivity.OnRecyclerItemClickListener mClickListener;

        public RecyclerViewAdapter(ArrayList<Integer> data, ArrayList<Integer> id, ArrayList<Boolean> fw) {
            this.mData = data;
            this.mDevId = id;
            this.mFw = fw;
        }

        public void setOnRecyclerItemClickListener(ChargingDeviceManagementActivity
                                                           .OnRecyclerItemClickListener listener) {
            this.mClickListener = listener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView txtName, txtDevID, txtVersion, txtFwUpdate;
            final ImageView imgDel, imgError;
            View viewItem;

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
            Integer data = mData.get(position);
            Integer id = mDevId.get(position);
            Boolean fw = mFw.get(position);
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
                intent.putExtra("name", position);
                setResult(RESULT_OK, intent);
                finish();
            });
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
    }

    public static int convertDpToPixel(int dp, Context context) {
        float pixel;
        pixel = dp * ((float) context.getResources().getDisplayMetrics().densityDpi
                / DisplayMetrics.DENSITY_DEFAULT);
        return (int) pixel;
    }

    private interface OnRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);
    }
}
