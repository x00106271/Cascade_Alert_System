package adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import com.cascadealertsystem.R;
import activities.MessageActivity;



public class NamesListAdaptor extends ArrayAdapter<MessageActivity.Names> {

    private Context mContext;


    public NamesListAdaptor(Context context) {
        super(context, R.layout.names_row);

        this.mContext = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        viewHolder holder;
        final MessageActivity.Names current = getItem(position);
        if (rowView == null) {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.names_row, parent, false);
            holder.box=(CheckBox)rowView.findViewById(R.id.checkbox_name);
            rowView.setTag(holder);
        } else {
            holder = (viewHolder) rowView.getTag();
        }
        String name=current.first+" "+current.last;
        holder.box.setText(name);
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()){
                    MessageActivity.names.get(position).picked=true;
                }
                else{
                    MessageActivity.names.get(position).picked=false;
                }
            }
        });
        if(MessageActivity.names.get(position).picked==true){
            holder.box.setChecked(true);
        }
        else{
            holder.box.setChecked(false);
        }

        return rowView;


    }

    // holder of views (better performance holding them in this than inflating every time)
    static class viewHolder {
        CheckBox box;
    }
}
