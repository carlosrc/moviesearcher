package es.udc.riws.moviesearcher.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import es.udc.riws.moviesearcher.model.Movie;
import es.udc.riws.moviesearcher.model.Person;
import es.udc.riws.moviesearcher.model.Person.TypePerson;

public class Searcher {

	public static List<Movie> search(String q, String qTitle, String qDescription, Integer qYearInit, Integer qYearEnd,
			Float qMinVoteAverage, Integer qRuntime, String[] qGenres, String[] qCast, String[] qDirectors,
			Boolean strict) {
		List<Movie> movies = new ArrayList<Movie>();

		// Ocurr indica si la búsqueda es estricta o no
		Occur ocurr = Occur.SHOULD;
		if (strict != null && strict) {
			ocurr = Occur.MUST;
		}

		Analyzer analyzer = ConstantesLucene.getAnalyzer();
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		try {
			directory = FSDirectory.open(folder);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// Campos de búsqueda

			BooleanQuery booleanQuery = new BooleanQuery();
			QueryParser parser = null;
			// Búsqueda general
			if (q != null && !q.equals("")) {
				// Todos los campos de búsqueda
				String[] camposBusqueda = new String[] { ConstantesLucene.title, ConstantesLucene.description,
						ConstantesLucene.year, ConstantesLucene.voteAverage, ConstantesLucene.runtime,
						ConstantesLucene.cast, ConstantesLucene.directors, ConstantesLucene.genres };

				parser = new MultiFieldQueryParser(ConstantesLucene.version, camposBusqueda, analyzer);
				booleanQuery.add(parser.parse(q), ocurr);
			}
			if (qTitle != null && !qTitle.equals("")) {
				if (strict) {
					qTitle = "\"" + qTitle + "\"";
				}
				parser = new QueryParser(ConstantesLucene.version, ConstantesLucene.title, analyzer);
				booleanQuery.add(parser.parse(qTitle), ocurr);
			}
			if (qDescription != null && !qDescription.equals("")) {
				if (strict) {
					qDescription = "\"" + qDescription + "\"";
				}
				parser = new QueryParser(ConstantesLucene.version, ConstantesLucene.description,
						ConstantesLucene.getAnalyzerWithStopWords());
				Query queryDesc = parser.parse(qDescription);
				if (!queryDesc.toString().isEmpty()) {
					booleanQuery.add(queryDesc, ocurr);
				}
			}

			qYearInit = qYearInit != null && qYearInit == 0 ? null : qYearInit;
			qYearEnd = qYearEnd != null && qYearEnd == 0 ? null : qYearEnd;
			if (qYearInit != null || qYearEnd != null) {
				booleanQuery.add(NumericRangeQuery.newIntRange(ConstantesLucene.year, qYearInit, qYearEnd, true, true),
						ocurr);
			}

			if (qMinVoteAverage != null && qMinVoteAverage > 0) {
				booleanQuery.add(NumericRangeQuery.newFloatRange(ConstantesLucene.voteAverage, qMinVoteAverage, null,
						true, true), ocurr);
			}

			if (qRuntime != null) {
				// Se aplica una varianza de +-10 minutos
				booleanQuery.add(NumericRangeQuery.newIntRange(ConstantesLucene.runtime, qRuntime - 10, qRuntime + 10,
						true, true), ocurr);
			}

			if (qGenres != null) {
				for (String qGenre : qGenres) {
					parser = new QueryParser(ConstantesLucene.version, ConstantesLucene.genres, analyzer);
					booleanQuery.add(parser.parse(qGenre), ocurr);
				}
			}

			if (qCast != null) {
				for (String qActor : qCast) {
					parser = new QueryParser(ConstantesLucene.version, ConstantesLucene.cast, analyzer);
					booleanQuery.add(parser.parse("\"" + qActor + "\""), ocurr);
				}
			}

			if (qDirectors != null) {
				for (String qDirector : qDirectors) {
					parser = new QueryParser(ConstantesLucene.version, ConstantesLucene.directors, analyzer);
					booleanQuery.add(parser.parse("\"" + qDirector + "\""), ocurr);
				}
			}

			// Si no existen condiciones, devolvemos todas las películas
			if (booleanQuery.clauses().isEmpty()) {
				booleanQuery.add(NumericRangeQuery.newFloatRange(ConstantesLucene.voteAverage, 0.1F, null, true, true),
						ocurr);
			}

			TopDocs topdocs = isearcher.search(booleanQuery, null, 1000);

			// Procesamos los resultados
			movies = processResults(topdocs.scoreDocs, isearcher);

			ireader.close();
			directory.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return movies;
	}

	public static List<Movie> findSimilar(long id) {
		List<Movie> movies = new ArrayList<Movie>();

		Analyzer analyzer = ConstantesLucene.getAnalyzer();
		File folder = new File(ConstantesLucene.directory);
		Directory directory;
		try {
			directory = FSDirectory.open(folder);
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);

			// Buscamos la película
			Query query = NumericRangeQuery.newLongRange(ConstantesLucene.id, 1, id, id, true, true);
			TopDocs topdoc = isearcher.search(query, 1);
			if (topdoc.scoreDocs == null || topdoc.scoreDocs.length == 0) {
				return movies;
			}
			int docId = topdoc.scoreDocs[0].doc;

			// ¿Generar document con los campos que vamos a buscar?

			// Similares
			// TODO: Configurar parámetros de similitud. Que es termfreq y
			// docfreq? Habrá problemas con los campos numéricos, como año, y
			// director y casting no sé como está funcionando. Debe de buscar
			// por palabras sueltas...
			MoreLikeThis mlt = new MoreLikeThis(ireader);
			mlt.setMinTermFreq(0);
			mlt.setMinDocFreq(0);
			mlt.setBoost(true);
			mlt.setFieldNames(new String[] { ConstantesLucene.genres, ConstantesLucene.directors });
			mlt.setAnalyzer(analyzer);

			Query queryLike = mlt.like(docId);
			TopDocs topdocs = isearcher.search(queryLike, 10);

			// Procesamos los resultados
			movies = processResults(topdocs.scoreDocs, isearcher);
			if (!movies.isEmpty() && movies.get(0).getId().equals(id)) {
				movies.remove(0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Obtenidas " + movies.size() + " películas similares.");

		return movies;
	}

	private static List<Movie> processResults(ScoreDoc[] hits, IndexSearcher isearcher) throws IOException {
		List<Movie> movies = new ArrayList<Movie>();
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);

			ScoreDoc score = hits[i];

			// Pasamos el string voteAverage a Float
			String voteAverageString = hitDoc.get(ConstantesLucene.voteAverage);
			Float voteAverage = null;
			if (voteAverageString != null) {
				voteAverage = Float.parseFloat(voteAverageString);
			}

			// Actores
			List<Person> people = new ArrayList<Person>();
			for (String personString : hitDoc.getValues(ConstantesLucene.cast)) {
				people.add(new Person(personString, null, null, TypePerson.CAST));
			}

			// Directores
			for (String personString : hitDoc.getValues(ConstantesLucene.directors)) {
				people.add(new Person(personString, null, null, TypePerson.DIRECTOR));
			}

			// Géneros
			List<String> genres = Arrays.asList(hitDoc.getValues(ConstantesLucene.genres));

			// Año
			int year = Integer.valueOf(hitDoc.get(ConstantesLucene.year));

			// Duración
			int runtime = Integer.valueOf(hitDoc.get(ConstantesLucene.runtime));

			// Identificador
			long id = Long.valueOf(hitDoc.get(ConstantesLucene.id));

			// Creamos el objeto película
			Movie movie = new Movie(id, hitDoc.get(ConstantesLucene.title), hitDoc.get(ConstantesLucene.description),
					hitDoc.get(ConstantesLucene.poster), voteAverage, year, runtime, people, genres, score.score);

			movies.add(movie);
		}
		return movies;
	}

}
