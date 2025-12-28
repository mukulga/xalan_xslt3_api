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
package com.softwaredataexperts.xslt3.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.softwaredataexperts.xslt3.model.XSLTransformationResult;
import com.softwaredataexperts.xslt3.service.XSLTransformationService;
import com.softwaredataexperts.xslt3.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * A class definition, implementing an XSL transformation REST 
 * api controller.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com>
 */
@RestController
@Api(value = "XSL transformation controller, accepting api requests to do XSL transformations")
public class XSLTransformationController {
	
	@Autowired
	private XSLTransformationService xslTransformationService;
	
	/**
	 * Method definition, to accept XSL transformation upload files, and 
	 * other XSL transformation configuration options.
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
	 * @return                                   ResponseEntity object instance
	 */
	@ApiOperation(value = "Method to, do XSL transformation as per arguments provided to this method, "
			                                                                         + "and send XSL transformation response to api client", 
			      produces = "XML, Text, HTML, JSON")    
	@PostMapping("/xsl3/transform")
    public ResponseEntity<Object> xslTransformHandler(@RequestParam("xml_file") Optional<MultipartFile> xmlFile, 
    		                                          @RequestParam("xsl_file") MultipartFile xslFile, 
    		                                          @RequestParam("aux_file") Optional<MultipartFile> auxFile, 
    		                                          @RequestParam("init_template") Optional<String> initTempl,
    		                                          @RequestParam("init_mode") Optional<String> initMode,
    		                                          @RequestParam("enable_assert") Optional<String> enableAssert,
    		                                          @RequestParam("enable_xsl_evaluate") Optional<String> enableXslEvaluate)
    {
				
		XSLTransformationResult xslTransformResult = new XSLTransformationResult();				
		
		/**
		 * An API auxFile argument can be, an optional input file like a text document, 
		 * JSON document, an XML Schema document. An XSL transformation stylesheet, may 
		 * contain in a usual way an optional XML Schema document inline within an XSL 
		 * stylesheet, using xsl:import-schema XSLT 3.0 instruction.
		 */
		
		ResponseEntity<Object> responseEntity = null;
		
		try {	
		   xslTransformResult = xslTransformationService.xslTransform(xmlFile, xslFile, auxFile, initTempl, initMode, enableAssert, enableXslEvaluate);
		   
		   List<String> errList = xslTransformResult.getErrorList();
		   
		   MultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();
		   
		   if (errList.size() == 0) {
			   String responseFormatStr = xslTransformResult.getResponseFormatStr();
			   		   		   
			   if ((Constants.XML).equals(responseFormatStr)) {
				   responseHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/xml")); 
			   }
			   else if ((Constants.TEXT).equals(responseFormatStr)) {
				   responseHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/text")); 
			   }
			   else if ((Constants.HTML).equals(responseFormatStr)) {
				   responseHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/html"));
			   }
			   else if ((Constants.JSON).equals(responseFormatStr)) {
				   responseHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/json"));
			   }

			   responseEntity = new ResponseEntity<Object>(xslTransformResult.getResultDocumentStr(), responseHeaders, HttpStatus.OK);			   
		   }
		   else {
			   responseHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/json"));
			   
			   responseEntity = new ResponseEntity<Object>(xslTransformResult.getErrorList(), responseHeaders, HttpStatus.BAD_REQUEST);
		   }
		}
		catch (Exception ex) {		    
			List<String> errList = xslTransformResult.getErrorList();
		    errList.add(ex.getMessage());
		    
		    MultiValueMap<String, String> errorResultHeaders = new LinkedMultiValueMap<>();
		    errorResultHeaders.put(org.springframework.http.HttpHeaders.CONTENT_TYPE, Arrays.asList("application/json"));
		    responseEntity = new ResponseEntity<Object>(errList, errorResultHeaders, HttpStatus.BAD_REQUEST); 
		}
		
		return responseEntity;
    }

}
