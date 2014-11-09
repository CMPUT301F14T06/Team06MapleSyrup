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
 * A Comparator which compare and sort 2 Question objects by time posted.
 */
public class DateComparator implements Comparator<Question> {
	/**
	 * A method which compares the difference between 2 Question's post time
	 * while sorting.
	 * 
	 * @param a
	 *            left hand side Comment object.
	 * @param b
	 *            right hand side Comment object.
	 * @return -1 if left hand side Question is posted later than right hand side
	 *         		Question 
	 *           1 otherwise
	 */
	
	@Override
	public int compare(Question a, Question b) {
		if (a.getID() >= b.getID()) {
			
			return -1;
			
		} else {
			
			return 1;
			
		}
	}

}
