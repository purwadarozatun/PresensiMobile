package mobile.javan.co.id.presensi.model.adapter.array;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mobile.javan.co.id.presensi.R;
import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.adapter.result.StaticResultAdapter;
import mobile.javan.co.id.presensi.util.Statics;

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
        protected LinearLayout layout;
        protected LinearLayout imageView;
        protected TextView nama;
        protected TextView status;
        protected TextView nik;
        protected TextView jamMasuk;
        protected TextView jamKerja;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.presensi_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.nama = (TextView) view.findViewById(R.id.label);
            viewHolder.jamKerja = (TextView) view.findViewById(R.id.jam_kerja);
            viewHolder.nik = (TextView) view.findViewById(R.id.nik);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.jamMasuk = (TextView) view.findViewById(R.id.jam_masuk);
            viewHolder.layout = (LinearLayout) view.findViewById(R.id.mainlayout);
            viewHolder.imageView = (LinearLayout) view.findViewById(R.id.statusPulangHolder);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.nama.setText(persons.get(position).getNama());
        holder.nik.setText(persons.get(position).getNik());

        holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.statusfilter));

        if (persons.get(position).getJamMasuk() != null) {
            holder.jamKerja.setText("" + persons.get(position).getDurasiKerja());
            holder.jamMasuk.setText("Jam Masuk : " + new Statics().getStringFrom("hh:mm:ss", persons.get(position).getJamMasuk(), "00:00:00"));
            holder.jamMasuk.setVisibility(View.VISIBLE);
        } else {
            holder.jamKerja.setText("-");
            holder.jamMasuk.setVisibility(View.GONE);
        }

        StaticResultAdapter statusKerja = persons.get(position).getStatusKerja();
        holder.layout.setBackgroundColor(Color.argb(50, 255, 255, 255));
        holder.status.setText("");
        String statusString = "";
        if (statusKerja != null) {
            if (statusKerja.status == true) {
                holder.layout.setBackgroundColor(Color.argb(50, 86, 232, 151));
            } else {
                holder.layout.setBackgroundColor(Color.argb(50, 255, 96, 74));
            }
            statusString = statusKerja.getResponseStatus();
        }
        StaticResultAdapter statusPulang = persons.get(position).getStatusPulang();

        if (null != statusPulang) {
            if (statusPulang.status == true) {
                holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.jam_kerja_cukup));
            } else {
                holder.imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.pulang_cepat));
            }
            if (!statusString.isEmpty()) {
                statusString += " , " + statusPulang.getResponseStatus();
            }
        }

        if (!statusString.isEmpty()) {
            holder.status.setText(statusString);
        }

        if (persons.get(position).getIzin() != "" && !persons.get(position).getIzin().equals("null")) {
            holder.layout.setBackgroundColor(Color.argb(100, 130, 218, 232));
            holder.status.setText(persons.get(position).getIzin());
        }

        return view;
    }


}
