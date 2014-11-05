package ca.ualberta.app.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

public class InternetConnection {
	public void myClickHandler(View view) {
	    ConnectivityManager connMgr = (ConnectivityManager) 
	    		view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected()) {
	        // fetch data
	    } else {
	        // display error
	    }
	}
}
