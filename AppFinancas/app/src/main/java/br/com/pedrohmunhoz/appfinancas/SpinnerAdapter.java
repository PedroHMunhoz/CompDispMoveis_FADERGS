package br.com.pedrohmunhoz.appfinancas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<ContaBancaria> {

    private Context context;
    private List<ContaBancaria> contas;

    public SpinnerAdapter(Context context, int textViewResourceId,
                          List<ContaBancaria> contas) {
        super(context, textViewResourceId, contas);
        this.context = context;
        this.contas = contas;
    }

    @Override
    public int getCount() {
        return contas.size();
    }

    @Override
    public ContaBancaria getItem(int position) {
        return contas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(contas.get(position).getNomeENumerConta());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(contas.get(position).getNomeENumerConta());
        return label;
    }
}
