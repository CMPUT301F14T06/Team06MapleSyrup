/*
 * Copyright 2014 Anni Dai
 * Copyright 2014 Bicheng Yan
 * Copyright 2014 Liwen Chen
 * Copyright 2014 Liang Jingjing
 * Copyright 2014 Xiaocong Zhou
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
