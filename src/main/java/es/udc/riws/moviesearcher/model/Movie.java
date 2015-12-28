package es.udc.riws.moviesearcher.model;

import java.util.List;

public class Movie {

	private Long id;

	private String title;

	private String description;

	private String urlPoster;

	private Float voteAverage;

	private Integer year;

	private Integer runtime;

	private List<Person> people;

	private List<String> genres;

	public Movie(Long id, String title, String description, String urlPoster, Float voteAverage, Integer year,
			Integer runtime, List<Person> people, List<String> genres) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.urlPoster = urlPoster;
		this.voteAverage = voteAverage;
		this.runtime = runtime;
		this.people = people;
		this.genres = genres;
		this.year = year;
	}

	public Movie(Long id, String title, String description, String urlPoster, Float voteAverage, Integer year,
			Integer runtime, List<Person> people, List<String> genres, Float score) {
		this(id, title, description, urlPoster, voteAverage, year, runtime, people, genres);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((runtime == null) ? 0 : runtime.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (runtime == null) {
			if (other.runtime != null)
				return false;
		} else if (!runtime.equals(other.runtime))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

}
