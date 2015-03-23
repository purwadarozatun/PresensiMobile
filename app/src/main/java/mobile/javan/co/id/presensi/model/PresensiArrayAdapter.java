package mobile.javan.co.id.presensi.model;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import mobile.javan.co.id.presensi.R;

/**
 * Created by Purwa on 20/03/2015.
 */
public class PresensiArrayAdapter extends ArrayAdapter<Person> {


    private final List<Person> persons;
    private final Activity context;


    public PresensiArrayAdapter(Activity context, List<Person> persons) {
        super(context, R.layout.row_button_layout, persons);
        this.context = context;
        this.persons = persons;
    }

    static class ViewHolder {
        protected TextView nama;
        protected TextView status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.row_button_layout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.nama = (TextView) view.findViewById(R.id.label);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nama.setText(persons.get(position).getNama());
        StaticResponse staticResponse = persons.get(position).getStatusKerja();
        if (staticResponse != null) {
            if (staticResponse.status == true) {
                holder.nama.setBackgroundColor(Color.argb(50, 86, 232, 151));
            } else {
                holder.nama.setBackgroundColor(Color.argb(50, 255, 96, 74));
            }
        }else{
            holder.nama.setBackgroundColor(Color.argb(50, 255, 255, 255));
        }
        return view;
    }


}
