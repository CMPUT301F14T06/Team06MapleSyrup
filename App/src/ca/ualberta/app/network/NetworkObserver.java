package ca.ualberta.app.network;

import java.util.Timer;
import java.util.TimerTask;

import ca.ualberta.app.activity.FragmentMain;
import ca.ualberta.app.activity.WaitingListActivity;

public class NetworkObserver {
	private Boolean observationStarted = null;

	/**
	 * Will be called when the Internet is not connected, start check if the
	 * Internet connects each 5 seconds, if the Internet is connected, then stop
	 * checking and call refresh method in the Activity.
	 * 
	 * @param activity
	 *            : FragmentMain where the function will be called.
	 */
	public void startObservation(final FragmentMain activity) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (InternetConnectionChecker
								.isNetworkAvailable(activity.getActivity())) {
							timer.cancel();
							timer.purge();
							Runnable action = new Runnable() {
								@Override
								public void run() {
									activity.updateList();
								}
							};
							activity.getActivity().runOnUiThread(action);
						}
					}
				}, 5000, 5000);
			}
		};
		if (observationStarted == null || !observationStarted) {
			thread.start();
			observationStarted = true;
		}
	}

	/**
	 * Will be called when the Internet is connected, start check if the
	 * Internet goes off each 5 seconds, if the Internet goes off, then stop
	 * checking and call refresh method in the Activity.
	 * 
	 * @param activity
	 *            : FragmentMain where the function will be called.
	 */
	public void setObserver(final FragmentMain activity) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (InternetConnectionChecker
								.isNetworkAvailable(activity.getActivity()) == false) {
							timer.cancel();
							timer.purge();
							Runnable action = new Runnable() {
								@Override
								public void run() {
									activity.updateList();
								}
							};
							activity.getActivity().runOnUiThread(action);
						}
					}
				}, 5000, 5000);
			}
		};
		if (observationStarted == null || observationStarted) {
			thread.start();
			observationStarted = false;
		}
	}
	
	/**
	 * Will be called when the Internet is not connected, start check if the
	 * Internet connects each 5 seconds, if the Internet is connected, then stop
	 * checking and call refresh method in the Activity.
	 * 
	 * @param activity
	 *            : WaitingListActivity where the function will be called.
	 */
	public void startObservation(final WaitingListActivity activity) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (InternetConnectionChecker
								.isNetworkAvailable(activity)) {
							timer.cancel();
							timer.purge();
							Runnable action = new Runnable() {
								@Override
								public void run() {
									activity.updateList();
								}
							};
							activity.runOnUiThread(action);
						}
					}
				}, 5000, 5000);
			}
		};
		if (observationStarted == null || !observationStarted) {
			thread.start();
			observationStarted = true;
		}
	}

	/**
	 * Will be called when the Internet is connected, start check if the
	 * Internet goes off each 5 seconds, if the Internet goes off, then stop
	 * checking and call refresh method in the Activity.
	 * 
	 * @param activity
	 *            : WaitingListActivity where the function will be called.
	 */
	public void setObserver(final WaitingListActivity activity) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				final Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if (InternetConnectionChecker
								.isNetworkAvailable(activity) == false) {
							timer.cancel();
							timer.purge();
							Runnable action = new Runnable() {
								@Override
								public void run() {
									activity.updateList();
								}
							};
							activity.runOnUiThread(action);
						}
					}
				}, 5000, 5000);
			}
		};
		if (observationStarted == null || observationStarted) {
			thread.start();
			observationStarted = false;
		}
	}
}
