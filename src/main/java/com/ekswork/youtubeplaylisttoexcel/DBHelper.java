package com.ekswork.youtubeplaylisttoexcel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by user on 25.08.2016.
 */
public class DBHelper  extends SQLiteOpenHelper {

    private static DBHelper dbInstance;
    public static final String DATABASE_NAME   = "YoutubePlaylistDB";
    private static final int DB_VERSION= 2;

    public static synchronized DBHelper GetInstance(Context ctx){

        if (dbInstance == null)
            dbInstance = new DBHelper(ctx.getApplicationContext());
        return dbInstance;

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE VIDEO (Id INTEGER PRIMARY KEY AUTOINCREMENT,VideoId TEXT, VideoUrl TEXT ,Title TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS VIDEO");
        onCreate(db);
    }




    public void InsertVideo(VideoItem item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("VideoId", item.getId());
        values.put("VideoUrl", item.getVideoUrl());
        values.put("Title", item.getTitle());

        db.insert("VIDEO", null, values);
        db.close();
    }

    public boolean ClearTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("VIDEO",null,null)>0;
    }

    public int GetTableCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        long rowCount =  DatabaseUtils.queryNumEntries(db,"VIDEO");
        return (int)rowCount;
    }

    public boolean ExportToExcel(){

        boolean result = true;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = null;
        try {
            c = db.rawQuery("select VideoId,VideoUrl,Title from VIDEO", null);
            int rowcount = 0;
            int colcount = 0;
            File sdCardDir = Environment.getExternalStorageDirectory();
            String filename = "PlayListForYoutubeToExcel.csv";
            // the name of the file to export with
            File saveFile = new File(sdCardDir, filename);
            FileWriter fw = new FileWriter(saveFile);

            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();
            colcount = c.getColumnCount();
            if (rowcount > 0) {
                c.moveToFirst();

                for (int i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {

                        bw.write(c.getColumnName(i) + ",");

                    } else {

                        bw.write(c.getColumnName(i));

                    }
                }
                bw.newLine();

                for (int i = 0; i < rowcount; i++) {
                    c.moveToPosition(i);

                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1)
                            bw.write(c.getString(j) + ",");
                        else
                            bw.write(c.getString(j));
                    }
                    bw.newLine();
                }
                bw.flush();
            }
        } catch (Exception ex) {
            result = false;
            if (db.isOpen()) {
                db.close();
                Log.w("wsd",ex.getMessage().toString());
            }


        } finally {

        }


        return result;
    }




}
