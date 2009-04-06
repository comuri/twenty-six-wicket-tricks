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
package com.locke.library.web.wow.extraction.extractors.bean.factories;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.locke.library.web.wow.extraction.extractors.bean.AbstractLabeledPanelExtractor;
import com.locke.library.web.wow.extraction.extractors.bean.factories.panels.DropDownChoicePanel;

/**
 * Panel factory
 * 
 * @author Jonathan Locke
 */
public abstract class DropDownChoiceExtractor<T> extends
		AbstractLabeledPanelExtractor<T> {

	private static final long serialVersionUID = -4967380964319730316L;

	/**
	 * {@inheritDoc}
	 */
	public DropDownChoiceExtractor(IModel<T> model) {
		super(model);
	}

	/**
	 * {@inheritDoc}
	 */
	public Panel extract(String id) {
		return new DropDownChoicePanel(id, getLabelModel(), getModel(), getChoices());
	}

	/**
	 * @return Choices for drop-downs created by this factory
	 */
	public abstract IModel<List<? extends T>> getChoices();
}
