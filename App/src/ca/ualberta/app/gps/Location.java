package ca.ualberta.app.gps;

import java.util.Date;
import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.models.ContextProvider;
import ca.ualberta.app.network.InternetConnectionChecker;

public class Location {
	private final static int CHECK_PARAMETER = 300000; // in milliseconds
	private static Date lastCheck;

	public static String getLocationName() {
		CacheController cacheController = new CacheController(
				ContextProvider.get());
		getLocationCoordinates();
		return cacheController.getUserLocation();
	}

	public static void forceCheckGPS() {
		lastCheck = null;
	}

	public static double[] getLocationCoordinates() {
		CacheController cacheController = new CacheController(
				ContextProvider.get());
		if (InternetConnectionChecker.isNetworkAvailable()
				&& (lastCheck == null || (new Date().getTime()
						- lastCheck.getTime() >= CHECK_PARAMETER))) {
			try {
				double[] coord = findLocation();
				cacheController.saveUserCoordinates(coord);
				cacheController.saveUserLocation(GeoCoder.toAddress(coord[0],
						coord[1]));
				lastCheck = new Date();
			} catch (Exception ex) {
				return new double[] { 0.0, 0.0 };
			}
		}
		return cacheController.getUserCoordinates();
	}

	private static double[] findLocation() {
		GPSTracker gps = new GPSTracker(ContextProvider.get());
		double[] coordinate = new double[2];
		if (gps.canGetLocation()) {
			coordinate[0] = gps.getLatitude();
			coordinate[1] = gps.getLongitude();
			return coordinate;
		} else {
			return new double[] { 0.0, 0.0 };
		}
	}
}
