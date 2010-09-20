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

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

/**
 * Convert a katakana word to a normalized form by stemming KATAKANA-HIRAGANA
 * PROLONGED SOUND MARK (U+30FC) which exists at the last of the string. In
 * general, most of Japanese full-text search engine uses more complicated
 * method which needs dictionaries. I think they are better than this filter in
 * quality, but they needs a well-tuned dictionary. In contract, this filter is
 * simple and maintenance-free.
 *
 * Note: This filter don't supports hankaku katakana characters, so you must
 * convert them before using this filter. And this filter support only
 * pre-composed characters.
 *
 */
public final class KatakanaStemFilter extends TokenFilter {
        static final char COMBINING_KATAKANA_HIRAGANA_VOICED_SOUND_MARK = '\u3099';

        static final char COMBINING_KATAKANA_HIRAGANA_SEMI_VOICED_SOUND_MARK = '\u309A';

        static final char KATAKANA_HIRAGANA_VOICED_SOUND_MARK = '\u309B';

        static final char KATAKANA_HIRAGANA_SEMI_VOICED_SOUND_MARK = '\u309C';

        static final char KATAKANA_HIRAGANA_PROLONGED_SOUND_MARK = '\u30FC';

        private TermAttribute termAtt;

        public KatakanaStemFilter(TokenStream in) {
                super(in);
                termAtt = addAttribute(TermAttribute.class);
        }

        /**
         * Returns the next input Token, after being stemmed
         */
        /*
        public final Token next() throws IOException {
                Token token = input.next();
                if (token == null)
                        return null;
                String s = token.termText();
                int len = s.length();
                if (len > 3
                                && s.charAt(len - 1) == KATAKANA_HIRAGANA_PROLONGED_SOUND_MARK
                                && isKatakanaString(s)) {
                        token = new Token(s.substring(0, len - 1), token.startOffset(),
                                        token.endOffset(), token.type());
                }
                return token;
        }
        */
        /**
         * Returns the next input Token, after being stemmed
         */
        @Override
        public boolean incrementToken() throws IOException {
                boolean incrementToken = input.incrementToken();
                if (incrementToken == false)
                        return false;
                String s = termAtt.term();
                int len = s.length();
                if (len > 3
                                && s.charAt(len - 1) == KATAKANA_HIRAGANA_PROLONGED_SOUND_MARK
                                && isKatakanaString(s)) {
                        termAtt.setTermBuffer(s.substring(0,len-1));
                }
                return true;
        }

        boolean isKatakanaString(String s) {
                for (int i = 0; i < s.length(); i++) {
                        char c = s.charAt(i);
                        if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.KATAKANA
                                        && c != COMBINING_KATAKANA_HIRAGANA_VOICED_SOUND_MARK
                                        && c != COMBINING_KATAKANA_HIRAGANA_SEMI_VOICED_SOUND_MARK
                                        && c != KATAKANA_HIRAGANA_VOICED_SOUND_MARK
                                        && c != KATAKANA_HIRAGANA_SEMI_VOICED_SOUND_MARK)
                                return false;
                }
                return true;
        }
}
