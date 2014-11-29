package ca.ualberta.app.test.FunctionalityTest;

import ca.ualberta.app.controller.CacheController;
import ca.ualberta.app.gps.Location;
import ca.ualberta.app.models.ContextProvider;
import junit.framework.TestCase;

public class CoordinatesTest extends TestCase {
	public void testGetUserCoordinates(){
		double[] location = Location.getLocationCoordinates();
		CacheController cacheController = new CacheController(ContextProvider.get());
		assertEquals(cacheController.getUserCoordinates()[0],location[0]);
		assertEquals(cacheController.getUserCoordinates()[1], location[1]);
	}
}
