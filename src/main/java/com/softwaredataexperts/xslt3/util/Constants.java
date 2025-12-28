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
package com.softwaredataexperts.xslt3.util;

/**
 * A class definition, specifying few application constants
 * for this API application project.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com>
 */
public class Constants {
	
	public static final String XML_DOCUMENT_BUILDER_FACTORY_KEY = "javax.xml.parsers.DocumentBuilderFactory";
	
	public static final String XML_DOCUMENT_BUILDER_FACTORY_VALUE = "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
	
    public static final String XSL_TRANSFORMER_FACTORY_KEY = "javax.xml.transform.TransformerFactory";
	
	public static final String XSL_TRANSFORMER_FACTORY_VALUE = "org.apache.xalan.processor.XSL3TransformerFactoryImpl";

	public static final String XML = "XML";
	
	public static final String TEXT = "TEXT";
	
	public static final String HTML = "HTML";
	
	public static final String JSON = "JSON";
	
	public static final double XML_AND_AUX_FILE_SIZE_LIMIT = 1;       // Size in MB
	
	public static final double XSL_FILE_SIZE_LIMIT = 1;               // Size in MB
	
}
