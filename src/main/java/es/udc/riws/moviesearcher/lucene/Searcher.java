package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.print.attribute.standard.JobOriginatingUserName;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;
import es.udc.riws.moviesearcher.model.Person.TypePerson;

public class Searcher {

	public static List<Movie> buscar(String q, String qTitle, String qDescription, Integer qYear, Integer qYearInit,
			Integer qYearEnd, Float qMinVoteAverage, Integer qRuntime) {
		List<Movie> movies = new ArrayList<Movie>();
 
		// TODO: Configurar Analyzer
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		try {
			directory = FSDirectory.open(folder);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// Añadir campos de búsqueda
			
			// TODO: Buscar por intervalos, por campos, etc

			BooleanQuery booleanQuery = new BooleanQuery();
			// Búsqueda general
			if (q != null && !q.equals("*:*")) {
				// Todos los campos de búsqueda 
				String[] camposBusqueda = new String[] { ConstantesLucene.title, ConstantesLucene.description,
						ConstantesLucene.year, ConstantesLucene.voteAverage, ConstantesLucene.releaseDate,
						ConstantesLucene.runtime, ConstantesLucene.cast, ConstantesLucene.directors,
						ConstantesLucene.writers, ConstantesLucene.genres };

				MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_48, camposBusqueda, analyzer);
				booleanQuery.add(parser.parse(q), Occur.MUST);
			}
			if (qTitle != null && !qTitle.equals("")) {
				booleanQuery.add(new TermQuery(new Term(ConstantesLucene.title, qTitle)), Occur.MUST);
			}
			if (qDescription != null && !qDescription.equals("")) {
				booleanQuery.add(new TermQuery(new Term(ConstantesLucene.description, qDescription)), Occur.MUST);
			}
			if (qYear != null) {
				booleanQuery.add(NumericRangeQuery.newIntRange(ConstantesLucene.year, qYear, qYear, true, true),
						Occur.MUST);
			}
			if ((qYearInit != null && qYearInit > 0) || (qYearEnd != null && qYearEnd > 0)) {
				booleanQuery.add(NumericRangeQuery.newIntRange(ConstantesLucene.year, qYearInit, qYearEnd, true, true),
						Occur.MUST);
			}
			
			if (qMinVoteAverage != null && qMinVoteAverage > 0) {
				booleanQuery.add(NumericRangeQuery.newFloatRange(ConstantesLucene.voteAverage, qMinVoteAverage, null, true, true),
						Occur.MUST);
			}
			
			if (qRuntime != null) {
				// Se aplica una varianza de +-10 minutos
				booleanQuery.add(NumericRangeQuery.newIntRange(ConstantesLucene.runtime, qRuntime - 10, qRuntime + 10, true, true),
						Occur.MUST);
			}
			
			// *****************************

			ScoreDoc[] hits = isearcher.search(booleanQuery, null, 1000).scoreDocs;

			// *** Iterar sobre los resultados ***
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

}
