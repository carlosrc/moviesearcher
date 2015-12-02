package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;

public class Searcher {
	
	public List<Movie> buscar(String q) {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		// Now search the index:
		File directory = new File(ConstantesLucene.directory);
	    DirectoryReader ireader = DirectoryReader.open(directory);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    // Parse a simple query that searches for "text":
	    QueryParser parser = new QueryParser(Version.LUCENE_48, ConstantesLucene.title, analyzer);
	    Query query = parser.parse(q);
	    ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	    List<Movie> movies = new ArrayList<Movie>();
	    // Iterate through the results:
	    for (int i = 0; i < hits.length; i++) {
	      Document hitDoc = isearcher.doc(hits[i].doc);
	      Movie movie = new Movie(hitDoc.get(ConstantesLucene.title),"");
	      movies.add(movie);
	    }
	    ireader.close();
	    directory.close();
	    return movies;
	}
	

}
