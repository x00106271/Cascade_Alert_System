package adaptors;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cascadealertsystem.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import activities.MainActivity;
import models.Alert;
import models.Comment;
import services.MobileService;
import services.MobileServiceApp;


public class AlertAdaptor extends ArrayAdapter<Alert> {

    private Context mContext;
    private ArrayList<MainActivity.Names> names;
    private ArrayList<Comment> comments;

    public AlertAdaptor(Context context) {
        super(context, R.layout.alert_row);

        this.mContext = context;
    }

    /**
     * Returns the view for a specific item on the list
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        viewHolder holder;
        final Alert current = getItem(position);
        if (rowView == null) {
            holder = new viewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.alert_row, parent, false);
            holder.title = (TextView) rowView.findViewById(R.id.alert_title);
            holder.body = (TextView) rowView.findViewById(R.id.alert_body);
            holder.createdby = (TextView) rowView.findViewById(R.id.alert_who);
            holder.image = (ImageView) rowView.findViewById(R.id.alert_image_type);
            holder.date = (TextView) rowView.findViewById(R.id.alert_date);
            holder.comment = (TextView) rowView.findViewById(R.id.alert_comments);
            holder.post = (Button) rowView.findViewById(R.id.alert_post);
            holder.addPost = (EditText) rowView.findViewById(R.id.alert_add_comment);
            rowView.setTag(holder);
        } else {
            holder = (viewHolder) rowView.getTag();
        }

        holder.title.setText(current.getTitle());
        holder.body.setText(current.getBody());
        holder.createdby.setText(getName(current.getCreatedBy()));
        holder.date.setText(getDateString(current.getStartDateTime()));
        holder.comment.setText("view comments (" + getNumComments(current.getId()) + ")");
        holder.addPost.setText("");
        holder.addPost.setHint("Add comment...");
        final String s=holder.addPost.getText().toString();

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=(LinearLayout) v.getParent();
                EditText com=(EditText)layout.findViewById(R.id.alert_add_comment);
                String s=com.getText().toString();
                if(!s.equals("")){
                    Comment nCom=new Comment(current.getId(),current.getCreatedBy(),s);
                    comments.add(nCom);
                    if(mContext instanceof MainActivity){
                        ((MainActivity)mContext).addComment(nCom);
                    }
                    try {
                        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.i("keyboard error: ",e.getMessage());
                    }
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout=(LinearLayout)v.getParent();
                for(Comment com:comments){
                    if(com.getAlertId().equals(current.getId())){
                        TextView tv=new TextView(v.getContext());
                        TextView tv2=new TextView(v.getContext());
                        tv.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv2.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv.setText(com.getBody());
                        tv.setGravity(Gravity.CENTER);
                        tv2.setTextColor(Color.CYAN);
                        tv2.setText(getName(com.getCreatedBy()));
                        tv2.setBackgroundResource(R.drawable.border_grey);
                        layout.addView(tv);
                        layout.addView(tv2);
                    }
                }
            }
        });

        if (current.getAlertType() == 0) {
            holder.image.setImageResource(R.drawable.crime);
        } else if (current.getAlertType() == 1) {
            holder.image.setImageResource(R.drawable.fire);
        } else if (current.getAlertType() == 2) {
            holder.image.setImageResource(R.drawable.missing2);
        } else if (current.getAlertType() == 3) {
            holder.image.setImageResource(R.drawable.stolen);
        } else if (current.getAlertType() == 4) {
            holder.image.setImageResource(R.drawable.road);
        } else if (current.getAlertType() == 5) {
            holder.image.setImageResource(R.drawable.service);
        } else if (current.getAlertType() == 6) {
            holder.image.setImageResource(R.drawable.weather);
        } else {
            holder.image.setImageResource(R.drawable.event);
        }

        return rowView;
    }

    // holder of views
    static class viewHolder {
        TextView title;
        TextView body;
        TextView createdby;
        ImageView image;
        TextView date;
        TextView comment;
        Button post;
        EditText addPost;
    }

    // return date as string
    public String getDateString(Date d) {
        DateFormat inputDF = new SimpleDateFormat("dd/MM/yyyy");
        String s = inputDF.format(d);
        return s;
    }

    // set names list
    public void setList(ArrayList<MainActivity.Names> list) {
        this.names = list;
    }

    // set comment list
    public void setCommentList(ArrayList<Comment> list) {
        this.comments = list;
    }

    // get string name from user id
    public String getName(String id) {
        String s = "";
        for (MainActivity.Names name : names) {
            if (name.id.equals(id)) {
                s = name.first + " " + name.last;
            }
        }
        return s;
    }

    //get comments for alert
    public int getNumComments(String id) {
        int num = 0;
        for (Comment com : comments) {
            if (com.getAlertId().equals(id)) {
                num++;
            }
        }
        return num;
    }

}