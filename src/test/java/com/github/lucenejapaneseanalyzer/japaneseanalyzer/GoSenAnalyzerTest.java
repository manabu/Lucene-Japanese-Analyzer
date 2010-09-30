package com.github.lucenejapaneseanalyzer.japaneseanalyzer;

//import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import org.junit.Before;
import org.junit.Test;
public class GoSenAnalyzerTest {
	@Before
	public void before(){
		System.setProperty("org.apache.lucene.ja.config.file", "japanese-gosen-analyzer.xml");
		System.setProperty("sen.home","../GoSen/testdata/dictionary");
		System.setProperty("org.apache.lucene.ja.config.verbose","true");
		
	}
	@Test
	public void test1() throws ParseException {
		GoSenAnalyzer analyzer = new GoSenAnalyzer();
		QueryParser qp = new QueryParser(Version.LUCENE_30,"testfield",analyzer);
		Query query = qp.parse("hello world");
	}
	@Test
	public void test2() throws ParseException {
		GoSenAnalyzer analyzer = new GoSenAnalyzer();
		QueryParser qp = new QueryParser(Version.LUCENE_30,"testfield",analyzer);
		Query query = qp.parse("こんにちは、世界");
	}
	@Test
	public void test3() throws ParseException {
		GoSenAnalyzer analyzer = new GoSenAnalyzer();
		QueryParser qp = new QueryParser(Version.LUCENE_30,"testfield",analyzer);
		Query query = qp.parse("私の名前はGoSenAnalyzerです。");
	}
	@Test
	public void indexWriteTest() throws Exception{
		RAMDirectory idx = new RAMDirectory();
		Searcher searcher = null;
        try {
        	Analyzer analyzer = new GoSenAnalyzer();
        	IndexWriter writer = 
                new IndexWriter(idx,analyzer,true,IndexWriter.MaxFieldLength.LIMITED);
        	String content = "世界のみなさん、こんにちは";
        	Document doc1 = new Document();
        	doc1.add(new Field("content", content, Field.Store.YES, Field.Index.ANALYZED));
        	writer.addDocument(doc1);
        	writer.optimize();
        	writer.close();
        	//
        	searcher = new IndexSearcher(idx);

        	QueryParser qp = new QueryParser(Version.LUCENE_30,"content",analyzer);
        	Query query = qp.parse("世界");


        	TopScoreDocCollector collector = TopScoreDocCollector.create(5, false);
        	searcher.search(query, collector);
        	ScoreDoc[] hits = collector.topDocs().scoreDocs;
        	int hitCount = collector.getTotalHits();
        	
        	searcher.close();
        	assertThat(hitCount,is(1));
        }catch(Exception e){
        	throw e;
        }finally{
        	if(searcher!=null){
        		searcher.close();
        	}
        }
	}
	@Test
	public void hankakuKanaTest() throws ParseException{
		GoSenAnalyzer analyzer = new GoSenAnalyzer();
		QueryParser qp = new QueryParser(Version.LUCENE_30,"testfield",analyzer);
		Query query = qp.parse("ﾊﾟﾝﾀﾛﾝ");
		
	}
	@Test
	public void hankakuKanaTest2() throws ParseException{
		GoSenAnalyzer analyzer = new GoSenAnalyzer();
		QueryParser qp = new QueryParser(Version.LUCENE_30,"testfield",analyzer);
		Query query = qp.parse("パンタロン");
		
	}

}
