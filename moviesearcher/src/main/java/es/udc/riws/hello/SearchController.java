package es.udc.riws.hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.riws.moviesearcher.api.ApiService;

@RestController
public class SearchController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private ApiService apiService;
    
    @RequestMapping("/indexar")
    public boolean index() {
        System.out.println("indexar");
        
        apiService.getMovies();
        
        return true;
        
    }
    
    @RequestMapping("/generateIndex")
    public boolean generateIndex() {
    	
    	return true;
    }
    
    @RequestMapping("/search")
    public Greeting query(@RequestParam(value="q", defaultValue="") String name) {
    	
//    	try {
//			MongoClient mongo = new MongoClient( "localhost" , 27017 );
//			DB db = mongo.getDB("information");
//			
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
    	
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
