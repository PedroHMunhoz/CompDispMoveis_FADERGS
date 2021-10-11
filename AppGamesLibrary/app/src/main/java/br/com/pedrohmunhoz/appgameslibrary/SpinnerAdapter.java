package br.com.pedrohmunhoz.appgameslibrary;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// This class extends from the ArrayAdapter typed with the Console class
public class SpinnerAdapter extends ArrayAdapter<Console> {

    private Context context;
    private List<Console> consoles;

    // Constructor from the class, to set the class variables
    public SpinnerAdapter(Context context, int textViewResourceId,
                       List<Console> consoles) {
        super(context, textViewResourceId, consoles);
        this.context = context;
        this.consoles = consoles;
    }

    // Override the getCount method of the base class, to return the List size
    @Override
    public int getCount(){
        return consoles.size();
    }

    // Override the getItem method of the base class, to return the selected item based on the position
    @Override
    public Console getItem(int position){
        return consoles.get(position);
    }

    // Override the getItemId method of the base class, to return the item ID based on the position
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(consoles.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(consoles.get(position).getName());
        return label;
    }
}
