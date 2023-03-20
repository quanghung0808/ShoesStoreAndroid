package com.example.loginregister.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginregister.DetailActivity;
import com.example.loginregister.R;
import com.example.loginregister.RecycleViewInterface;
import com.example.loginregister.model.GenericShoes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GenericShoesAdapter extends BaseAdapter {

    private ArrayList<GenericShoes> _listGenericShoes;
    private int _layout;
    Context _context;
    private RecycleViewInterface mListener;



    public GenericShoesAdapter(ArrayList<GenericShoes> listGenericShoes) {
        this._listGenericShoes = listGenericShoes;
    }

    public GenericShoesAdapter(Context context, int layout, ArrayList<GenericShoes> listGenericShoes) {
        this._context = context;
        this._layout = layout;
        this._listGenericShoes = listGenericShoes;

    }

    @Override
    public int getCount() {
        return _listGenericShoes.size();

    }

    @Override
    public Object getItem(int position) {
        return _listGenericShoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return _listGenericShoes.get(position).getId();
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewTrainee;
        if (convertView == null) {
            viewTrainee = View.inflate(parent.getContext(), R.layout.shoes_card_view, null);
        } else {
            viewTrainee = convertView;
        }
        GenericShoes genericShoes = (GenericShoes) getItem(position);



         //Get a reference to the ImageView
        ImageView imageView = viewTrainee.findViewById(R.id.product_image);


        Picasso.get().load(genericShoes.getImage())
                .into(imageView);
        //((ImageView) viewTrainee.findViewById(R.id.product_image)).setImageResource();String.format("ID : %d", category.getId())
        ((TextView) viewTrainee.findViewById(R.id.product_title)).setText(String.format("%s", genericShoes.getName()));
        ((TextView) viewTrainee.findViewById(R.id.product_price)).setText(String.format("Price : %f", genericShoes.getPrice()));


        //
        viewTrainee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start the DetailActivity
                Intent intent = new Intent(_context, DetailActivity.class);
                // Pass the data to the DetailActivity using intent extras
                intent.putExtra("shoe_id", genericShoes.getId());
                intent.putExtra("shoe_name", genericShoes.getName());
                intent.putExtra("shoe_price", genericShoes.getPrice());
                intent.putExtra("shoe_image", genericShoes.getImage());
                intent.putExtra("shoe_CategoryId", genericShoes.getCategoryId());



                // Start the DetailActivity
                _context.startActivity(intent);
            }
        });


        //

        return viewTrainee;
    }




//    public class GenericShoesViewHolder extends RecyclerView.ViewHolder {
//        private ConstraintLayout layoutItem;
//        private ImageView img;
//        private TextView tvName;
//        private TextView tvPrice;
//
//        public GenericShoesViewHolder(@NonNull View itemView) {
//            super(itemView);
//            img = itemView.findViewById(R.id.product_image);
//            tvName = itemView.findViewById(R.id.product_title);
//            tvPrice = itemView.findViewById(R.id.product_price);
//            layoutItem = itemView.findViewById(R.id.layout_item);
//        }
//
//        public void bind(final GenericShoes genericShoes, final RecycleViewInterface listener) {
//            tvName.setText(String.format("Name : %s", genericShoes.getName()));
//            tvPrice.setText(String.format("Price : %f", genericShoes.getPrice()));
//            Picasso.get().load(genericShoes.getImage()).into(img);
//
//            layoutItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onItemClick(genericShoes);
//                    }
//                }
//            });
//        }
//    }




}