package com.popseven.hidefile.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.popseven.hidefile.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class MyOutgoingCallHandler extends BroadcastReceiver {

    private SharedPreferences sharedPref,sharedPref1;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPref = context.getSharedPreferences("com.popseven.hidefile.loginpin",MODE_PRIVATE);
        sharedPref1 = context.getSharedPreferences("com.popseven.hidefile.hideicon",MODE_PRIVATE);
        // Extract phone number reformatted by previous receivers
        String phoneNumber = getResultData();
        if (phoneNumber == null) {
            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }

        if (phoneNumber.equals("#"+sharedPref.getInt("loginpin",111111111)+"#")) { // DialedNumber checking.
            // My app will bring up, so cancel the broadcast
            setResultData(null);

            // Start my app
            if(sharedPref1.getBoolean("hideicon", false)==true){
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("extra_phone", phoneNumber);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }

    }
}