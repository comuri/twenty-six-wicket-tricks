/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.tricks.t;

import org.apache.wicket.PageParameters;

import com.locke.Trick;
import com.locke.library.web.components.labels.HtmlLabel;
import com.locke.library.web.utilities.renderer.TemplateRenderer;

/**
 * Trick 'T'
 * 
 * @author Jonathan Locke
 */
public class T extends Trick {

	static final TemplateRenderer renderer = new TemplateRenderer();

	public T() {
		PageParameters parameters = new PageParameters("a=1,b=2,c=3");
		add(new HtmlLabel("rendering", renderer.render(new TestWebPage(
				parameters))));
	}
}
