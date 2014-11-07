package ca.ualberta.app.network.data;

public class SearchCommand {

	private String query = null;
	private String[] fields = null;

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
		command.append("}}}");

		return command.toString();
	}
}
