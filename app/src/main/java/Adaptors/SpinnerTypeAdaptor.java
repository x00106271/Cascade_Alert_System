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

public class SpinnerTypeAdaptor extends ArrayAdapter<String>{

    private int groupid;
    private String[] textData;
    private Activity context;
    private LayoutInflater inflater;

    public SpinnerTypeAdaptor(Activity context, int vg, int id,String[] textData){
        super(context,id,textData);
        this.context=context;
        groupid=vg;
        this.textData=textData;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getView(position,convertView,parent);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView=inflater.inflate(groupid, parent, false);
        ImageView iconpart=(ImageView) itemView.findViewById(R.id.image_spinner);
        iconpart.setImageResource (getImage(position));
        TextView textpart= (TextView) itemView.findViewById(R.id.text_image_spinner);
        textpart.setText (textData[position]);
        return itemView;

    }
    // add image icons
    public Integer getImage(int pos){
        Integer[] imageIds=new Integer[8];
        imageIds[0]=R.drawable.crime;
        imageIds[1]=R.drawable.fire;
        imageIds[2]=R.drawable.missing2;
        imageIds[3]=R.drawable.stolen;
        imageIds[4]=R.drawable.road;
        imageIds[5]=R.drawable.service;
        imageIds[6]=R.drawable.weather;
        imageIds[7]=R.drawable.event;
        return(imageIds[pos]);

    }


}