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

package ca.ualberta.app.network.data;

public class SearchCommand {

	private String query = null;
	private String[] fields = null;
	private long from;
	private long size;
	private String lable = "question";
	
	public SearchCommand(String query) {
		this.query = query;
	}

	public SearchCommand(String query, String[] fields) {
		this.query = query;
		this.fields = fields;
	}

	public SearchCommand(String query, String[] fields, String SortOption) {
		this.query = query;
		this.fields = fields;
	}

	public SearchCommand(String query, long from, long size){
		this.query = query;
		this.from = from;
		this.size = size;
	}
	
	public SearchCommand(String query, long from, long size,
			String lable) {
		this.query = query;
		this.from = from;
		this.size = size;
		this.lable  = lable;
	}

	public String getJsonCommand() {
		StringBuffer command = new StringBuffer(
				"{\"from\" : " + from + ", \"size\" : " + size + ", \"query\" : {\"query_string\" : {\"query\" : \""
						+ query + "\"");

		if (fields != null) {
			command.append(", \"fields\":  [");

			for (int i = 0; i < fields.length; i++) {
				command.append("\"" + fields[i] + "\", ");
			}
			command.delete(command.length() - 2, command.length());

			command.append("]");
		}
		if (lable == "question"){
			command.append("}}, \"sort\": {\"ID_question\" : {\"order\" : \"desc\"");
		}
		
		command.append("}}}");

		return command.toString();
	}
}
