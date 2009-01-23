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
package com.locke.library.web.wow.editors;

import org.apache.wicket.model.IModel;

import com.locke.library.web.panels.IPanelFactory;

public class BeanFormPanel<T> extends AbstractFormPanel<T> {

	private static final long serialVersionUID = -5026146949588538140L;

	public BeanFormPanel(String id, IModel<T> model, IPanelFactory fields) {
		super(id, model);
		BeanForm<T> form = new BeanForm<T>("form", model);
		add(form);
		form.add(fields.newPanel("fields"));
	}
}
