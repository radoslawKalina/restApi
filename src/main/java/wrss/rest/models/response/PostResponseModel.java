package wrss.rest.models.response;

import org.springframework.hateoas.RepresentationModel;

public class PostResponseModel extends RepresentationModel {
	
	private int id;
	private String title;
	private String message;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}	

}
