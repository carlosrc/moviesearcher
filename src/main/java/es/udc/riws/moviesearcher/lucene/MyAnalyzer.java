package es.udc.riws.moviesearcher.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class MyAnalyzer extends Analyzer {

	/** Default maximum allowed token length */
	public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

	public static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET;

	protected final CharArraySet stopwords;

	private Version version;

	public MyAnalyzer(Version version) {
		// Las stopwords no pueden ser null
		this.stopwords = STOP_WORDS_SET;
		this.version = version;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		final StandardTokenizer src = new StandardTokenizer(version, reader);
		src.setMaxTokenLength(maxTokenLength);
		TokenStream tok = new StandardFilter(version, src);

		// No distinguimos mayúsculas
		tok = new LowerCaseFilter(version, tok);
		// No procesamos las stopwords
		tok = new StopFilter(version, tok, stopwords);
		// No distinguimos acentos
		tok = new ASCIIFoldingFilter(tok);

		return new TokenStreamComponents(src, tok);
	}

}
