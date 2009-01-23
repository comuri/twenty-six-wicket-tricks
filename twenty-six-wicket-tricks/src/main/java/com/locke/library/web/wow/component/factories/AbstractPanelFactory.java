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
package com.locke.library.web.wow.component.factories;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.locke.library.utilities.metadata.MetaData;
import com.locke.library.web.wow.component.IComponentFactory;

/**
 * Base implementation of {@link IComponentFactory}.
 * 
 * @see IComponentFactory
 * @author Jonathan Locke
 */
public abstract class AbstractPanelFactory<T> implements
		IComponentFactory<Panel, T> {

	private static final long serialVersionUID = -6684693052212706516L;

	/**
	 * MetaData populated by subclasses
	 */
	private final MetaData metadata = new MetaData();

	/**
	 * The model this factory uses when creating components
	 */
	private final IModel<T> model;

	/**
	 * Construct.
	 * 
	 * @param model
	 *            The model that this factory will assign to created components
	 */
	public AbstractPanelFactory(final IModel<T> model) {
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public MetaData getMetaData() {
		return metadata;
	}

	/**
	 * {@inheritDoc}
	 */
	public IModel<T> getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract Panel newComponent(String id);
}
