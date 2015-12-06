package es.udc.riws.moviesearcher.lucene;

public class ConstantesLucene {

	// Campos del índice de Lucene
	
	public static String title 				= "title";
	public static String original_title 	= "originalTitle";
	public static String description 		= "description";
	public static String poster		 		= "poster";
	public static String year 				= "year";
	public static String voteAverage		= "voteAverage";
	public static String releaseDate		= "releaseDate";
	public static String runtime			= "runtime";
	public static String genres				= "genres";
	
	public static String cast				= "cast";
	public static String directors			= "directors";
	public static String writers			= "writers";
	
	public static String score				= "score";
	
	// Directorio donde se almacenan los índices
	public static String directory			= "lucene/";
	
	public static String tokenize			= "+|+";

	
}
