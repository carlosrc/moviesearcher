package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;

public class Indexer {

	public static void indexar(List<Movie> movies) {
		
		// TODO: Configurar Analyzer
		Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_48);

		File folder = new File(ConstantesLucene.directory);
		// FIXME: Borrar directorio antes de indexar
		if (folder.exists()) {
			boolean exito = folder.delete();
			
		}
		Directory directory;
				
		try {
			directory = FSDirectory.open(folder);

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_48, analyzer);
			IndexWriter iwriter = new IndexWriter(directory, config);

			// Cada película, un documento
			for (Movie movie : movies) {
				iwriter.addDocument(addMovie(movie));
			}
			iwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Método para indexar una película
	private static Document addMovie(Movie movie) {
		Document doc = new Document();
		// TODO: Añadir campos 
		doc.add(new TextField(ConstantesLucene.title, movie.getTitle(), Field.Store.YES));
		doc.add(new TextField(ConstantesLucene.description, movie.getDescription(), Field.Store.YES));
		return doc;
	}

}
