package siddur.query.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.eclipse.jetty.util.log.Log;

import siddur.query.bean.Comment;
import siddur.query.dao.DerbyUtil;
import siddur.query.web.tag.Paging;

public class LuceneUtil {
	private static final File DATA_DIR = new File("lucene") ;
	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String WRITE_AT = "writeAt";
	public static final String WRITE_BY = "writeBy";
	public static final String PROJECT = "project";
	public static final String TARGET = "target";
	public static LuceneUtil instance = new LuceneUtil();
	
	private Analyzer analyzer;
	private FieldType ft1, ft2, ft3, ft4, ft5, ft6;
	IndexWriter iw;
	
	private LuceneUtil(){}
	
	public void addComment(Comment c){
		try {
			iw.addDocument(createDoc(c));
			iw.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateComment(Comment c){
		try {
			iw.updateDocument(new Term(ID, Integer.toString(c.id)), createDoc(c));
			iw.commit();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void destory() throws IOException{
		iw.close();
	}
	
	public void init(){
		boolean first = false;
		if(!DATA_DIR.isDirectory()){
			first = true;
		}
		analyzer = new EnglishAnalyzer(Version.LUCENE_42);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		try {
			iw = new IndexWriter(FSDirectory.open(DATA_DIR), config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ft1 = new FieldType();
		ft1.setIndexed(true);
		ft1.setStored(true);
		
		ft2 = new FieldType();
		ft2.setIndexed(true);
		ft2.setStored(true);
		
		ft3 = new FieldType();
		ft3.setIndexed(false);
		ft3.setStored(true);
		ft3.setNumericType(NumericType.DOUBLE);
		
		ft4 = new FieldType();
		ft4.setIndexed(false);
		ft4.setStored(true);
		
		ft5 = new FieldType();
		ft5.setIndexed(true);
		ft5.setStored(true);
		
		ft6 = new FieldType();
		ft6.setIndexed(false);
		ft6.setStored(true);
		
		if(first)
			indexAll();
	}
	
	public Document createDoc(Comment c){
		Document doc = new Document();
		Field f1 = new Field(ID, Integer.toString(c.id), ft1);
		doc.add(f1);
		Field f2 = new Field(CONTENT, c.content, ft2);
		doc.add(f2);
		Field f3 = new Field(WRITE_AT, Long.toString(c.writeAt.getTime()), ft3);
		doc.add(f3);
		Field f4 = new Field(WRITE_BY, c.writeBy, ft4);
		doc.add(f4);
		Field f5 = new Field(PROJECT, Integer.toString(c.project), ft5);
		doc.add(f5);
		Field f6 = new Field(TARGET, Integer.toString(c.target), ft6);
		doc.add(f6);
		
		return doc;
	}
	
	public Paging<Comment> search(int project, String condition, int pageIndex, int pageSize){
		Paging<Comment> paging = new Paging<Comment>();
		paging.pageSize = pageSize;
		paging.pageIndex = pageIndex;
		List<Comment> list = new ArrayList<Comment>();
		try {
			TermQuery tq = new TermQuery(new Term(PROJECT, Integer.toString(project)));
			BooleanQuery bq = new BooleanQuery();
			bq.add(tq, Occur.MUST);
			if(condition != null && !condition.equals("")){
//				condition = condition.replace("?", "");
				Query q = new QueryParser(Version.LUCENE_42, CONTENT, analyzer).parse(condition);
				bq.add(q, Occur.MUST);
			}
			IndexReader reader = DirectoryReader.open(FSDirectory.open(DATA_DIR));
			IndexSearcher is = new IndexSearcher(reader);
			
			int total = (pageIndex + 1) * pageSize;
			TopDocs results = is.search(bq, 150);
			ScoreDoc[] hits = results.scoreDocs;
			
			int numTotalHits = results.totalHits;
			Log.info(numTotalHits + " total matching documents");
			
			int start = pageIndex * pageSize;
		    int end = Math.min(numTotalHits, total);
		    paging.total = numTotalHits;
		    
		    for (int i = start; i < end; i++) {
				ScoreDoc s = hits[i];
				Document doc = is.doc(s.doc);
				Comment c = new Comment();
				c.id = Integer.parseInt(doc.getField(ID).stringValue());
				c.content = doc.getField(CONTENT).stringValue();
//				System.out.println(c.content + "-------" + s.score);
//				System.out.println(is.explain(tq, s.doc));
				c.writeAt = new Date(Long.parseLong(doc.getField(WRITE_AT).stringValue()));
				c.writeBy = doc.getField(WRITE_BY).stringValue();
				c.project = Integer.parseInt(doc.getField(PROJECT).stringValue());
				c.target = Integer.parseInt(doc.getField(TARGET).stringValue());
				list.add(c);
			}
		    
		    reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		paging.data = list;
		
		return paging;
	}
	
	private void indexAll(){
		List<Comment> comments = DerbyUtil.instance.listAsks();
		
		Log.info("start to index..");
		
		try {
			for(Comment c : comments){
				iw.addDocument(createDoc(c));
			}
			iw.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
