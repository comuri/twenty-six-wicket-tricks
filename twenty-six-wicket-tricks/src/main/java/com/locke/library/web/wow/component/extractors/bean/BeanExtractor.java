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
package com.locke.library.web.wow.component.extractors.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;

import com.locke.library.utilities.metadata.MetaData;
import com.locke.library.web.wow.component.IComponentFactory;
import com.locke.library.web.wow.component.IComponentFactorySource;
import com.locke.library.web.wow.component.factories.CheckBoxFactory;
import com.locke.library.web.wow.component.factories.DateFieldFactory;
import com.locke.library.web.wow.component.factories.IntegerFieldFactory;
import com.locke.library.web.wow.component.factories.TextFieldFactory;

/**
 * Extracts a list of panel factories from a bean {@link IModel} by inspecting
 * it.
 * 
 * @author Jonathan Locke
 */
public class BeanExtractor implements IComponentFactorySource<Panel> {

	/**
	 * Meta-data key for bean property descriptor
	 */
	public static final MetaData.Key<PropertyDescriptor> PROPERTY = new MetaData.Key<PropertyDescriptor>();

	private static final long serialVersionUID = -6916869078349411606L;

	/**
	 * The model to extract factories from
	 */
	private IModel<?> model;

	/**
	 * Extraction settings
	 */
	private final Settings settings;

	/**
	 * Construct.
	 * 
	 * @param model
	 *            The model to extract from
	 */
	public BeanExtractor(IModel<?> model) {
		this(new Settings(), model);
	}

	/**
	 * Construct.
	 * 
	 * @param model
	 *            The model to extract from
	 * @param settings
	 *            Settings to use for extraction
	 */
	public BeanExtractor(Settings settings, IModel<?> model) {
		this.settings = settings;
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IComponentFactory<Panel, ?>> factories() {
		final List<IComponentFactory<Panel, ?>> factories = new ArrayList<IComponentFactory<Panel, ?>>();
		try {

			// Get properties
			final BeanInfo info = Introspector.getBeanInfo(model.getObject()
					.getClass());

			// Go through properties
			for (PropertyDescriptor property : info.getPropertyDescriptors()) {

				// Create a component factory
				final IModel<?> propertyModel = newModel(property, model);
				if (propertyModel != null) {

					// Create component factory
					final IComponentFactory<Panel, ?> factory = newComponentFactory(
							property, propertyModel);

					// If it was created
					if (factory != null) {

						// Set bean property meta-data on factory
						factory.getMetaData().put(PROPERTY, property);

						// Add to factory list
						factories.add(factory);
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return factories;
	}

	/**
	 * A factory method that creates a component factory for a given bean
	 * property and model.
	 * 
	 * @param property
	 *            The bean property descriptor
	 * @param model
	 *            The model for the component
	 * @return A component factory
	 */
	protected IComponentFactory<Panel, ?> newComponentFactory(
			PropertyDescriptor property, IModel<?> model) {
		try {
			final Method readMethod = property.getReadMethod();
			if (!settings.shouldIgnore(readMethod)) {
				final Class<? extends IComponentFactory<Panel, ?>> factoryClass = factory(readMethod);
				Constructor<? extends IComponentFactory<Panel, ?>> constructor = factoryClass
						.getConstructor(IModel.class);
				return constructor.newInstance(model);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a model for a given bean property.
	 * 
	 * @param property
	 *            The bean property descriptor
	 * @param model
	 *            The bean model
	 * @return The model for the given property
	 */
	protected IModel<?> newModel(PropertyDescriptor property, IModel<?> model) {
		return new PropertyModel<Object>(model, property.getName());
	}

	/**
	 * @param readMethod
	 *            The property read method
	 * @return The component factory for this property read method
	 */
	private Class<? extends IComponentFactory<Panel, ?>> factory(
			final Method readMethod) {

		// Try annotation
		final PanelFactory annotation = readMethod
				.getAnnotation(PanelFactory.class);
		if (annotation != null) {
			return annotation.factory();
		}

		// Try read method map
		final Class<? extends IComponentFactory<Panel, ?>> factoryClass = settings
				.factory(readMethod);
		if (factoryClass != null) {
			return factoryClass;
		}

		// Try default
		final Class<? extends IComponentFactory<Panel, ?>> defaultFactoryClass = settings
				.defaultFactory(readMethod);
		if (defaultFactoryClass != null) {
			return defaultFactoryClass;
		}

		throw new IllegalStateException("No IComponentFactory found for "
				+ readMethod);
	}

	/**
	 * Settings for the extractor, including default factories and
	 * property-specific factories. You can add mappings to new default
	 * factories with {@link #map(Class, Class)} and property-specific factories
	 * with {@link #map(Class, String, Class)}.
	 * 
	 * @author Jonathan Locke
	 */
	public static class Settings {

		/**
		 * Default map from property type to factory class
		 */
		private Map<Class<?>, Class<? extends IComponentFactory<Panel, ?>>> defaultFactories = new HashMap<Class<?>, Class<? extends IComponentFactory<Panel, ?>>>();

		/**
		 * Map from property read method to factory class
		 */
		private Map<Method, Class<? extends IComponentFactory<Panel, ?>>> factories = new HashMap<Method, Class<? extends IComponentFactory<Panel, ?>>>();

		/**
		 * Construct.
		 */
		public Settings() {

			// Set up defaults for common types
			map(Boolean.class, CheckBoxFactory.class);
			map(Boolean.TYPE, CheckBoxFactory.class);
			map(String.class, TextFieldFactory.class);
			map(Integer.class, IntegerFieldFactory.class);
			map(Integer.TYPE, IntegerFieldFactory.class);
			map(Date.class, DateFieldFactory.class);
		}

		/**
		 * Map type to factory (for all properties)
		 * 
		 * @param type
		 *            The property type
		 * @param factory
		 *            The factory to use for this type
		 */
		public void map(Class<?> type,
				Class<? extends IComponentFactory<Panel, ?>> factory) {
			defaultFactories.put(type, factory);
		}

		/**
		 * @param type
		 *            The type
		 * @param propertyName
		 *            The property
		 * @param factory
		 *            The factory to use
		 */
		public void map(Class<?> type, String propertyName,
				Class<? extends IComponentFactory<Panel, ?>> factory) {
			try {
				Method readMethod = type.getMethod("get"
						+ Strings.capitalize(propertyName));
				factories.put(readMethod, factory);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		/**
		 * @param readMethod
		 *            The property access method
		 * @return The default component factory for the given type
		 */
		private Class<? extends IComponentFactory<Panel, ?>> defaultFactory(
				final Method readMethod) {
			return defaultFactories.get(readMethod.getReturnType());
		}

		/**
		 * @param readMethod
		 *            The read method
		 * @return The factory for the given read method
		 */
		private Class<? extends IComponentFactory<Panel, ?>> factory(
				final Method readMethod) {

			// Look up factory for read method
			return factories.get(readMethod);
		}

		/**
		 * @param method
		 *            The property read method to inspect
		 * @return True if the method should be ignored. For example, you might
		 *         not want Object.getClass() to be treated as a property.
		 */
		private boolean shouldIgnore(Method method) {
			final String declaringClassName = method.getDeclaringClass()
					.getName();
			return declaringClassName.startsWith("java.")
					|| declaringClassName.startsWith(".javax");
		}
	}
}
