package services;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.cascadealertsystem.R;

import java.io.FileNotFoundException;

import activities.MainActivity;

public class VideoGallery extends Activity {

    private VideoView targetvideo;
    private Button ok;
    private Button cancel;
    private static final int SELECT_VIDEO = 100;
    private MediaStore.Video image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // start intent to get video
        Intent videoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        videoPickerIntent.setType("video/*");
        startActivityForResult(videoPickerIntent, SELECT_VIDEO);

        targetvideo = (VideoView) findViewById(R.id.target_video);
        ok=(Button)findViewById(R.id.gal_ok);
        cancel=(Button)findViewById(R.id.gal_cancel);
        ok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_create_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    //when photo choosen put on screen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent videoReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, videoReturnedIntent);

        switch(requestCode) {
            case SELECT_VIDEO:
                if(resultCode == RESULT_OK){
                    Uri selectedVideo = videoReturnedIntent.getData();
                    targetvideo.setVideoURI(selectedVideo);
                }
        }
    }

}
