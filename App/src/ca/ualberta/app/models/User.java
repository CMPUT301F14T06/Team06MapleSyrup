package ca.ualberta.app.models;

import java.util.HashMap;
import java.util.Map;

public class User {

	public static boolean loginStatus = false;
	public static Map<Long, Question> favoriteId = new HashMap<Long, Question>();
	public static Map<Long, Question> localCacheId = new HashMap<Long, Question>();;
	public static Author author = null;

}
