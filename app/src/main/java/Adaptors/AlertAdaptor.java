package adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cascadealertsystem.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import activities.MainActivity;
import models.Alert;
import models.Comment;
import models.OutputFile;


public class AlertAdaptor extends ArrayAdapter<Alert> {

    private Context mContext;
    private ArrayList<MainActivity.Names> names;
    private ArrayList<Comment> comments;
    private ArrayList<OutputFile> files;

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
            holder.drop = (ImageButton) rowView.findViewById(R.id.alert_image_dropdown);
            rowView.setTag(holder);
        } else {
            holder = (viewHolder) rowView.getTag();
        }
        LinearLayout layout = (LinearLayout) holder.comment.getParent();
        clearComments(layout);
        holder.comment.setEnabled(true);
        holder.title.setText(current.getTitle());
        holder.body.setText(current.getBody());
        holder.createdby.setText(getName(current.getCreatedBy()));
        holder.date.setText(getDateString(current.getStartDateTime()));
        holder.comment.setText("view comments (" + getNumComments(current.getId()) + ")");
        holder.addPost.setText("");
        holder.addPost.setHint("Add comment...");
        final String aid = current.getCreatedBy();

        holder.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) v.getParent();
                EditText com = (EditText) layout.findViewById(R.id.alert_add_comment);
                String s = com.getText().toString();
                if (!s.equals("")) {
                    Comment nCom = new Comment(current.getId(), current.getCreatedBy(), s);
                    comments.add(nCom);
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).addComment(nCom);
                    }
                    try {
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.i("keyboard error: ", e.getMessage());
                    }
                    sortEmptyFill(v);
                }
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                LinearLayout layout = (LinearLayout) v.getParent();
                for (Comment com : comments) {
                    if (com.getAlertId().equals(current.getId())) {
                        TextView tv = new TextView(v.getContext());
                        TextView tv2 = new TextView(v.getContext());
                        tv.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv2.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        tv2.setText(com.getBody());
                        tv2.setGravity(Gravity.CENTER);
                        tv.setTextColor(Color.GRAY);
                        tv.setText(getName(com.getCreatedBy()));
                        tv2.setBackgroundResource(R.drawable.border_grey);
                        layout.addView(tv);
                        layout.addView(tv2);
                    }
                }
            }
        });

        holder.drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.getMenuInflater().inflate(R.menu.menu_alert_popup, popup.getMenu());
                Menu popupMenu = popup.getMenu();
                // if user does not own alert switch off edit and remove buttons
                Log.i("aid: ", aid);
                Log.i("userid: ", ((MainActivity) mContext).getUserId());
                if (!aid.equals(((MainActivity) mContext).getUserId())) {
                    popupMenu.findItem(R.id.one).setEnabled(false);
                    popupMenu.findItem(R.id.two).setEnabled(false);
                }
                // if user is not garda switch off response button
                if (getUserLevel(((MainActivity) mContext).getUserId()) != 2) {
                    popupMenu.findItem(R.id.three).setEnabled(false);
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.one:
                                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                final LinearLayout layout = new LinearLayout(mContext);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                alert.setTitle("Edit Alert");
                                TextView view = new TextView(mContext);
                                view.setText("Title");
                                layout.addView(view);
                                final EditText title = new EditText(mContext);
                                title.setText(current.getTitle());
                                layout.addView(title);
                                TextView view2 = new TextView(mContext);
                                view2.setText("Description");
                                layout.addView(view2);
                                final EditText body = new EditText(mContext);
                                body.setText(current.getBody());
                                layout.addView(body);
                                alert.setView(layout);
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String newTitle = title.getText().toString();
                                        String newBody = body.getText().toString();
                                        current.setTitle(newTitle);
                                        current.setBody(newBody);
                                        ((MainActivity) mContext).updateAlert(current);
                                        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                });

                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                                alert.show();
                                return true;
                            case R.id.two:
                                AlertDialog.Builder adb = new AlertDialog.Builder(mContext);
                                adb.setTitle("Delete Alert");
                                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        current.setActive(false);
                                        ((MainActivity) mContext).deleteAlert(current);
                                    }
                                });
                                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                adb.show();

                                return true;
                            case R.id.three:
                                AlertDialog.Builder alert3 = new AlertDialog.Builder(mContext);
                                alert3.setTitle("Garda Response");
                                final EditText input = new EditText(mContext);
                                alert3.setView(input);
                                alert3.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String value = input.getText().toString();
                                        // deal with garda response...send string to DB table ??
                                        current.setActive(false);
                                        ((MainActivity) mContext).deleteAlert(current);
                                        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputManager.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    }
                                });

                                alert3.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });
                                alert3.show();
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
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
            //HAVE THE ALERT AND MEDIA CHECK HERE...IF MEDIA FOUND PUT IN A IMAGE/VIDEO VIEW
        // AND RUN THE BACKGROUND THREAD (SPINNER RUNNING WHILE IT LOADS) AND HAVE IT LOAD IMAGE/VIDEO
        // WHEN IT RETURNS
        return rowView;


    }

    // holder of views (better performance holding them in this than inflating every time)
    static class viewHolder {
        TextView title;
        TextView body;
        TextView createdby;
        ImageView image;
        TextView date;
        TextView comment;
        Button post;
        EditText addPost;
        ImageButton drop;
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

    //set files list
    public void setFileList(ArrayList<OutputFile> file){
        this.files=file;
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

    //get number comments for alert
    public int getNumComments(String id) {
        int num = 0;
        for (Comment com : comments) {
            if (com.getAlertId().equals(id)) {
                num++;
            }
        }
        return num;
    }

    // clear comments
    public void clearComments(LinearLayout lay){
        int count = lay.getChildCount(); // get amount of child views in layout
        if(count>1){ // delete all views after the first view which we need to keep
            View v;
            for(int i=1;i<count;i++){
                v = lay.getChildAt(i);
                v.setVisibility(View.GONE);
            }
        }
    }

    // re-enable comment button/clear comments/fill comments (done after new comment added)
    public void sortEmptyFill(View v){
        LinearLayout lay=(LinearLayout)v.getParent(); // get parent of view
        RelativeLayout rel=(RelativeLayout)lay.getParent();
        LinearLayout view=(LinearLayout)rel.findViewById(R.id.alert_dropdown);
        TextView view2=(TextView)view.findViewById(R.id.alert_comments);
        view2.setEnabled(true);
        clearComments(view);
        view2.performClick();
    }

    //return user level of user
    public int getUserLevel(String id){
        int r=0;
        for(MainActivity.Names n:names){
            if(n.id.equals(id)){
                r=n.userLevel;
            }
        }
        return r;
    }

}