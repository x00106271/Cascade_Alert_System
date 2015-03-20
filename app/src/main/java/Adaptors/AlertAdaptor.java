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

    Context mContext;
    int MlayoutResourceId;

    public AlertAdaptor(Context context, int layoutResourceId) {
        super(context,layoutResourceId);

        this.mContext = context;
        this.MlayoutResourceId=layoutResourceId;
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
            rowView = inflater.inflate(MlayoutResourceId, parent, false);
        }
        rowView.setTag(current);
        final TextView text=(TextView) rowView.findViewById(R.id.alertview);
        text.setText(current.getBody());
        return rowView;
    }

}