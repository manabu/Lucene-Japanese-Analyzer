package com.github.lucenejapaneseanalyzer.japaneseanalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HankakuKatakanaTest {
	String input;
	String expected;
	@Before
	public void before(){
		System.setProperty("org.apache.lucene.ja.config.file", "japanese-gosen-analyzer.xml");
		System.setProperty("sen.home","../GoSen/testdata/dictionary");
		System.setProperty("org.apache.lucene.ja.config.verbose","true");
		
	}
	@Parameters
	public static Collection data() {
		return Arrays.asList(new Object[][] {
				{ "ｶﾞ", "ガ" },
				{ "ｷﾞ", "ギ" },
				{ "ｸﾞ", "グ" },
				{ "ｹﾞ", "ゲ" },
				{ "ｺﾞ", "ゴ" },
				{ "ｻﾞ", "ザ" },
				{ "ｼﾞ", "ジ" },
				{ "ｽﾞ", "ズ" },
				{ "ｾﾞ", "ゼ" },
				{ "ｿﾞ", "ゾ" },
				{ "ﾀﾞ", "ダ" },
				{ "ﾁﾞ", "ヂ" },
				{ "ﾂﾞ", "ヅ" },
				{ "ﾃﾞ", "デ" },
				{ "ﾄﾞ", "ド" },
				{ "ﾊﾞ", "バ" },
				{ "ﾊﾟ", "パ" },
				{ "ﾋﾞ", "ビ" },
				{ "ﾋﾟ", "ピ" },
				{ "ﾌﾞ", "ブ" },
				{ "ﾌﾟ", "プ" },
				{ "ﾍﾞ", "ベ" },
				{ "ﾍﾟ", "ペ" },
				{ "ﾎﾞ", "ボ" },
				{ "ﾎﾟ", "ポ" },
				{ "ｳﾞ", "ヴ" }
				 });
	}
	public HankakuKatakanaTest(String input, String expected){
		this.input = input;
		this.expected = expected;
	}
	@Test
	public void test() throws IOException{
		// TODO パラメタライズで、半角カタカナのテストをかく
		String text = this.input;
		Analyzer analyzer = new GoSenAnalyzer();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		TermAttribute ta = ts.getAttribute(TermAttribute.class);
		while(ts.incrementToken()){
			String term = ta.term();
			System.out.println(term);
			assertThat(term,is(this.expected));
		}
		
	}
}
