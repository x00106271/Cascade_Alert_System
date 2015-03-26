package adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
        final Alert current=getItem(position);
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.alert_row, parent, false);
        }
        rowView.setTag(current);

        final TextView type=(TextView) rowView.findViewById(R.id.alerttype);
        final TextView date=(TextView) rowView.findViewById(R.id.alertdateshow);
        final TextView title=(TextView) rowView.findViewById(R.id.alerttitle);
        final TextView body=(TextView) rowView.findViewById(R.id.alertbody);
        final TextView createdby=(TextView) rowView.findViewById(R.id.alertby);
        final TextView comments=(TextView) rowView.findViewById(R.id.alertcomments);

        title.setText(current.getTitle());
        body.setText(current.getBody());
        comments.setText("3 comments");
        date.setText("11/11/11");
        createdby.setText("keyth");
        type.setText(getType(current.getAlertType()));

        return rowView;
    }

    // get type string from int value
    public String getType(int id){
        if(id==1){
            return "crime";
        }
        else if(id==2){
            return "missing";
        }
        else if(id==3){
            return "traffic";
        }
        else if(id==4){
            return "weather";
        }
        else if(id==5){
            return "services";
        }
        else{ //event
            return "event";
        }
    }

}