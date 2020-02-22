package com.example.barcodescannerusinggooglesheet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<ListItem> {

    List<ListItem> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyAdapter(Context context, List<ListItem> objects) {
        super(context, R.layout.list_item, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public ListItem getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.list_item, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ListItem item = getItem(position);

        vh.textViewId.setText(item.getId());
        vh.textViewBarcode.setText(item.getBarcode());
        return vh.rootView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final TextView textViewId;
        public final TextView textViewBarcode;

        private ViewHolder(RelativeLayout rootView, TextView textViewBarcode, TextView textViewId) {
            this.rootView = rootView;
            this.textViewId = textViewId;
            this.textViewBarcode = textViewBarcode;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textViewId = (TextView) rootView.findViewById(R.id.textViewId);
            TextView textViewBarcode = (TextView) rootView.findViewById(R.id.textViewCode);

            return new ViewHolder(rootView, textViewBarcode, textViewId);
        }
    }
}