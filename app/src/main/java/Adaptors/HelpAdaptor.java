package adaptors;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cascadealertsystem.R;

public class HelpAdaptor extends ArrayAdapter<HelpAdaptor.Item>{
    private int groupid;
    private LayoutInflater inflater;
    private Activity context;

    public HelpAdaptor(Activity context, int vg, int id) {
        super(context,id);
        this.context=context;
        groupid=vg;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Item item=getItem(position);
        View rowView=inflater.inflate(groupid, parent, false);
        rowView = inflater.inflate(R.layout.help_row, parent, false);
        ImageView iconpart=(ImageView) rowView.findViewById(R.id.help_image);
        iconpart.setImageResource(getImage(position));
        TextView textpart= (TextView) rowView.findViewById(R.id.help_text);
        textpart.setText (item.text);

        return rowView;
    }

    // add image icons
    public Integer getImage(int pos) {
        Integer[] imageIds = new Integer[13];
        imageIds[0] = R.drawable.alert;
        imageIds[1] = R.drawable.message;
        imageIds[2] = R.drawable.map;
        imageIds[3] = R.drawable.refresh;
        imageIds[4] = R.drawable.crime;
        imageIds[5]=R.drawable.fire;
        imageIds[6]=R.drawable.missing2;
        imageIds[7]=R.drawable.stolen;
        imageIds[8]=R.drawable.road;
        imageIds[9]=R.drawable.service;
        imageIds[10]=R.drawable.weather;
        imageIds[11]=R.drawable.event;
        imageIds[12]=R.drawable.message;
        return (imageIds[pos]);
    }

        public static class Item{
        public final String text;
        public Item(String text) {
            this.text = text;
        }

    }
}
