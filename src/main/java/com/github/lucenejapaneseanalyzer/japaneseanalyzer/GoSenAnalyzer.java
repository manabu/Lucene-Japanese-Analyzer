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
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Filters a Japanese tokenizer with a {@link POSfilter},{@link
 * LowerCaseFilter} and {@link StopFilter}.
 * 
 * @author Manabu Ishii
 * @author Kazuhiro Kazama
 * @author Takashi Okamoto
 */
public class GoSenAnalyzer extends Analyzer {
  public static final String PROPERTY_NAME = "org.apache.lucene.ja.config.file";
  public static final String VERBOSE_FLAG = "org.apache.lucene.ja.config.verbose";
  public static final String PROPERTY_FILE_NAME = "org/apache/lucene/analysis/ja/analyzer-sen.xml";
  public static final String PROPERTY_SEN_HOME = "sen.home";

  private Set stopTable;
  private Hashtable posTable;
  private String tokenizer;
  private boolean verbose;
  private String configFile = null;

  /**
   * Build a Japanese analyzer.
   */
  public GoSenAnalyzer(){
      this(System.getProperty(PROPERTY_SEN_HOME) + System.getProperty("file.separator") + "dictionary.xml"); 
  }
  
  /**
   * Build a Japanese analyzer.
   * @param configFile configuration file for morphological analuzer. 
   *         If you use sen, specify sen configuration file(eg. sen.xml). 
   */
  public GoSenAnalyzer(String configPath) {
    String propertyFile = System.getProperty(PROPERTY_NAME);
    this.configFile = configPath;
    if (propertyFile == null)
      propertyFile = PROPERTY_FILE_NAME;
    InputStream in = this.getClass().getClassLoader().getResourceAsStream(
        propertyFile);
    if (in == null)
      throw new GoSenAnalyzerException(
          "Can't find a Japanese tokenizer property file: " + propertyFile);
    
    ArrayList slist = new ArrayList();
    ArrayList alist = new ArrayList();
    parseConfig(in, slist, alist);

    if (tokenizer == null)
      throw new GoSenAnalyzerException(
          "Can't find Japanese tokenizer class.");

    /* Set a stop word list. */
    String[] stopWords = (String[]) slist.toArray(new String[0]);
    stopTable = StopFilter.makeStopSet(stopWords);

    /* Set a pos word list. */
    String[] pos = (String[]) alist.toArray(new String[0]);
    posTable = POSFilter.makePOSTable(pos);

    /* Read a verbose flag */
    verbose = Boolean.getBoolean(VERBOSE_FLAG);
  }

  /**
   * parse configuration file and set a stop word and a pos word.
   * @param in	InputStream for config file.
   * @param slist stop word list
   * @param alist pos word list
   * @throws FactoryConfigurationError
   */
  private void parseConfig(InputStream in, ArrayList slist, ArrayList alist) throws FactoryConfigurationError {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(in);
      NodeList nl = doc.getFirstChild().getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        org.w3c.dom.Node n = nl.item(i);
        if (n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
          String nn = n.getNodeName();
          String value = n.getFirstChild().getNodeValue();

          if (nn.equals("tokenizerClass")) {
            tokenizer = value;
          } else if (nn.equals("stop")) {
            // read nested tag in <stop>
            NodeList dnl = n.getChildNodes();
            for (int j = 0; j < dnl.getLength(); j++) {
              org.w3c.dom.Node dn = dnl.item(j);
              if (dn.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                String dnn = dn.getNodeName();
                if (dn.getFirstChild() == null) {
                  throw new IllegalArgumentException("element '" + dnn
                      + "' is empty");
                }
                String dvalue = dn.getFirstChild().getNodeValue();

                if (dnn.equals("letters")) {
                  for (int k = 0; k < dvalue.length(); k++) {
                    slist.add("" + dvalue.charAt(k));
                  }
                } else if (dnn.equals("word")) {
                  slist.add(dvalue);
                }
              }
            }
          } else if (nn.equals("accept")) {
            // read nested tag in <accept>
            NodeList dnl = n.getChildNodes();
            for (int j = 0; j < dnl.getLength(); j++) {
              org.w3c.dom.Node dn = dnl.item(j);
              if (dn.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                String dnn = dn.getNodeName();
                if (dn.getFirstChild() == null) {
                  throw new IllegalArgumentException("element '" + dnn
                      + "' is empty");
                }
                String dvalue = dn.getFirstChild().getNodeValue();

                if (dnn.equals("pos")) {
                  alist.add(dvalue);
                }
              }
            }
          }
        }
      }
      in.close();
    } catch (ParserConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Constructs a Japanese tokenizer filtered by a {@link POSFilter}, a
   * {@link LowerCaseFilter}and a {@linkStopFilter}.
   */
  public final TokenStream tokenStream(String fieldName, Reader reader) {
    reader = (Reader) new NormalizeReader(reader);

    Class[] types = new Class[2];
    types[0] = Reader.class;
    types[1] = String.class;
    Object[] args = new Object[2];
    args[0] = reader;
    args[1] = configFile;
    TokenStream result;
    try {
      result = (TokenStream) Class.forName(tokenizer).getConstructor(types)
          .newInstance(args);
    } catch (Exception e) {
      e.printStackTrace();
      throw new GoSenAnalyzerException("Can't load a Japanese tokenizer: "
          + e.getMessage());
    }
    result = new POSFilter(result, posTable);
    result = new DigitFilter(result);
    result = new LowerCaseFilter(result);
    result = new KatakanaStemFilter(result);
    //result = new PorterStemFilter(result);
    result = new StopFilter(false, result, stopTable);
    if (verbose)
      result = new MonitorFilter(result);
    return result;
  }

}
