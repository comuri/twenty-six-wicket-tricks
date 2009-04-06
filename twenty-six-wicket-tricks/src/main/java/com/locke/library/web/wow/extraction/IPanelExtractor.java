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
package com.locke.library.web.wow.extraction;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.locke.library.utilities.metadata.MetaData;
import com.locke.library.web.wow.layout.layouts.FreeFormLayout;

/**
 * Extracts a panel from some data source, most likely a Java bean.
 * <p>
 * Also gives access to the model which is to be given to the panel and any
 * meta-data extracted from that model (for example, meta-data might include a
 * bean {@link PropertyDescriptor}). This information can be used to order and
 * place extracted panels in a layout.
 * 
 * @author Jonathan Locke
 */
public interface IPanelExtractor<M> extends Serializable {

	/**
	 * Meta-data key for the component's name. Component factories that don't
	 * provide this value cannot participate in some layouts, such as
	 * {@link FreeFormLayout}, which requires named components.
	 */
	public static final MetaData.Key<String> NAME = new MetaData.Key<String>();

	/**
	 * @param id
	 *            The id the extractor should give the panel
	 * @return The panel
	 */
	Panel extract(String id);

	/**
	 * @return Meta-data for use in constructing and placing components
	 */
	MetaData getMetaData();

	/**
	 * @return The model to be given to the extracted panel
	 */
	IModel<M> getModel();
}
