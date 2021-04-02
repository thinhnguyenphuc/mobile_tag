package com.example.a18127223_note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomArrayAdapter extends ArrayAdapter<items>{
    Context context;
    ArrayList<items> arrayList;
    int layoutResource;
    ArrayList<Integer> hide;

    public CustomArrayAdapter(Context context, int resource, ArrayList<items> objects, ArrayList<Integer> hide) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
        this.layoutResource = resource;
        this.hide = hide;

    }
    @NonNull
    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        if(!this.hide.contains(position) ){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResource,null);
            TextView titleView = (TextView)convertView.findViewById(R.id.textTitle);
            titleView.setText(arrayList.get(position).getTile());
            TextView tagView = (TextView)convertView.findViewById(R.id.textTag);
            tagView.setText(arrayList.get(position).getTag());
            TextView timeView = (TextView)convertView.findViewById(R.id.textTime);
            timeView.setText(arrayList.get(position).getTime());
            return convertView;
        } else{
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_hide,null);
            return convertView;
        }
    }

}
