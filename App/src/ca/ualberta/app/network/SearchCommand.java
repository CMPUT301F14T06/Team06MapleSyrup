package ca.ualberta.app.network;


public class SearchCommand {

	private String query;

	public SearchCommand(String query) {
		this.query = query;
	}
	
	public String getJsonCommand() {
		StringBuffer command = new StringBuffer(
				"{\"query\" : {\"query_string\" : {\"query\" : \"" + query
						+ "\"");
		
		command.append("}}}");

		return command.toString();
	}
}
