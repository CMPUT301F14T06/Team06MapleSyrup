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
