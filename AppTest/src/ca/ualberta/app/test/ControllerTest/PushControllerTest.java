package ca.ualberta.app.test.ControllerTest;

import android.test.AndroidTestCase;
import ca.ualberta.app.controller.PushController;

public class PushControllerTest extends AndroidTestCase {
	public void testPush(){
		PushController pc = new PushController(getContext());
		assertTrue("pc should not be null",pc != null);
	}
}
