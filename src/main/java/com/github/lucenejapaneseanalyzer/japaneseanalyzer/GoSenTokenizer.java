package com.github.lucenejapaneseanalyzer.japaneseanalyzer;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.IOException;
import java.io.Reader;

import net.java.sen.SenFactory;
import net.java.sen.StreamTagger;
import net.java.sen.dictionary.Morpheme;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;



/**
 * This is a Japanese tokenizer which uses "Sen" morphological
 * analyzer.
 *
 * @author Manabu Ishii
 * @author Takashi Okamoto
 * @author Kazuhiro Kazama
 */
public class GoSenTokenizer extends Tokenizer {
    private StreamTagger tagger = null;
	private TermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;

    public GoSenTokenizer(Reader in, String configFile) throws IOException {
        tagger = new StreamTagger(SenFactory.getStringTagger(configFile),in);
		termAtt = addAttribute(TermAttribute.class);
		offsetAtt = addAttribute(OffsetAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!tagger.hasNext()) return false;
        net.java.sen.dictionary.Token token = tagger.next();

        if (token == null) return incrementToken();
        //
        final Morpheme m = token.getMorpheme();
         termAtt.setTermBuffer(m.getBasicForm());
         offsetAtt.setOffset(token.getStart() , token.end());
         typeAtt.setType(m.getPartOfSpeech());
        return true;
    }

  public void close() throws IOException {
    // TODO Auto-generated method stub
    super.close();
  }


}
