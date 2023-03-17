package com.example.loginregister.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.loginregister.R;
import com.example.loginregister.model.Category;

import java.util.ArrayList;


public class CategoryAdapter extends BaseAdapter {

    private ArrayList<Category> _listCategory;
    private int _layout;
    Context _context;

    public CategoryAdapter(ArrayList<Category> listCategory) {
        this._listCategory = listCategory;
    }

    public CategoryAdapter(Context context, int layout, ArrayList<Category> listCategory) {
        this._context = context;
        this._layout = layout;
        this._listCategory = listCategory;
    }

    @Override
    public int getCount() {
        return _listCategory.size();

    }

    @Override
    public Object getItem(int position) {
        return _listCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _listCategory.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewTrainee;
        if (convertView == null) {
            viewTrainee = View.inflate(parent.getContext(), R.layout.list_trainee, null);
        } else {
            viewTrainee = convertView;
        }
        Category category = (Category) getItem(position);
        ((TextView) viewTrainee.findViewById(R.id.traineeId)).setText(String.format("ID : %d", category.getId()));
        ((TextView) viewTrainee.findViewById(R.id.name)).setText(String.format("Name : %s", category.getName()));
//        ((TextView) viewTrainee.findViewById(R.id.phone)).setText(String.format("Phone : %s", trainee.getPhone()));
//        ((TextView) viewTrainee.findViewById(R.id.email)).setText(String.format("Email : %s", trainee.getEmail()));
//        ((TextView) viewTrainee.findViewById(R.id.gender)).setText(String.format("Gender : %s", trainee.getGender()));

        return viewTrainee;
    }
}