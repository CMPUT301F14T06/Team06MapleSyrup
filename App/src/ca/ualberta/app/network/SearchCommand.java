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

	// "from" : 0, "size" : 10,

	public String getJsonCommand() {
		StringBuffer command = new StringBuffer(
				"{\"from\" : 0, \"size\" : 1000, \"query\" : {\"query_string\" : {\"query\" : \""
						+ query + "\"");

		if (fields != null) {
			command.append(", \"fields\":  [");

			for (int i = 0; i < fields.length; i++) {
				command.append("\"" + fields[i] + "\", ");
			}
			command.delete(command.length() - 2, command.length());

			command.append("]");
		}
		if (sortOption == "date") {
			command.append("}}, \"sort\":  {\"ID_question\" : {\"order\" : \"desc\"");
		}
		if (sortOption == "score") {
			command.append("}}, \"sort\":  {\"total_score\" : {\"order\" : \"desc\"");
		}
		if (sortOption == "q_upvote") {
			command.append("}}, \"sort\":  {\"upvoteCount_question\" : {\"order\" : \"desc\"");
		}
		if (sortOption == "a_upvote") {
			command.append("}}, \"sort\":  {\"upvoteCount_answer\" : {\"order\" : \"desc\"");
		}

		command.append("}}}");

		return command.toString();
	}
}
