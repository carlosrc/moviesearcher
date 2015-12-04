package es.udc.riws.moviesearcher.model;

import java.util.List;

public class Movie {

	// TODO: Añadir campos: persona (director, actores o escritores), géneros,
	// duración, año de estreno, nota

	private String title;

	private String description;

	private String urlPoster;

	private Float voteAverage;

	private String releaseDate;

	private int runtime;

	private List<Person> people;

	private List<String> genres;

	// Puntuación calculada por Lucene de la similitud de la búsqueda con la
	// película en cuestión
	// TODO: Normalizar resultado del 0 al 1 en función del máximo score
	private Float score;

	public Movie(String title, String description, String urlPoster, Float voteAverage, String releaseDate, int runtime,
			List<Person> people, List<String> genres, Float score) {
		super();
		this.title = title;
		this.description = description;
		this.urlPoster = urlPoster;
		this.voteAverage = voteAverage;
		this.runtime = runtime;
		this.people = people;
		this.genres = genres;
		this.releaseDate = releaseDate;

		this.score = score;
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

	public List<Person> getPeople() {
		return people;
	}

	public void setPeople(List<Person> people) {
		this.people = people;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public int getRuntime() {
		return runtime;
	}

	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

}
