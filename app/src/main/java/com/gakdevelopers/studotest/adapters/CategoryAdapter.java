package com.gakdevelopers.studotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> categoryList;

    public CategoryAdapter(List<CategoryModel> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;

        if (view == null) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        } else {
            v = view;
        }

        TextView txtCategoryName = v.findViewById(R.id.txtCategoryName);
        TextView txtNoOfTest = v.findViewById(R.id.txtNoOfTest);

        txtCategoryName.setText(categoryList.get(i).getName());
        txtNoOfTest.setText("" + categoryList.get(i).getNoOfTest() + " Test(s)");

        return v;
    }
}
