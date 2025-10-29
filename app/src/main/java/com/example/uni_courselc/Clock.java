package com.example.uni_courselc;
import android.util.Log;

import java.util.Calendar;
public class Clock {

    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    String greet = "";


    public String setGreet() {

        if(hour >= 0 && hour <= 12){
            greet = "Good Morning!!";


        }else if(hour >=12 && hour <=18){
            greet = "Good Afternoon!!";

        }
        else if(hour >=18 && hour <=24){
            greet = "Good Evening!!";


        }
        return  greet;



    }
    public void testing(){
        if(hour == 0){
            Log.d("null","null");
        }else{
            Log.d("not null",greet);
            System.out.println(hour);
        }
    }




}
