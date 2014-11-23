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

package ca.ualberta.app.comparator;

import java.util.Comparator;
import ca.ualberta.app.models.Question;

/**
 * A comparator class which compares 2 Question objects and sort them by
 * picture.
 */
public class PictureComparator implements Comparator<Question> {
	/**
	 * @param a
	 *            : left hand side Comment
	 * @param b
	 *            : right hand side Comment
	 * @return -1 if right hand side Comment's picture is null but left hand
	 *         side Comment's picture is not null, 1 if right hand side
	 *         Comment's picture is not null but left hand side Comment's
	 *         picture is null, 0 otherwise.
	 */
	@Override
	public int compare(Question a, Question b) {
		if ((a.hasImage() == true) && (b.hasImage() == false)) {
			return -1;
		} else if ((a.hasImage() == false) && (b.hasImage() == true)) {
			return 1;
		} else if ((a.hasImage() == false) && (b.hasImage() == false)) {
			return 0;
		} else if ((a.hasImage() == true) && (b.hasImage() == true)) {
			return 0;
		}else {
			return -1;
		}
	}

}