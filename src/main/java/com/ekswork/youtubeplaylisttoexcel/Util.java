package com.ekswork.youtubeplaylisttoexcel;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by user on 26.08.2016.
 */
public class Util {



    public static void ShowMessage(String msg, Context c){
        Toast.makeText(c,msg,Toast.LENGTH_LONG).show();
    }

    public static String ValidateUrl(String tempUrl){
        String returnVal="";

        try {

            if (tempUrl.contains("http"))
                returnVal = tempUrl.split("list=")[1];


        }catch (Exception ex){
            returnVal="";
        }
        return  returnVal;
    }

}
