package com.pwned.aggrogamer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
 

public class AggroGamer extends FragmentActivity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(GetSDKVersion() >= 11){
        	Intent myIntent = new Intent(AggroGamer.this, AggroRSSTablet.class);
			startActivity(myIntent);
        } else {
        	Intent myIntent = new Intent(AggroGamer.this, AggroRSS.class);
			startActivity(myIntent);
        }
        /* 
         * Remember to call finish();
         */
        finish();
    }
	

	private int GetSDKVersion() {
	    int version = 0;
	    version = Build.VERSION.SDK_INT; 
	    Log.e("SDK Version", Integer.toString(version));
	    return version;
	}
}
