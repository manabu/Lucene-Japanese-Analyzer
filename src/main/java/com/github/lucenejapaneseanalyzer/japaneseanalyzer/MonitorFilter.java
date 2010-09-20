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
import java.util.HashSet;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Filter tokens extracted with a Japanese tokenizer.
 *
 */
public final class MonitorFilter extends TokenFilter {
  /* Class variables */

  /* Instance variables */
  HashSet parts;

        private TermAttribute termAtt;
        private OffsetAttribute offsetAtt;
        private TypeAttribute typeAtt;

  /**
   * Construct filtering <i>in </i>.
   */
  public MonitorFilter(TokenStream in) {
          super(in);
                termAtt = addAttribute(TermAttribute.class);
                offsetAtt = addAttribute(OffsetAttribute.class);
                typeAtt = addAttribute(TypeAttribute.class);
  }

  /**
   * Returns the next token in the stream, or null at EOS.
   * <p>
   * Print all tokens.
   */
  /*
  public final Token next() throws IOException {
    Token t = input.next();
    if (t != null)
      System.out.println("[" + t.termText() + ", " + t.type() + ", "
          + t.startOffset() + ", " + t.endOffset() + ", " + "]");
    return t;
  }
        */
  /**
   * Returns the next token in the stream, or null at EOS.
   * <p>
   * Print all tokens.
   */
  @Override
  public boolean incrementToken() throws IOException {
          boolean incrementToken = input.incrementToken();
            if (incrementToken)
              System.out.println("[" + termAtt.term() + ", " + typeAtt.type() + ", "
                  + offsetAtt.startOffset() + ", " + offsetAtt.endOffset() + ", " + "]");
            return incrementToken;
  }
}
