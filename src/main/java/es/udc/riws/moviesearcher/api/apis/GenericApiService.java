package es.udc.riws.moviesearcher.api.apis;

import java.util.List;

import es.udc.riws.moviesearcher.model.Movie;

/**
 * Servicio que deben de implementar el resto de servicios de este paquete.
 * 
 * @author Carlos
 *
 */
public interface GenericApiService {

	public List<Movie> getMovies();

}
