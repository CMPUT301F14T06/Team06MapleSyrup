package ca.ualberta.app.comparator;

import java.util.Comparator;

import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.gps.GeoCoder;
import ca.ualberta.app.models.ContextProvider;
import ca.ualberta.app.models.Question;

public class DistanceComparator implements Comparator<Question> {
	private CacheController cacheController = new CacheController(
			ContextProvider.get());
	private double[] UserCoordinates = cacheController.getUserCoordinates();

	@Override
	public int compare(Question a, Question b) {
		if (GeoCoder
				.toFindDistance(UserCoordinates, a.getLocationCoordinates()) >= GeoCoder
				.toFindDistance(UserCoordinates, b.getLocationCoordinates())) {
			return 1;
		}
		else{
			return -1;
		}
	}
}
