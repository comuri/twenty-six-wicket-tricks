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
package com.locke.library.web.wow.component;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.apache.wicket.model.IModel;

import com.locke.library.utilities.metadata.MetaData;
import com.locke.library.web.wow.layouts.FreeFormLayout;

/**
 * Interface to instantiate a component with a given model. Also contains
 * meta-data extracted from the model (for example, a bean
 * {@link PropertyDescriptor}) which can be used to order and place constructed
 * components in a layout.
 * 
 * @author Jonathan Locke
 */
public interface IComponentFactory<C, M> extends Serializable {

	/**
	 * Meta-data key for the component's name. Component factories that don't
	 * provide this value cannot participate in some layouts, such as
	 * {@link FreeFormLayout}, which requires named components.
	 */
	public static final MetaData.Key<String> NAME = new MetaData.Key<String>();

	/**
	 * @return Meta-data for use in constructing and placing components
	 */
	MetaData getMetaData();

	/**
	 * @return The model which will be given to new component instances
	 */
	IModel<M> getModel();

	/**
	 * @param id
	 *            The id of the component to create
	 * @return The component
	 */
	C newComponent(String id);
}
