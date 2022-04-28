package com.axolotls.pracheta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.axolotls.pracheta.R;
import com.axolotls.pracheta.model.GetAllOperators;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RechargeSpinnerAdapter extends ArrayAdapter<Integer> {
    private Integer[] images;
    private String[] text;
    private Context context;

    public RechargeSpinnerAdapter(Context context, Integer[] images, String[] text) {
        super(context, android.R.layout.simple_spinner_item, images);
        this.images = images;
        this.text = text;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getImageForPosition(position, convertView, parent);
    }

    private View getImageForPosition(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.recharge_spinner_layout, parent, false);
        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(text[position]);
        ImageView imageView = (ImageView) row.findViewById(R.id.spinnerImages);
        imageView.setImageResource(images[position]);
        return row;
    }
}
