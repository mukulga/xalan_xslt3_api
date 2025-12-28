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

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.apache.xml.utils.DefaultErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A class definition, specifying Xalan-J XSL transformation error 
 * handler.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com> 
 */
public class XslTransformErrorHandler extends DefaultErrorHandler {
    
    private List<String> trfErrorList = new ArrayList<String>();
    
    private List<String> trfFatalErrorList = new ArrayList<String>();
    
    /**
     * Class constructor.
     */
    public XslTransformErrorHandler() {
    	super(true);
    }

    @Override
    public void error(TransformerException ex) throws TransformerException {            	    	
    	SourceLocator srcLocator = ex.getLocator();
    	
    	int errLineNo = srcLocator.getLineNumber();
    	int errColNo = srcLocator.getColumnNumber();    	
    	String errMesg = ex.getMessage();
        
    	trfErrorList.add("[Error : line " + errLineNo + ", column " + errColNo + "] " + errMesg);
    	
    	trfErrorList.add(errMesg);
    }

    @Override
    public void fatalError(TransformerException ex) throws TransformerException {        
    	SourceLocator srcLocator = ex.getLocator();    	    	    	
    	
    	int errLineNo = srcLocator.getLineNumber();
    	int errColNo = srcLocator.getColumnNumber();    	
    	String errMesg = ex.getMessage();
        
        trfFatalErrorList.add("[Fatal error : line " + errLineNo + ", column " + errColNo + "] " + errMesg);
    	trfFatalErrorList.add(errMesg);
    }

    @Override
    public void warning(TransformerException ex) throws TransformerException {
        // no op        
    }

    @Override
    public void error(SAXParseException ex) throws SAXException {        
    	String errMesg = ex.getMessage();
    	
    	int errLineNo = ex.getLineNumber();
    	int errColNo = ex.getColumnNumber();
    	
    	trfErrorList.add("[Error : line " + errLineNo + ", column " + errColNo + "] " + errMesg);
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {        
    	String errMesg = ex.getMessage();
    	
        int errLineNo = ex.getLineNumber();
    	int errColNo = ex.getColumnNumber();
    	
        trfErrorList.add("[Fatal error : line " + errLineNo + ", column " + errColNo + "] " + errMesg); 
    }

    @Override
    public void warning(SAXParseException ex) throws SAXException {
        // no op       
    }
    
    public List<String> getTrfErrorList() {
        return trfErrorList;  
    }
    
    public List<String> getTrfFatalErrorList() {
        return trfFatalErrorList;  
    }

}
