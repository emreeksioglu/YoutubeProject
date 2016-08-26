package com.ekswork.youtubeplaylisttoexcel;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private DBHelper dbHelper;
    private List<VideoItem> searchResults;
    private String API_KEY="AIzaSyBuZds-9w6gWTdD1SmBOisGNKWTs7xDQYY";
    private Handler handler;
    private Intent intent ;
    private ListView lstVideos;
    private EditText txtUrl;
    private TextView txtMsg;
    private Button btnExport;
    private Button btnSearch;


    public void Init(){
        txtUrl = (EditText)findViewById(R.id.txtUrl);
        txtMsg = (TextView)findViewById(R.id.txtMessage);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnExport = (Button)findViewById(R.id.btnExport);
        lstVideos = (ListView)findViewById(R.id.lstVideos);

        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();

        dbHelper =new DBHelper(this);
        dbHelper.ClearTable();

        intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();

        if (Intent.ACTION_SEND.equals(action))
            txtUrl.setText(AppHelper.YOUTUBE_PLAYLIST_URL_PREFIX + intent.getStringExtra(Intent.EXTRA_TEXT).split("list=")[1]);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.ClearTable();

                txtMsg.setText(getResources().getString(R.string.msgLoadingText));

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtUrl.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                searchOnYoutube(txtUrl.getText().toString());
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               boolean status =  dbHelper.ExportToExcel();
                if (status)
                    Util.ShowMessage(getResources().getString(R.string.msgSaveSuccess),getApplicationContext());
                else
                    Util.ShowMessage(getResources().getString(R.string.msgSaveError),getApplicationContext());
            }
        });
    }

    private void searchOnYoutube(final String yUrl){
        new Thread(){
            public void run(){

                final String playListId = Util.ValidateUrl(yUrl);
                if (playListId.isEmpty()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtMsg.setText(getResources().getString(R.string.msgErrorUrlIsUnvalid));
                            return;

                        }
                    });
                }else {
                    YoutubeConnector yc = new YoutubeConnector(getApplicationContext());
                    searchResults = yc.GetPlayListItem(playListId);

                    handler.post(new Runnable(){
                        public void run(){
                            updateVideosFound();
                        }
                    });
                }
            }
        }.start();
    }

    private void updateVideosFound(){
        for (VideoItem item : searchResults   ){
            dbHelper.InsertVideo(item);
        }

        Log.w("wsd","Tablo sayım");
        Log.w("wsd", Integer.toString( dbHelper.GetTableCount()));

        CustomAdapter adapter = new CustomAdapter(MainActivity.this, searchResults);
        lstVideos.setAdapter(adapter);
        lstVideos.setScrollingCacheEnabled(false);

        txtMsg.setText(getResources().getString(R.string.ISOK) + lstVideos.getCount() + getResources().getString(R.string.msgVideoFound));
    }
}
