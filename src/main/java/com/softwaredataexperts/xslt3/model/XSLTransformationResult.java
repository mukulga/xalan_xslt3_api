/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.softwaredataexperts.xslt3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A class definition, that encapsulates the result of an XSL 
 * transformation.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com>
 */
public class XSLTransformationResult { 
	
	/**
	 * This string value, represents an XSL transformation result
	 * that be either XML document, text document, HTML document or
	 * a JSON document. 
	 */
	private String m_resultDocumentStr = null;
	
	/**
	 * This can have string value "XML", "TEXT", "HTML" or "JSON". 
	 */	
	private String m_responseFormatStr = null;
	
	/**
	 * If the resultDocumentStr value is null, then this list value
	 * shall contain details about one or more errors produced by an XSL
	 * transformation. 
	 */
	private List<String> m_errorList = new ArrayList<String>();

	public String getResultDocumentStr() {
		return m_resultDocumentStr;
	}

	public void setResultDocumentStr(String resultDocumentStr) {
		this.m_resultDocumentStr = resultDocumentStr;
	}
	
	public String getResponseFormatStr() {
		return m_responseFormatStr;
	}
	
	public void setResponseFormatStr(String responseFormatStr) {
		this.m_responseFormatStr = responseFormatStr;
	}

	public List<String> getErrorList() {
		return m_errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.m_errorList = errorList;
	}

}
