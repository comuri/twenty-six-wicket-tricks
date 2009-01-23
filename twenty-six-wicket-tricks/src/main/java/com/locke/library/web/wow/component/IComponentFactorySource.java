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

import java.util.List;

import org.apache.wicket.Component;

import com.locke.library.web.wow.component.extractors.bean.BeanExtractor;
import com.locke.library.web.wow.layouts.ColumnLayout;

/**
 * An arbitrary source of component factories.
 * <p>
 * An extractor such as {@link BeanExtractor} would implement this method to
 * serve as a source of factories that construct components for each property of
 * a bean.
 * <p>
 * A layout implementation like {@link ColumnLayout} can consume this interface
 * and use the extracted factories to create components, decoupling it from any
 * concrete source of components.
 * 
 * @author Jonathan Locke
 */
public interface IComponentFactorySource<T extends Component> {

	/**
	 * @return A list of component factories
	 */
	List<IComponentFactory<T, ?>> factories();
}
