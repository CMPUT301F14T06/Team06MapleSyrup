package ca.ualberta.app.network;


public class SearchCommand {

	private String query = null;
	private String[] fields = null;
	private String sortOption = null;
	
	public SearchCommand(String query) {
		this.query = query;
	}

	public SearchCommand(String query, String[] fields) {
		this.query = query;
		this.fields = fields;
	}
	
	public SearchCommand(String query, String SortOption) {
		this.query = query;
		this.sortOption = SortOption;
	}
	
	public SearchCommand(String query, String[] fields, String SortOption) {
		this.query = query;
		this.fields = fields;
		this.sortOption = SortOption;
	}
	
	public String getJsonCommand() {
		StringBuffer command = new StringBuffer(
				"{\"query\" : {\"query_string\" : {\"query\" : \"" + query
						+ "\"");

		if (fields != null) {
			command.append(", \"fields\":  [");

			for (int i = 0; i < fields.length; i++) {
				command.append("\"" + fields[i] + "\", ");
			}
			command.delete(command.length() - 2, command.length());

			command.append("]");
		}
		//{ "query": {"query_string": {"query": "*"}}, "sort": {"upvoteCount":{"order": "desc"}}}
		if (sortOption == "q_upvote"){
			command.append("}}, \"sort\":  {\"upvoteCount\" : {\"order\" : \"desc\"");
		}
		command.append("}}}");

		return command.toString();
	}
}
