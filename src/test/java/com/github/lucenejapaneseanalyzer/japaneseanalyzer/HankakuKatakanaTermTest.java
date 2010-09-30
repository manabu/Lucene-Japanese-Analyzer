package com.github.lucenejapaneseanalyzer.japaneseanalyzer;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Before;
import org.junit.Test;

public class HankakuKatakanaTermTest {
	@Before
	public void before(){
		System.setProperty("org.apache.lucene.ja.config.file", "japanese-gosen-analyzer.xml");
		System.setProperty("sen.home","../GoSen/testdata/dictionary");
		System.setProperty("org.apache.lucene.ja.config.verbose","true");
		
	}
	@Test
	public void test() throws IOException{
		String text = "パンタロン";
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is("パンタロン"));
		}
		
	}
	@Test
	public void test2() throws IOException{
		String text = "ﾊﾟﾝﾀﾛﾝ";
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is("パンタロン"));
		}
		
	}
	@Test
	public void test3() throws IOException{
		String text = "ライス";
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is("ライス"));
		}
		
	}
	@Test
	public void test4() throws IOException{
		String text = "ﾗｲｽ";
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is("ライス"));
		}
	}
	@Test
	public void test5() throws Exception{
		// TODO 全角スペースを半角スペースにする
		String text = "　";
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is(" "));
		}
	}
}
