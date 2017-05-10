package edu.arizona.cs.watson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.nio.file.*;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.util.Index;

/**
 * ROSARIO RIVERA
 * CS483 PROJECT
 * WATSON
 * COMMENTS: 
 *
 */
public class IndexW
{
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
	
    public static void main( String[] args ) throws IOException, ParseException
    {
    	
    	File index = new File("index");
    	Path p = index.toPath();
    	StandardAnalyzer analyzer = new StandardAnalyzer(ENGLISH_STOP_WORDS_SET);
    	Directory dir = SimpleFSDirectory.open(p);
    	IndexWriterConfig config = new IndexWriterConfig(analyzer);
    	
    	IndexWriter w = new IndexWriter(dir, config);
    	w.deleteAll();
        //System.out.println("Please enter name of file: ");

    	// = new Scanner(System.in);
    	//String name = scan.next();
    	File text = new File("text3.txt");
    	String line;
    	String key = "";
    	String info = "";
    	Boolean beg = true;
    	Sentence sent;
		List<String> lemma;
		String inline;

		
//		  File filedir = new File("wiki");
//		  
//		  File[] directoryListing = filedir.listFiles();
//		  if (directoryListing != null) {
//			  
//		   for (File child : directoryListing) {
//			   System.out.println(child.getName());
			   Scanner scan = new Scanner(text);
				
			   //w.close();
		    	while(scan.hasNextLine() && (line = scan.nextLine()) != null)
		    	{
		    		
		    		    			//end = false;
		    		if(line.length()>0)
		    		{
		    			char start = line.charAt(0);
			    		if(line.startsWith("[[")&& line.endsWith("]]"))
			    		{
			    			
			    			if(!beg && !(info.isEmpty())&& info != "")
			    			{

			    				//System.out.println(info);
			    				sent = new Sentence(info);
			    				
			    				lemma = sent.lemmas();
		
			    				//lemma.removeAll(c)
			    				//System.out.println(key);
			    				//System.out.println(lemma.toString());
			    				addDoc(w, lemma.toString(), key);
			    				//System.out.println(lemma.toString());
			    				info = "";
			    			
			    			}
			    			else if(beg)
			    			{
			    				beg = false;
			    			}

			    			key = line.substring(2, line.indexOf(']'));
			    			
			    			
			    			
			    		}
			    		else if(line.contains("may refer to")|| start=='#')
			    		{
			    			beg = true;
			    		}
			    		else if(start!= '#' && start != '=' && start != '|')
			    		{
			    			info = info +" "+ line;
			    		}
		    		}
		    	}
		    	scan.close();
//		    }
//		   
//		  } else {
////		     Handle the case where dir is not really a directory.
////		     Checking dir.isDirectory() above would not be sufficient
////		     to avoid race conditions with another process that deletes
////		     directories.
//		  }    
		    	w.close();

		  System.out.println("INDEX DONE");
		
    
  
        
        
        
        
        
        //Query q = new QueryParser("content", analyzer).parse("");
        //querySearch(q,index);
        //System.out.println( "Hello World!" );
    }
    

    
    private static void addDoc(IndexWriter w, String text, String title) throws IOException 
    {
    	  Document doc = new Document();
    	  doc.add(new StringField("title", title, Field.Store.YES));
    	  //doc.getField("title").
    	  doc.add(new TextField("text", text, Field.Store.YES));
    	  w.addDocument(doc);
    }
}
