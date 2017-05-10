package edu.arizona.cs.watson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import edu.stanford.nlp.simple.Sentence;

public class QueryW {
	
	 public static final CharArraySet ENGLISH_STOP_WORDS_SET;
	  
	  static {
	    final List<String> stopWords = Arrays.asList(
	      "a", "an", "and", "are", "as", "at", "be", "but", "by",
	      "for", "if", "in", "into", "is", "it", "{", "}", "|",
	      "no", "not", "of", "on", "or", "such", "[", "]",
	      "that", "the", "their", "then", "there", "these",
	      "they", "this", "to", "was", "will", "with", ","
	    );
	    final CharArraySet stopSet = new CharArraySet(stopWords, false);
	    ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet); 
	  }
	
	public static void main(String[] args) throws IOException, ParseException
	{
		File index = new File("index");
    	Path p = index.toPath();
    	StandardAnalyzer analyzer = new StandardAnalyzer(ENGLISH_STOP_WORDS_SET);
    	Directory dir = SimpleFSDirectory.open(p);
		File quest = new File("questions.txt");
		Scanner scanQ = new Scanner(quest);
		String qscan = "";
		Sentence squery;
		List<String> lquery;
    	System.out.println("At query");
   while(scanQ.hasNextLine() && (qscan = scanQ.nextLine()) != null)
   {
	   if(qscan.length() > 0)
	   {
		   String category = qscan;
		   String question = scanQ.nextLine();
		   String ganswer = scanQ.nextLine();
		   String mquery = category+ " "+question;//+" "+ganswer;
		   squery = new Sentence(mquery);
		   lquery = squery.lemmas();
		   mquery = lquery.toString();
		   QueryParser q2 = new QueryParser("text", analyzer);
		   mquery = q2.escape(mquery);
		   System.out.println(ganswer);
		   //Query og = new Query();
		   Query q = q2.parse(mquery);
	    	
	    	querySearch(q,dir);
	   }
   }
   scanQ.close();
	}
	private static void querySearch(Query query, Directory index) throws IOException
	{
        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;    
        
        
        // 4. display results
        System.out.println("Found " + hits.length + " hit(s).");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") + "\t Score:" + hits[i].score);
        }

        reader.close();
	}
}
