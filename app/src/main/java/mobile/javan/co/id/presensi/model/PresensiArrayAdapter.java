package mobile.javan.co.id.presensi.model;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
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
        super(context, R.layout.presensi_list_small_layout, persons);
        this.context = context;
        this.persons = persons;
    }

    static class ViewHolder {
        protected TextView nama;
        protected TextView status;
        protected TextView nik;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.presensi_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.nama = (TextView) view.findViewById(R.id.label);
            viewHolder.nik = (TextView) view.findViewById(R.id.nik);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nama.setText(persons.get(position).getNama());
        holder.nik.setText(persons.get(position).getNik());
        StaticResponse staticResponse = persons.get(position).getStatusKerja();
        holder.nama.setBackgroundColor(Color.argb(50, 255, 255, 255));
        holder.status.setBackgroundColor(Color.argb(50, 255, 255, 255));
        holder.status.setText("");

        if (staticResponse != null) {
            if (staticResponse.status == true) {
                holder.nama.setBackgroundColor(Color.argb(50, 86, 232, 151));
                holder.status.setBackgroundColor(Color.argb(50, 86, 232, 151));
            } else {
                holder.nama.setBackgroundColor(Color.argb(50, 255, 96, 74));
                holder.status.setBackgroundColor(Color.argb(50, 255, 96, 74));
            }
            holder.status.setText(staticResponse.responseStatus);
        }
        if(persons.get(position).getIzin() != "" && !persons.get(position).getIzin().equals("null")){
            holder.nama.setBackgroundColor(Color.argb(100, 130, 218, 232));
            holder.status.setBackgroundColor(Color.argb(100, 130, 218, 232));
            holder.status.setText(persons.get(position).getIzin());
        }

        return view;
    }


}
