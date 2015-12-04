package es.udc.riws.moviesearcher.model;

import java.util.ArrayList;
import java.util.List;

public class Movie {

	// TODO: Añadir campos: persona (director, actores o escritores), géneros,
	// duración, año de estreno, nota

	private String title;

	private String description;

	private String urlPoster;

	private Float voteAverage;

	private List<String> genres = new ArrayList<String>();

	public Movie(String title, String description, String urlPoster, Float voteAverage, List<String> genres) {
		super();
		this.title = title;
		this.description = description;
		this.urlPoster = urlPoster;
		this.voteAverage = voteAverage;
		this.genres = genres;
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

	public String getUrlPoster() {
		return urlPoster;
	}

	public void setUrlPoster(String urlPoster) {
		this.urlPoster = urlPoster;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage(Float voteAverage) {
		this.voteAverage = voteAverage;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

}
