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
package com.locke.tricks.f;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.locke.library.web.components.forms.ErrorFocusingForm;

@SuppressWarnings("serial")
public class ErrorFocusingTestForm extends ErrorFocusingForm {

	private String field1;
	private String field2;

	public ErrorFocusingTestForm(String id) {
		super(id);
		setModel(new CompoundPropertyModel<ErrorFocusingForm>(this));
		this.add(new RequiredTextField<String>("field1"));
		this.add(new RequiredTextField<String>("field2"));
	}

	@Override
	protected void onSubmit() {
		this.info(String.format(this.getString("submit"), field1, field2));
	}
}