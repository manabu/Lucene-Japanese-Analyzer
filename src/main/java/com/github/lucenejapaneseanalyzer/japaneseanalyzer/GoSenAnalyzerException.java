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

/**
 * Thrown to indicate that a Japanese analyzer has a problem.
 *
 */
public class GoSenAnalyzerException extends RuntimeException {
  /**
   * Constructs an <code>JapaneseAnalyzerException</code> with no detail
   * message.
   */
  public GoSenAnalyzerException() {
    super();
  }

  /**
   * Constructs an <code>JapaneseAnalyzerException</code> with the specified
   * detail message.
   *
   * @param s
   *          the detail message.
   */
  public GoSenAnalyzerException(String s) {
    super(s);
  }
}
