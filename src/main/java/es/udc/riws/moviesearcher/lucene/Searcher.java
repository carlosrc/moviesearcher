package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;

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
			QueryParser parser = new QueryParser(Version.LUCENE_48, ConstantesLucene.title, analyzer);
			Query query = parser.parse(q);
			
			ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

			// Iterar sobre los resultados
			for (int i = 0; i < hits.length; i++) {
				System.out.println("Movie_" + i);
				Document hitDoc = isearcher.doc(hits[i].doc);
				Movie movie = new Movie(hitDoc.get(ConstantesLucene.title), hitDoc.get(ConstantesLucene.description));
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
