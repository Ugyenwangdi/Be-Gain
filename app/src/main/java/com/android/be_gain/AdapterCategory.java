package com.android.be_gain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.be_gain.databinding.RowCategoryBinding;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> {

    private Context context;
    private ArrayList<ModelCategory> categoryArrayList;

    // view binding
    private RowCategoryBinding binding;


    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // bind row_category.xml
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategory.HolderCategory holder, int position) {
        // get data
        ModelCategory model = categoryArrayList.get(position);
        String id = model.getId();
        String category = model.getCategory();
        String uid = model.getUid();
        String timestamp = model.getTimestamp();

        // set data
        holder.categoryTv.setText(category);

        // handle click, delete category
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+category, Toast.LENGTH_SHORT).show();
                // will do after showing, lets first show categories
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    /* View holder to UI views for row_category.xml*/
    class HolderCategory extends RecyclerView.ViewHolder
    {
        // ui views of row_category.xml
        TextView categoryTv;
        ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemView)
        {
            super(itemView);
            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;
        }
    }
}
