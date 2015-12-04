package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
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

		Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_48);
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		try {
			directory = FSDirectory.open(folder);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// TODO: Añadir el resto de campos a la búsqueda
			String[] camposBusqueda = new String[] { ConstantesLucene.title, ConstantesLucene.description,
					ConstantesLucene.duration, ConstantesLucene.year, ConstantesLucene.voteAverage,
					ConstantesLucene.releaseDate, ConstantesLucene.runtime, ConstantesLucene.genres };
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

					String[] campos = personString.split(" | ");
					if (campos != null && campos.length == 3) {
						int orden = Integer.valueOf(campos[2]);

						people.add(new Person(campos[0], campos[1], orden, TypePerson.CAST));
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

}
