package es.udc.riws.moviesearcher.rest;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.riws.moviesearcher.api.ApiService;
import es.udc.riws.moviesearcher.model.Movie;

@RestController
public class SearchRestController {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private ApiService apiService;
    
    @RequestMapping("/indexar")
    public List<Movie> index() {
        System.out.println("indexar");
        
        List<Movie> movies = apiService.getMovies();
        
        return movies;
        
    }
    
    @RequestMapping("/generateIndex")
    public boolean generateIndex() {
    	
    	return true;
    }
    
}
