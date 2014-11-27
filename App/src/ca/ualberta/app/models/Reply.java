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

import java.util.Date;

/**
 * This is the interface of reply
 */
public class Reply extends InputsModel {

	private long questionID_Reply;
	private String questionTitle_Reply;
	private long ID_Reply;
	private long answerID_Reply;
	/**
	 * The constructor of the class
	 * 
	 * @param content
	 *            The content of a reply.
	 * @param userName
	 *            The user name of the author of the reply.
	 */
	public Reply(String content, String userName) {
		super(content, userName);
		this.questionID_Reply = 0;
		this.answerID_Reply = 0;
		this.questionTitle_Reply = null;
		this.ID_Reply = new Date().getTime() - 130;
	}

	public long getID(){
		return this.ID_Reply;
	}
	
	public void setQuestionID(long questionId) {
		this.questionID_Reply = questionId;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle_Reply = questionTitle;		
	}
	
	public void setAnswerID(long answerID){
		this.answerID_Reply = answerID;
	}
	
	public long getQuestionID(){
		return this.questionID_Reply;
	}
	
	public String getQuestionTitle(){
		return this.questionTitle_Reply;
	}
	
	public long getAnswerID(){
		return this.answerID_Reply;
	}
	

}
