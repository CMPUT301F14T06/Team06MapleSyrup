package ca.ualberta.app.activity.test;

import ca.ualberta.app.controller.PushController;
import junit.framework.TestCase;

public class PushControllerTest extends TestCase {
	public void testPush(){
		PushController pc = new PushController();
		assertTrue("pc should not be null",pc != null);
	}
}
