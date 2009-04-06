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
package com.locke.library.web.wow.extraction.extractors.bean.factories.editors;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

public class TextFieldPanel extends AbstractTextFieldEditor {

	private static final long serialVersionUID = 2232889782978056661L;

	public TextFieldPanel(String id, IModel<String> label, IModel<String> model) {
		super(id, label);
		add(new RequiredTextField<String>("component", model));
	}
}
