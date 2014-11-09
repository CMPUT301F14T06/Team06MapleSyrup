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

package ca.ualberta.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;

public class Answer extends InputsModel {
	ArrayList<Reply> replyList;
	long upvoteCount_answer;
	long ID_answer;

	public Answer(String content, String userName, Bitmap image) {
		super(content, userName, image);
		this.replyList = new ArrayList<Reply>();
		this.ID_answer = new Date().getTime() - 100;
		this.upvoteCount_answer = 0;
	}

	public void addReply(Reply newReply) {
		this.replyList.add(newReply);
	}

	public List<Reply> getReplyList() {
		return this.replyList;
	}

	public ArrayList<Reply> getReplyArrayList() {
		return this.replyList;
	}

	public int getReplyPosition(Reply reply) {
		return this.replyList.indexOf(reply);
	}

	public void upvoteAnswer() {
		this.upvoteCount_answer++;
	}

	public void setUpvoteCount(long newUpvoteCount){
		this.upvoteCount_answer = newUpvoteCount;
	}
	
	public long getAnswerUpvoteCount() {
		return this.upvoteCount_answer;
	}
}
