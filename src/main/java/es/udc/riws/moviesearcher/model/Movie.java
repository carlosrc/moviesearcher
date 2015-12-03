package es.udc.riws.moviesearcher.model;

public class Movie {

	// TODO: Añadir campos: persona (director, actores o escritores), géneros, duración, año de estreno, nota
	
	private String title;

	private String description;
	
	public Movie(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
