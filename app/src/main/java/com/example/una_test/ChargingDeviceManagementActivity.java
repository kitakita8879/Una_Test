package com.example.una_test;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.una_test.databinding.ActivityChargingDeviceManagementBinding;
import com.example.una_test.databinding.Test2AddDialogBinding;
import com.example.una_test.databinding.Test2RemoveDialogBinding;
import com.example.una_test.databinding.Test2ViewItemBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ChargingDeviceManagementActivity extends AppCompatActivity {
    // todo: choco 通常不會有 public, 只要有寫到 public, static 都要注意是否能避免
    public final ArrayList<Data> dataList = new ArrayList<>();
    private Test2RemoveDialogBinding mRemoveBinding;
    private Dialog mDialogRemove;
    private RecyclerViewAdapter rvAdapter;
    private boolean isRemoveData = false;
    // todo: choco 測試已知故意寫錯時不會要求 coding style, 如果是正式的會盡量要求, 甚至是屬性排列順序
    public final ObservableField<String> test = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChargingDeviceManagementBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_charging_device_management);

        dataList.add(new Data(1, 12345678, true));
        dataList.add(new Data(2, 87654321, false));

        binding.imgBack.setOnClickListener(v -> {
            if (isRemoveData) {
                Intent intent = new Intent();
                intent.putExtra("name", dataList.get(0).name);
                setResult(RESULT_OK, intent);
            }
            finish();
        });

        binding.recyclerViewTest2.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RecyclerViewAdapter(dataList);
        rvAdapter.setOnRecyclerItemClickListener((view, position) -> this.showRemoveDialog(position));
        binding.recyclerViewTest2.setAdapter(rvAdapter);

        RecyclerSpace decoration = new RecyclerSpace(convertDpToPixel(30, this));
        binding.recyclerViewTest2.addItemDecoration(decoration);

        binding.viewAdd.setOnClickListener(v -> showAddDialog());
        binding.setClickNum(0);
    }

    private void showRemoveDialog(int position) {
        // todo: choco mDialogRemove, mRemoveBinding 可以 local use, 同 showAddDialog
        mDialogRemove = new Dialog(this, R.style.dialogRemove);
        View viewDialog = View.inflate(this, R.layout.test_2_remove_dialog, null);
        mRemoveBinding = Test2RemoveDialogBinding.bind(viewDialog);
        mDialogRemove.setContentView(viewDialog);

        mDialogRemove.show();
        // todo: dialog 除非特別寬、高, 印象中不需要改到 window
        Objects.requireNonNull(mDialogRemove.getWindow()).setLayout(
                convertDpToPixel(300, ChargingDeviceManagementActivity.this),
                WindowManager.LayoutParams.WRAP_CONTENT);

        mRemoveBinding.imgCheck1.setSelected(true);
        mRemoveBinding.txtRemove.setOnClickListener(v -> {
            dataList.remove(position);
            rvAdapter.notifyItemRemoved(position);
            mDialogRemove.dismiss();
            isRemoveData = true;
        });

        mRemoveBinding.txtCancel.setOnClickListener(v -> mDialogRemove.dismiss());
        mRemoveBinding.imgCheck1.setOnClickListener(v -> {
            mRemoveBinding.imgCheck1.setSelected(true);
            mRemoveBinding.imgCheck2.setSelected(false);
        });
        mRemoveBinding.imgCheck2.setOnClickListener(v -> {
            mRemoveBinding.imgCheck2.setSelected(true);
            mRemoveBinding.imgCheck1.setSelected(false);
        });
    }

    private void showAddDialog() {
        Dialog addDialog = new Dialog(this, R.style.dialogRemove);
        View viewDialog = View.inflate(this, R.layout.test_2_add_dialog, null);
        Test2AddDialogBinding addDialogBinding = Test2AddDialogBinding.bind(viewDialog);
        addDialog.setContentView(viewDialog);
        addDialog.show();
        Objects.requireNonNull(addDialog.getWindow()).setLayout(
                convertDpToPixel(300, this), WindowManager.LayoutParams.WRAP_CONTENT);
        addDialogBinding.setItem(this);
        addDialogBinding.txtAdd.setOnClickListener(v -> {
            if (!addDialogBinding.editNum.getText().toString().isEmpty()) {
                int name = Integer.parseInt(addDialogBinding.editNum.getText().toString());
                int devId = name + 10000000;
                dataList.add(new Data(name, devId, false));
                rvAdapter.notifyItemInserted(dataList.size() - 1);
                addDialog.dismiss();
            }
        });
        addDialogBinding.txtCancel.setOnClickListener(v -> addDialog.dismiss());
    }

    public static class Data {
        // todo: choco name, devId 如果不會被更改記得加 final
        public int name, devId;
        public boolean fwUpdate;

        Data(int name, int devId, boolean fwUpdate) {
            this.name = name;
            this.devId = devId;
            this.fwUpdate = fwUpdate;
        }

        public void setFwUpdate(boolean fwUpdate) {
            this.fwUpdate = fwUpdate;
        }
    }

    // todo: choco 可以學學改用 ListAdapter, 了解 ListAdapter 好處, 對比當前 RecyclerView.Adapter
    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Data> mDataList;
        private OnRecyclerItemClickListener mClickListener;

        public RecyclerViewAdapter(ArrayList<Data> dataList) {
            this.mDataList = dataList;
        }

        public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
            this.mClickListener = listener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final Test2ViewItemBinding binding;

            ViewHolder(View item) {
                super(item);
                binding = Test2ViewItemBinding.bind(item);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // todo: choco 通常不會 R. 換行, 會連 R.layout 都換行
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R
                    .layout.test_2_view_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.binding.setData(mDataList.get(position));
            // todo: choco 命名不明確, 如果是 del 應該會是 onDelClick, 且可以把 item 丟出去, 不用只丟 position
            holder.binding.imgDel.setOnClickListener(v ->
                    mClickListener.onRecyclerItemClick(v, holder.getAdapterPosition()));
            // todo: choco 身為 adapter, 最好只關注自身 UI, 避免處理與自身 UI 無關的事, 達到解耦
            holder.binding.viewItem.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.putExtra("name", mDataList.get(holder.getAdapterPosition()).name);
                setResult(RESULT_OK, intent);
                finish();
            });
            holder.binding.imgDevice.setOnClickListener(v -> {
                boolean fwReverse = mDataList.get(holder.getAdapterPosition()).fwUpdate;
                mDataList.get(holder.getAdapterPosition()).setFwUpdate(!fwReverse);
                notifyItemChanged(holder.getAdapterPosition());
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
