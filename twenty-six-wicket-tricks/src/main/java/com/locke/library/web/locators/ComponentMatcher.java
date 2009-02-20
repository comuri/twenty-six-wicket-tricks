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
package com.locke.library.web.locators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Component.IVisitor;

import com.locke.library.locator.ISource;
import com.locke.library.utilities.object.MutableValue;

public class ComponentMatcher implements ISource<Component>, Serializable {

	private static final long serialVersionUID = 5529503615284334432L;

	private final MarkupContainer container;
	private final Class<?> type;

	public ComponentMatcher(final MarkupContainer container) {
		this(container, Component.class);
	}

	public ComponentMatcher(final MarkupContainer container, final Class<?> type) {
		this.container = container;
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Component> all() {
		final List<Component> components = new ArrayList<Component>();
		if (matches(container)) {
			components.add(container);
		}
		container.visitChildren(type, new IVisitor<Component>() {
			public Object component(Component component) {
				if (matches(component)) {
					components.add(component);
				}
				return CONTINUE_TRAVERSAL;
			}
		});
		return components;
	}

	/**
	 * {@inheritDoc}
	 */
	public Component first() {
		final MutableValue<Component> value = new MutableValue<Component>();
		if (matches(container)) {
			return container;
		}
		container.visitChildren(type, new IVisitor<Component>() {
			public Object component(Component component) {
				if (matches(component)) {
					value.setValue(component);
					return STOP_TRAVERSAL;
				}
				return CONTINUE_TRAVERSAL;
			}
		});
		return value.getValue();
	}

	/**
	 * @param component
	 *            The component to test
	 * @return True if the component matches
	 */
	protected boolean matches(Component component) {
		return true;
	}
}
