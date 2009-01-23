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
package com.locke.library.web.models;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * A list model formed by wrapping a string model and breaking it into pieces on
 * a separator.
 * 
 * @author Jonathan Locke
 */
public class StringListModel extends
		AbstractReadOnlyModel<List<? extends String>> {

	private static final long serialVersionUID = 6331128615397330714L;

	private final IModel<String> model;

	private final String separator;

	public StringListModel(IModel<String> model) {
		this(model, ";");
	}

	public StringListModel(IModel<String> model, String separator) {
		this.model = model;
		this.separator = separator;
	}

	@Override
	public List<String> getObject() {
		return Arrays.asList(model.getObject().split(separator));
	}
}
