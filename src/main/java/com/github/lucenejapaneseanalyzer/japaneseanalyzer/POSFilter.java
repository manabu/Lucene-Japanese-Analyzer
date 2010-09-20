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
import java.util.Hashtable;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Filter tokens extracted with a Japanese tokenizer by their part of speech.
 *
 */
public final class POSFilter extends TokenFilter {
  /* Instance variables */
  Hashtable table;

        private TypeAttribute typeAtt;
  /**
   * Construct a filter which removes unspecified pos from the input
   * TokenStream.
   */
  public POSFilter(TokenStream in, String[] pos) {
          super(in);
    table = makePOSTable(pos);
        typeAtt = addAttribute(TypeAttribute.class);
  }

  /**
   * Construct a filter which removes unspecified pos from the input
   * TokenStream.
   */
  public POSFilter(TokenStream in, Hashtable posTable) {
          super(in);
    table = posTable;
        typeAtt = addAttribute(TypeAttribute.class);
  }

  /**
   * Builds a hashtable from an array of pos.
   */
  public final static Hashtable makePOSTable(String[] pos) {
    Hashtable posTable = new Hashtable(pos.length);
    for (int i = 0; i < pos.length; i++)
      posTable.put(pos[i], pos[i]);
    return posTable;
  }

  /**
   * Returns the next token in the stream, or null at EOS.
   * <p>
   * Removes a specified part of speech.
   */
  /*
  public final Token next() throws IOException {
    Token t;
    while (true) {
      t = input.next();
      if (t == null)
        return null;
      if (table.contains(t.type()))
        break;
    }
    return t;
  }
        */
  /**
   * Returns the next token in the stream, or null at EOS.
   * <p>
   * Removes a specified part of speech.
   */
  @Override
  public boolean incrementToken() throws IOException {
          boolean incrementToken;
          while(true){
                  incrementToken = input.incrementToken();
                  if(incrementToken==false)
                          return false;
                  if(table.contains(typeAtt.type()))
                          break;
          }
          return incrementToken;
  }
}
