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
package com.softwaredataexperts.xslt3.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.softwaredataexperts.xslt3.model.XSLTransformationResult;
import com.softwaredataexperts.xslt3.util.XSLTransformUtil;

/**
 * A class definition, specifying a service class used by REST 
 * api for this project.
 * 
 * @author Mukul Gandhi <gandhi.mukul@gmail.com>
 */
@Service
public class XSLTransformationService extends XSLTransformUtil {

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
	 * @throws IOException
	 */
	public XSLTransformationResult xslTransform(Optional<MultipartFile> xmlFile, MultipartFile xslFile, Optional<MultipartFile> auxFile, 
			                                                                     Optional<String> initTempl, Optional<String> initMode, 
			                                                                     Optional<String> enableAssert, Optional<String> enableXslEvaluate) throws Exception {
		
		XSLTransformationResult xslTransformResult = null;
		
		XSLTransformUtil xslTransformUtil = new XSLTransformUtil();
		
		xslTransformResult = xslTransformUtil.xslTransform(xmlFile, xslFile, auxFile, initTempl, initMode, enableAssert, enableXslEvaluate);
		
		return xslTransformResult;
	}

}
