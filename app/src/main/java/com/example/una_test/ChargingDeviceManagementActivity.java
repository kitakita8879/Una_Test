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
    private Test2RemoveDialogBinding mRemoveBinding;
    private RecyclerViewAdapter mRvAdapter;
    private boolean mIsRemoveData = false;
    private final ArrayList<Data> mDataList = new ArrayList<>();
    public ObservableField<String> test = new ObservableField<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChargingDeviceManagementBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_charging_device_management);

        mDataList.add(new Data(1, 12345678, true));
        mDataList.add(new Data(2, 87654321, false));

        binding.imgBack.setOnClickListener(v -> {
            if (mIsRemoveData) {
                Intent intent = new Intent();
                intent.putExtra("name", mDataList.get(0).name);
                setResult(RESULT_OK, intent);
            }
            finish();
        });

        binding.recyclerViewTest2.setLayoutManager(new LinearLayoutManager(this));
        mRvAdapter = new RecyclerViewAdapter(mDataList);
        mRvAdapter.setOnRecyclerItemClickListener(mRvClickListener);
        binding.recyclerViewTest2.setAdapter(mRvAdapter);

        RecyclerSpace decoration = new RecyclerSpace(convertDpToPixel(30, this));
        binding.recyclerViewTest2.addItemDecoration(decoration);

        binding.viewAdd.setOnClickListener(v -> showAddDialog());
        binding.setClickNum(0);
    }

    private final OnRecyclerItemClickListener mRvClickListener = new OnRecyclerItemClickListener() {
        @Override
        public void onDeleteClick(int position) {
            showRemoveDialog(position);
        }

        @Override
        public void onItemClick(int name) {
            Intent intent = new Intent();
            intent.putExtra("name", name);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    private void showRemoveDialog(int position) {
        Dialog mDialogRemove = new Dialog(this, R.style.dialogRemove);
        View viewDialog = View.inflate(this, R.layout.test_2_remove_dialog, null);
        mRemoveBinding = Test2RemoveDialogBinding.bind(viewDialog);
        mDialogRemove.setContentView(viewDialog);
        mDialogRemove.show();
        mRemoveBinding.imgCheck1.setSelected(true);
        mRemoveBinding.txtRemove.setOnClickListener(v -> {
            mDataList.remove(position);
            mRvAdapter.notifyItemRemoved(position);
            mDialogRemove.dismiss();
            mIsRemoveData = true;
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
                mDataList.add(new Data(name, devId, false));
                mRvAdapter.notifyItemInserted(mDataList.size() - 1);
                addDialog.dismiss();
            }
        });
        addDialogBinding.txtCancel.setOnClickListener(v -> addDialog.dismiss());
    }

    public static class Data {
        public final int name, devId;
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
    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final ArrayList<Data> mDataList;
        private OnRecyclerItemClickListener mClickListener;

        public RecyclerViewAdapter(ArrayList<Data> dataList) {
            this.mDataList = dataList;
        }

        public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener) {
            this.mClickListener = listener;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final Test2ViewItemBinding binding;

            ViewHolder(View item) {
                super(item);
                binding = Test2ViewItemBinding.bind(item);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.test_2_view_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.binding.setData(mDataList.get(position));
            holder.binding.imgDel.setOnClickListener(v ->
                    mClickListener.onDeleteClick(holder.getAdapterPosition()));
            holder.binding.viewItem.setOnClickListener(v ->
                    mClickListener.onItemClick(mDataList.get(holder.getAdapterPosition()).name));
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
        void onDeleteClick(int position);

        void onItemClick(int name);
    }
}
