package mobile.javan.co.id.presensi.model;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import mobile.javan.co.id.presensi.R;

/**
 * Created by Purwa on 20/03/2015.
 */
public class MenuArrayAdapter extends ArrayAdapter<MenuModel> {


    private final List<MenuModel> menuModels;
    private final Activity context;


    public MenuArrayAdapter(Activity context, List<MenuModel> menuModels) {
        super(context, R.layout.menu_drawer, menuModels);
        this.context = context;
        this.menuModels = menuModels;
    }

    static class ViewHolder {
        protected LinearLayout layout;
        protected TextView label;
        protected RelativeLayout icon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.menu_drawer, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.label = (TextView) view.findViewById(R.id.menuLabel);
            viewHolder.icon = (RelativeLayout) view.findViewById(R.id.menuIcon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();
//        Log.v("JSON MENU OBJ" , new Gson().toJson(menuModels));
        holder.label.setText(menuModels.get(position).getMenuLabel());

        return view;
    }


}
