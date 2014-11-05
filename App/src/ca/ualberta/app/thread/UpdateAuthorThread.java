package ca.ualberta.app.thread;

import ca.ualberta.app.ESmanager.AuthorMapManager;
import ca.ualberta.app.models.Author;

public class UpdateAuthorThread extends Thread {
	private Author author;
	private AuthorMapManager authorMapManager;
	
	public UpdateAuthorThread(Author author) {
		this.author = author;
		this.authorMapManager = new AuthorMapManager();
	}

	@Override
	public void run() {
		authorMapManager.updateAuthor(author);

		// Give some time to get updated info
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
