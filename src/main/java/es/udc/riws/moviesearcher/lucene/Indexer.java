package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;

public class Indexer {

	private static FieldType mltType;

	public static void index(List<Movie> movies) {

		Analyzer analyzer = ConstantesLucene.getAnalyzer();

		File folder = new File(ConstantesLucene.directory);
		// Borrar directorio antes de generar un nuevo índice
		if (folder.exists()) {
			try {
				FileUtils.deleteDirectory(folder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Directory directory;

		try {
			directory = FSDirectory.open(folder);

			IndexWriterConfig config = new IndexWriterConfig(ConstantesLucene.version, analyzer);
			IndexWriter iwriter = new IndexWriter(directory, config);

			// Tipo propio para la búsqueda con MoreLikeThis
			mltType = new FieldType();
			mltType.setIndexed(true);
			mltType.setStored(false);
			mltType.setTokenized(false);
			mltType.setStoreTermVectors(true);
			mltType.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);

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
		if (movie.getId() == null) {
			return doc;
		}
		if (movie.getTitle() == null) {
			return doc;
		}

		doc.add(new LongField(ConstantesLucene.id, movie.getId(), Field.Store.YES));

		doc.add(new TextField(ConstantesLucene.title, movie.getTitle(), Field.Store.YES));

		if (movie.getDescription() != null) {
			doc.add(new TextField(ConstantesLucene.description, movie.getDescription(), Field.Store.YES));
		}
		if (movie.getUrlPoster() != null) {
			// El póster no se indexa, sólo se almacena
			doc.add(new StoredField(ConstantesLucene.poster, movie.getUrlPoster()));
		}
		if (movie.getVoteAverage() != null) {
			doc.add(new FloatField(ConstantesLucene.voteAverage, movie.getVoteAverage(), Field.Store.YES));
		}

		if (movie.getYear() != null) {
			doc.add(new IntField(ConstantesLucene.year, movie.getYear(), Field.Store.YES));
		}

		if (movie.getRuntime() != null) {
			doc.add(new IntField(ConstantesLucene.runtime, movie.getRuntime(), Field.Store.YES));
		}

		for (Person person : movie.getPeople()) {
			String personToIndex = person.getName();
			switch (person.getType()) {
			case CAST:
				doc.add(new TextField(ConstantesLucene.cast, personToIndex, Field.Store.YES));
				break;
			case DIRECTOR:
				doc.add(new TextField(ConstantesLucene.directors, personToIndex, Field.Store.YES));
				doc.add(new Field(ConstantesLucene.directorsLiteral, personToIndex, mltType));
				break;
			default:
				break;
			}
		}

		for (String genre : movie.getGenres()) {
			doc.add(new TextField(ConstantesLucene.genres, genre, Field.Store.YES));
		}

		return doc;
	}

}
