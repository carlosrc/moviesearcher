package es.udc.riws.moviesearcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.udc.riws.moviesearcher.api.ApiService;

@Controller
public class PruebaController {

	@Autowired
	private ApiService apiService;

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String homePage() {
//		return "redirect:search";
//	}
//	
//	@RequestMapping(value = "/generateIndex", method = RequestMethod.GET)
//	public String generateIndex() {
//
//		apiService.getMovies();
//
//		return null;
//	}
//
//	
	@RequestMapping(value = "/buscar", method = RequestMethod.GET)
	public String search() {

		apiService.getMovies();

		return null;
	}

}
