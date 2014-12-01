package ca.ualberta.app.models;

import android.app.Application;
import android.content.Context;

/**
 * Get the application context
 */
public class ContextProvider extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	/**
	 * get the context
	 * 
	 * @return the context
	 */
	public static Context get() {
		return context;
	}
}
