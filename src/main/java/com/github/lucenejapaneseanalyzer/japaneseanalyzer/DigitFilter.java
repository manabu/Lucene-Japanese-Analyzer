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

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * Merge digits extracted with a Japanese tokenizer.
 */
public final class DigitFilter extends TokenFilter {

        private TermAttribute termAtt;
        private OffsetAttribute offsetAtt;
        private TypeAttribute typeAtt;
        private String prevTermText;
        private int prevStart;
        private int prevEnd;
        private String prevTypeText;
        /* Instance variables */
        boolean preRead;

        Token preReadToken;

        /**
         * Construct filtering <i>in </i>.
         */

        protected DigitFilter(TokenStream in) {
                super(in);
                preRead = false;
                preReadToken = null;
                termAtt = addAttribute(TermAttribute.class);
                offsetAtt = addAttribute(OffsetAttribute.class);
                typeAtt = addAttribute(TypeAttribute.class);
        }

        /**
         * Returns the next token in the stream, or null at EOS.
         * <p>
         * Merge consecutive digits.
         */
        @Override
        public boolean incrementToken() throws IOException {
                if (preRead) {
                        preRead = false;
                        //
                        termAtt.setTermBuffer(prevTermText);
                        offsetAtt.setOffset(prevStart, prevEnd);
                        typeAtt.setType(prevTypeText);
                        return true;
                }
                boolean incrementToken = input.incrementToken();
                if (incrementToken == false)
                        return false;
                String term = termAtt.term();
                if (term.length() == 1 && Character.isDigit(term.charAt(0))) {
                        int start = offsetAtt.startOffset();
                        int end = offsetAtt.endOffset();
                        String type = typeAtt.type();
                        StringBuffer st = new StringBuffer();
                        st.append(termAtt.term());
                        while (true) {
                                incrementToken = input.incrementToken();
                                if (incrementToken == false
                                                || (termAtt.term().length() != 1 || !Character.isDigit(termAtt
                                                                .term().charAt(0)))) {
                                        preRead = true;
                                        prevTermText = termAtt.term();
                                        prevStart = offsetAtt.startOffset();
                                        prevEnd = offsetAtt.endOffset();
                                        prevTypeText = typeAtt.type();
                                        //
                                        termAtt.setTermBuffer(new String(st));
                                        offsetAtt.setOffset(start, end);
                                        typeAtt.setType(type);
                                        return true;
                                }
                                st.append(termAtt.term());
                                end = offsetAtt.endOffset();
                        }
                }
                return incrementToken;
        }

}
