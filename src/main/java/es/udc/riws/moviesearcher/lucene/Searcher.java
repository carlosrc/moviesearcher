package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;
import es.udc.riws.moviesearcher.model.Person.TypePerson;

public class Searcher {

	public static List<Movie> buscar(String q) {
		List<Movie> movies = new ArrayList<Movie>();

		// TODO: Configurar Analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		try {
			directory = FSDirectory.open(folder);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// TODO: Añadir el resto de campos a la búsqueda
			String[] camposBusqueda = new String[] { ConstantesLucene.title, ConstantesLucene.description,
					ConstantesLucene.duration, ConstantesLucene.year, ConstantesLucene.voteAverage,
					ConstantesLucene.releaseDate, ConstantesLucene.runtime, ConstantesLucene.cast,
					ConstantesLucene.directors, ConstantesLucene.writers, ConstantesLucene.genres };
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_48, camposBusqueda, analyzer);
			Query query = parser.parse(q);

			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

			// Iterar sobre los resultados
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);

				ScoreDoc score = hits[i];

				// Pasamos el string voteAverage a Float
				String voteAverageString = hitDoc.get(ConstantesLucene.voteAverage);
				Float voteAverage = null;
				if (voteAverageString != null)
					voteAverage = Float.parseFloat(voteAverageString);

				// Actores
				List<Person> people = new ArrayList<Person>();
				for (String personString : hitDoc.getValues(ConstantesLucene.cast)) {

					String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
					if (campos != null && campos.length == 3) {
						Integer orden = null;
						if (campos[2] != null && !campos[2].equals("null")) {
							orden = Integer.valueOf(campos[2]);
						}
						people.add(new Person(campos[0], campos[1], orden, TypePerson.CAST));
					}
				}

				// Directores
				for (String personString : hitDoc.getValues(ConstantesLucene.directors)) {
					String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
					if (campos != null && campos.length > 0) {
						people.add(new Person(campos[0], null, null, TypePerson.DIRECTOR));
					}
				}

				// Escritores
				for (String personString : hitDoc.getValues(ConstantesLucene.writers)) {
					String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
					if (campos != null && campos.length > 0) {
						people.add(new Person(campos[0], null, null, TypePerson.WRITER));
					}
				}

				// Duración
				int runtime = Integer.valueOf(hitDoc.get(ConstantesLucene.runtime));

				// Creamos el objeto película
				Movie movie = new Movie(hitDoc.get(ConstantesLucene.title), hitDoc.get(ConstantesLucene.description),
						hitDoc.get(ConstantesLucene.poster), voteAverage, hitDoc.get(ConstantesLucene.releaseDate),
						runtime, people, Arrays.asList(hitDoc.getValues(ConstantesLucene.genres)), score.score);

				// Añadimos la película buscada a la lista de películas
				movies.add(movie);
			}
			ireader.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return movies;
	}
	
/*	// Filtramos películas por anho de estreno
	public static List<Movie> filtrar(String q1, String q2){
		List<Movie> movies = new ArrayList<Movie>();
		
		// TODO: Configurar Analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		
		// Pasamos los anhos a entero para compararlos
		int q1int = Integer.parseInt(q1);
		int q2int = Integer.parseInt(q2);
		
		// Comprobamos que el primer anho es menor que el segundo
		if (q1int < q2int){
		    System.out.println("q1int es " + q1int + " q2int es " + q2int);
		    
		    // Recorremos todos los años que hay entre los dos años
		    for(int x = q1int; x<=q2int; x++){
		    	
		    	try {
					directory = FSDirectory.open(folder);
					DirectoryReader ireader = DirectoryReader.open(directory);
					IndexSearcher isearcher = new IndexSearcher(ireader);
					
					// Buscar por fecha de estreno y luego parsear para pillar el año
					// String[] camposBusqueda = new String[] { ConstantesLucene.releaseDate}; 
					String[] camposBusqueda = new String[] { ConstantesLucene.year, ConstantesLucene.year };
					
					MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_48, camposBusqueda, analyzer);
					
					// Pasamos el entero x del FOR a String para la query, ya que se trata de un anho
					String xString = Integer.toString(x);
					Query query = parser.parse(xString);
					
					ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
					
					// Iterar sobre los resultados
					for (int i = 0; i < hits.length; i++) {
						Document hitDoc = isearcher.doc(hits[i].doc);

						ScoreDoc score = hits[i];
						
						// String anhoEstreno = String.valueOf(hitDoc.get(ConstantesLucene.releaseDate)).substring(6,10);
						
						// Pasamos el string voteAverage a Float
						String voteAverageString = hitDoc.get(ConstantesLucene.voteAverage);
						Float voteAverage = null;
						if (voteAverageString != null)
							voteAverage = Float.parseFloat(voteAverageString);

						// Actores
						List<Person> people = new ArrayList<Person>();
						for (String personString : hitDoc.getValues(ConstantesLucene.cast)) {

							String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
							if (campos != null && campos.length == 3) {
								Integer orden = null;
								if (campos[2] != null && !campos[2].equals("null")) {
									orden = Integer.valueOf(campos[2]);
								}
								people.add(new Person(campos[0], campos[1], orden, TypePerson.CAST));
							}
						}

						// Directores
						for (String personString : hitDoc.getValues(ConstantesLucene.directors)) {
							String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
							if (campos != null && campos.length > 0) {
								people.add(new Person(campos[0], null, null, TypePerson.DIRECTOR));
							}
						}

						// Escritores
						for (String personString : hitDoc.getValues(ConstantesLucene.writers)) {
							String[] campos = personString.split(Pattern.quote(ConstantesLucene.tokenize));
							if (campos != null && campos.length > 0) {
								people.add(new Person(campos[0], null, null, TypePerson.WRITER));
							}
						}

						// Duración
						int runtime = Integer.valueOf(hitDoc.get(ConstantesLucene.runtime));

						// Creamos el objeto película
						Movie movie = new Movie(hitDoc.get(ConstantesLucene.title), hitDoc.get(ConstantesLucene.description),
								hitDoc.get(ConstantesLucene.poster), voteAverage, hitDoc.get(ConstantesLucene.releaseDate),
								runtime, people, Arrays.asList(hitDoc.getValues(ConstantesLucene.genres)), score.score);

						// Añadimos la película buscada a la lista de películas
						movies.add(movie);
					}
					ireader.close();
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}				   	
		    }
		}
	    return movies; 	
	}*/

}
