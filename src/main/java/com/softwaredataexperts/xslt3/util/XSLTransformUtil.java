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

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xalan.transformer.XalanProperties;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.softwaredataexperts.xslt3.model.XSLTransformationResult;

/**
 * A class definition, implementing an XSL transformation invocation 
 * code using Xalan-J's XSLT 3.0 transformation processor.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com>
 */
public class XSLTransformUtil {		
	
	protected String m_xmlFileName = null;
	
	protected String m_auxFileName = null;
	
	protected String m_xslFileName = null;
	
	/**
	 * Method definition, to invoke an XSL transformation by Apache Xalan 
	 * XSLT 3.0 development code.
	 * 
	 * @param xmlFile							 Reference to XML document uploaded
	 * @param xslFile                            Reference to an XSL stylesheet document uploaded 
	 * @param auxFile                            Reference to an auxiliary document file uploaded 
	 * @param initTempl                          An XSL transformation's initial template name
	 * @param initMode                           An XSL transformation's initial mode name
	 * @param enableAssert                       Boolean value true or false, whether XSL transformation
	 *                                           xsl:assert feature is enabled or not. 
	 * @param enableXslEvaluate                  Boolean value true or false, whether XSL transformation
	 *                                           xsl:evaluate feature is enabled or not.
	 * @return                                   XSLTransformationResult object instance
	 * @throws Exception
	 */
	public XSLTransformationResult xslTransform(Optional<MultipartFile> xmlFile, MultipartFile xslFile, Optional<MultipartFile> auxFile, 
                                                Optional<String> initTemplate, Optional<String> initMode, Optional<String> enableAssert, 
                                                Optional<String> enableXslEvaluate) throws Exception {
		
		XSLTransformationResult xslTransformResult = new XSLTransformationResult();
		
		XslTransformErrorHandler xslTransformErrorHandler = new XslTransformErrorHandler();
		
		try {
			System.setProperty(Constants.XML_DOCUMENT_BUILDER_FACTORY_KEY, Constants.XML_DOCUMENT_BUILDER_FACTORY_VALUE);
			System.setProperty(Constants.XSL_TRANSFORMER_FACTORY_KEY, Constants.XSL_TRANSFORMER_FACTORY_VALUE);						

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);		

			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

			m_xmlFileName = xmlFile.isPresent() ? (xmlFile.get()).getOriginalFilename() : null;
			String xmlDocStr = xmlFile.isPresent() ? new String((xmlFile.get()).getBytes()) : null;
			DOMSource xmlInputDomSrc = null;
			if (xmlDocStr != null) {
				StringReader strReader = new StringReader(xmlDocStr);
				InputSource inpSource = new InputSource(strReader);			
				Document document = docBuilder.parse(inpSource);
				xmlInputDomSrc = new DOMSource(document, m_xmlFileName);
			}

			m_xslFileName = xslFile.getOriginalFilename(); 
			String xslDocString = new String(xslFile.getBytes());	

			StringReader xslStrReader = new StringReader(xslDocString);
			InputSource xslInpSrc = new InputSource(xslStrReader);
			Document xslDocument = docBuilder.parse(xslInpSrc);
			DOMSource xslDomInputSrc = new DOMSource(xslDocument, m_xslFileName);

			m_auxFileName = auxFile.isPresent() ? (auxFile.get()).getOriginalFilename() : null;
			String auxFileDataStrValue = auxFile.isPresent() ? new String((auxFile.get()).getBytes()) : null;
			if (auxFileDataStrValue != null) {
			   FileOutputStream fos = new FileOutputStream(m_auxFileName);
			   fos.write(auxFileDataStrValue.getBytes());
			   fos.flush();
			   fos.close();
			}

			TransformerFactory xslTransformFactory = TransformerFactory.newInstance();

			String initTemplNameStr = null;			
			if (initTemplate.isPresent()) {
			   initTemplNameStr = initTemplate.get();
			   if ("".equals(initTemplNameStr)) {
				  initTemplNameStr = org.apache.xalan.templates.Constants.XSL_INITIAL_TEMPLATE_DEFAULT_NAME;  
			   }
			   
			   xslTransformFactory.setAttribute(XalanProperties.INIT_TEMPLATE, initTemplNameStr);
			}
			
			if (initMode.isPresent()) {
				String initModeNameStr = initMode.get();
				xslTransformFactory.setAttribute(XalanProperties.INIT_MODE, initModeNameStr);
			}
			
			List<String> trfErrorList = new ArrayList<String>();
			
			validateUploadedFileSizes(xmlFile, xslFile, auxFile, trfErrorList);
			
			if (trfErrorList.size() > 0) {
    		    xslTransformResult.setErrorList(trfErrorList);
    		   
    		    return xslTransformResult;
    		}
			
			if (enableAssert.isPresent()) {
				String enableAssertStr = enableAssert.get();
				if ("true".equals(enableAssertStr) || "yes".equals(enableAssertStr) || "1".equals(enableAssertStr)) {
				   xslTransformFactory.setAttribute(XalanProperties.ASSERT_ENABLED, Boolean.TRUE);
				}
				else if (!("false".equals(enableAssertStr) || "no".equals(enableAssertStr) || "0".equals(enableAssertStr))) {					
					trfErrorList.add("Error : XSL transformation's enable_assert configuration value, may have the values "
										                                                                + "true, yes, 1, false, no, 0. The supplied "
										                                                                + "value " + enableAssertStr + ", is incorrect.");				   						
				}
			}
			
			xslTransformFactory.setErrorListener(xslTransformErrorHandler);

			StringWriter resultStrWriter = new StringWriter();
			
			Templates templates = xslTransformFactory.newTemplates(xslDomInputSrc);
			String xslResultMethStr = null;
			if (templates != null) { 
				Transformer transformer = templates.newTransformer();
				transformer.setErrorListener(xslTransformErrorHandler);

				xslResultMethStr = transformer.getOutputProperty(OutputKeys.METHOD);
				
				Source xmlInpSrc = null;
	    		if ((initTemplNameStr != null) && (xmlInputDomSrc == null)) {    			
	    			StringReader strReader = new StringReader("<?xml version=\"1.0\"?><unlikely_xml_element/>");
	         	    xmlInpSrc = new StreamSource(strReader);
	    		}
	    		else {
	    			xmlInpSrc = xmlInputDomSrc; 
	    		}
	    		
	    		TransformerImpl transformerImpl = (TransformerImpl)transformer;
	    		if (enableXslEvaluate.isPresent()) {
					String enableXslEvaluateStr = enableXslEvaluate.get();
					if ("true".equals(enableXslEvaluateStr) || "yes".equals(enableXslEvaluateStr) || "1".equals(enableXslEvaluateStr)) {
						transformerImpl.setProperty(TransformerImpl.XSL_EVALUATE_PROPERTY, Boolean.TRUE);
					}
					else if (!("false".equals(enableXslEvaluateStr) || "no".equals(enableXslEvaluateStr) || "0".equals(enableXslEvaluateStr))) {
						trfErrorList.add("Error : XSL transformation's enable_xsl_evaluate configuration value, may have the values "
											                                                                      + "true, yes, 1, false, no, 0. The supplied "
											                                                                      + "value " + enableXslEvaluateStr + ", is incorrect.");				   							
					}
				}
	    		
	    		if (trfErrorList.size() > 0) {
	    		    xslTransformResult.setErrorList(trfErrorList);
	    		   
	    		    return xslTransformResult;
	    		}

				transformer.transform(xmlInpSrc, new StreamResult(resultStrWriter));
		    }

			trfErrorList = xslTransformErrorHandler.getTrfErrorList();
			List<String> trfFatalErrorList = xslTransformErrorHandler.getTrfFatalErrorList();
			if (trfErrorList.size() > 0 || trfFatalErrorList.size() > 0) {
				trfErrorList.addAll(trfFatalErrorList);
				xslTransformResult.setErrorList(trfErrorList);
			}
			else {
				if ((org.apache.xml.serializer.Method.XML).equals(xslResultMethStr)) {
					xslTransformResult.setResponseFormatStr(Constants.XML);
				}
				else if ((org.apache.xml.serializer.Method.TEXT).equals(xslResultMethStr)) {
					xslTransformResult.setResponseFormatStr(Constants.TEXT);
				}
				else if ((org.apache.xml.serializer.Method.HTML).equals(xslResultMethStr)) {
					xslTransformResult.setResponseFormatStr(Constants.HTML);
				}
				else if ((org.apache.xml.serializer.Method.JSON).equals(xslResultMethStr)) {
					xslTransformResult.setResponseFormatStr(Constants.JSON);
				}
				
				xslTransformResult.setResultDocumentStr(resultStrWriter.toString());
			}
		}		
        catch (TransformerException ex) {
        	List<String> errList = xslTransformErrorHandler.getTrfErrorList();
        	List<String> fatalErrList = xslTransformErrorHandler.getTrfFatalErrorList();
        	if ((errList.size() == 0) && (fatalErrList.size() == 0)) {
        	   throw new Exception(ex.getMessage());
        	}
		}
		catch (org.xml.sax.SAXException ex) {
			List<String> errList = xslTransformErrorHandler.getTrfErrorList();
        	List<String> fatalErrList = xslTransformErrorHandler.getTrfFatalErrorList();
        	if ((errList.size() == 0) && (fatalErrList.size() == 0)) {
        	   throw new Exception(ex.getMessage());
        	}
		}
		catch (Exception ex) {
			if (!((ex instanceof NullPointerException) || (ex instanceof ClassCastException))) {
				List<String> errList = xslTransformErrorHandler.getTrfErrorList();
	        	List<String> fatalErrList = xslTransformErrorHandler.getTrfFatalErrorList();
	        	if ((errList.size() == 0) && (fatalErrList.size() == 0)) {
	        	   throw new Exception(ex.getMessage());
	        	}
			}
		}
 		finally {
 			if (m_auxFileName != null) {
 			   File file = new File(m_auxFileName);
			   file.delete();
 			}
		}
		
		return xslTransformResult;
	}

	/**
	 * Method definition, to validate uploaded file sizes.
	 * 
	 * @param xmlFile                       Represents, an optional XML document file
	 * @param xslFile                       Represents, a required XSL document file
	 * @param auxFile                       Represents, an optional auxiliary document file
	 * @param trfErrorList                  A List<String> object instance, to contain file sizes
	 *                                      error information.
	 */
	private void validateUploadedFileSizes(Optional<MultipartFile> xmlFile, MultipartFile xslFile,
			                                                                         Optional<MultipartFile> auxFile, List<String> trfErrorList) {
		if (m_xmlFileName != null) {
			MultipartFile xmlMultipartDocument = xmlFile.get();
			long xmlDocumentByteSize = xmlMultipartDocument.getSize();
			double xmlDocumentMbSize = (xmlDocumentByteSize / (1024 * 1024));
			if (xmlDocumentMbSize > 1.5) {
			    trfErrorList.add("Error : An XML document's max size, for an uploaded XML document can be " + Constants.XML_AND_AUX_FILE_SIZE_LIMIT + " MB.");
			}
		}
		
		long xslDocumentByteSize = xslFile.getSize();
		double xslDocumentMbSize = (xslDocumentByteSize / (1024 * 1024));
		if (xslDocumentMbSize > 0.5) {
		    trfErrorList.add("Error : An XSL stylesheet document's max size, for an uploaded XSL stylesheet can be " + Constants.XSL_FILE_SIZE_LIMIT + " MB.");
		}
		
		if (m_auxFileName != null) {
			MultipartFile auxFileMultipartDocument = auxFile.get();
			long auxFileDocumentByteSize = auxFileMultipartDocument.getSize();
			double auxFileDocumentMbSize = (auxFileDocumentByteSize / (1024 * 1024));
			if (auxFileDocumentMbSize > 1.5) {
			    trfErrorList.add("Error : An XML auxiallry document's max size, for an uploaded XML document can be " + Constants.XML_AND_AUX_FILE_SIZE_LIMIT + " MB.");
			}
		}
	}

}
