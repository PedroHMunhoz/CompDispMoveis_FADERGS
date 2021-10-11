package br.com.pedrohmunhoz.appgameslibrary;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Console> {

    private Context context;
    private List<Console> consoles;

    public SpinnerAdapter(Context context, int textViewResourceId,
                       List<Console> consoles) {
        super(context, textViewResourceId, consoles);
        this.context = context;
        this.consoles = consoles;
    }

    @Override
    public int getCount(){
        return consoles.size();
    }

    @Override
    public Console getItem(int position){
        return consoles.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(consoles.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(consoles.get(position).getName());
        return label;
    }
}
