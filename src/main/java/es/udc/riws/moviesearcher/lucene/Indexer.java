package es.udc.riws.moviesearcher.lucene;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;

public class Indexer {

	
	public void indexar(List<Movie> movies) {
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);

	    // Store the index in memory:
	    Directory directory = new RAMDirectory();
	}
	
}
