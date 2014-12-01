package ca.ualberta.app.gps;

import java.util.List;
import java.util.Locale;
import ca.ualberta.app.models.ContextProvider;
import android.location.Address;
import android.location.Geocoder;

public class GeoCoder {
	/**
	 * transfer the coordinate to actual address
	 * 
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @return the transfer address
	 */
	public static String toAddress(double latitude, double longitude) {
		String myAddress;
		Geocoder geocoder = new Geocoder(ContextProvider.get(),
				Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if (addresses != null && addresses.size() > 0) {
				Address returnedAddress = addresses.get(0);
				myAddress = formatAddress(returnedAddress);
			} else {
				myAddress = "";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			myAddress = "";
		}
		return myAddress;
	}

	/**
	 * Format the address
	 * 
	 * @param address
	 *            the address that need to be formated
	 * @return formated address
	 */
	private static String formatAddress(Address address) {
		StringBuilder stringBuilder = new StringBuilder();
		String location = "";
		location = address.getLocality();
		stringBuilder.append(location != null ? (location + ", ")
				: (address.getSubAdminArea() != null) ? (address
						.getSubAdminArea() + ", ") : "");
		location = address.getAdminArea();
		stringBuilder.append(location != null ? (location + ", ") : "");
		stringBuilder.append(location != null ? address.getCountryName()
				: address.getCountryCode());
		return stringBuilder.toString();
	}

	/**
	 * transfer an address to coordinates
	 * 
	 * @param strAddress
	 *            the address that need to be transfered
	 * @return the transfered coordinates
	 */
	public static double[] toLatLong(String strAddress) {
		Geocoder geocoder = new Geocoder(ContextProvider.get(),
				Locale.getDefault());
		List<Address> addresses;
		double[] returnLatLong = new double[2];
		try {
			addresses = geocoder.getFromLocationName(strAddress, 1);
			if (addresses != null && addresses.size() > 0) {
				Address returnedAddress = addresses.get(0);
				returnLatLong[0] = returnedAddress.getLatitude();
				returnLatLong[1] = returnedAddress.getLongitude();
			}
		} catch (Exception e) {
			return new double[] { 0.0, 0.0 };
		}
		return returnLatLong;
	}

	/**
	 * calculate the distance between two coordinates
	 * 
	 * @param coordinate1
	 *            the coordinate
	 * @param coordinate2
	 *            the coordinate
	 * @return the distance between the coordinates
	 */
	public static double toFindDistance(double[] coordinate1,
			double[] coordinate2) {
		double d = 1000000000;
		if (coordinate1 != null && coordinate2 != null) {
			double R = 6371000; // m
			double dLat = Math.toRadians(coordinate1[0] - coordinate2[0]);
			double dLon = Math.toRadians(coordinate1[1] - coordinate2[1]);
			double lat1 = Math.toRadians(coordinate1[0]);
			double lat2 = Math.toRadians(coordinate2[0]);

			double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
					+ Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1)
					* Math.cos(lat2);
			double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			d = R * c;
		}

		return d;
	}

	/**
	 * transfer coordinates to string
	 * 
	 * @param coord
	 *            the coordinate
	 * @return the transfered String
	 */
	public static String coordinatesToString(double[] coord) {
		try {
			return String.valueOf(coord[0]) + ";" + String.valueOf(coord[1]);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * transfer the String to coordinates
	 * 
	 * @param coord
	 *            the String
	 * @return the transfered coordinates
	 */
	public static double[] coordinatesFromString(String coord) {
		double[] dcoord = new double[2];
		dcoord[0] = Double.valueOf((coord.split(";")[0]));
		dcoord[1] = Double.valueOf((coord.split(";")[1]));
		return dcoord;
	}
}
