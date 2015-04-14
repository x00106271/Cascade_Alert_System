package adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cascadealertsystem.R;
import models.Alert;


public class AlertAdaptor extends ArrayAdapter<Alert> {

    private Context mContext;

    public AlertAdaptor(Context context) {
        super(context,R.layout.alert_row);

        this.mContext = context;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=convertView;
        Alert current=getItem(position);
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.alert_row, parent, false);
        }
        rowView.setTag(current);

        TextView title=(TextView) rowView.findViewById(R.id.alert_title);
        TextView body=(TextView) rowView.findViewById(R.id.alert_body);
        TextView createdby=(TextView) rowView.findViewById(R.id.alert_who);
        ImageView image=(ImageView) rowView.findViewById(R.id.alert_image_type);

        title.setText(current.getTitle());
        body.setText(current.getBody());
        createdby.setText("keyth");

        if(current.getAlertType()==0){
            image.setImageResource(R.drawable.crime);
        }
        else if(current.getAlertType()==1){
            image.setImageResource(R.drawable.fire);
        }
        else if(current.getAlertType()==2){
            image.setImageResource(R.drawable.missing2);
        }
        else if(current.getAlertType()==3){
            image.setImageResource(R.drawable.stolen);
        }
        else if(current.getAlertType()==4){
            image.setImageResource(R.drawable.road);
        }
        else if(current.getAlertType()==5){
            image.setImageResource(R.drawable.service);
        }
        else if(current.getAlertType()==6){
            image.setImageResource(R.drawable.weather);
        }
        else{
            image.setImageResource(R.drawable.event);
        }

        return rowView;
    }
}