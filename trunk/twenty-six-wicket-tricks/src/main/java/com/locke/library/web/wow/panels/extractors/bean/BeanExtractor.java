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
package com.locke.library.web.wow.panels.extractors.bean;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;

import com.locke.library.utilities.metadata.MetaData;
import com.locke.library.web.wow.panels.IPanelFactory;
import com.locke.library.web.wow.panels.factories.CheckBoxFactory;
import com.locke.library.web.wow.panels.factories.DateFieldFactory;
import com.locke.library.web.wow.panels.factories.IntegerFieldFactory;
import com.locke.library.web.wow.panels.factories.TextFieldFactory;

/**
 * Extracts a list of panel factories from a bean {@link IModel} by inspecting
 * it.
 * 
 * @author Jonathan Locke
 */
public class BeanExtractor implements Iterable<IPanelFactory<?>> {

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
		private final Map<Class<?>, Class<? extends IPanelFactory<?>>> defaultFactories = new HashMap<Class<?>, Class<? extends IPanelFactory<?>>>();

		/**
		 * Map from property read method to factory class
		 */
		private final Map<Method, Class<? extends IPanelFactory<?>>> factories = new HashMap<Method, Class<? extends IPanelFactory<?>>>();

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
		public void map(final Class<?> type,
				final Class<? extends IPanelFactory<?>> factory) {
			this.defaultFactories.put(type, factory);
		}

		/**
		 * @param type
		 *            The type
		 * @param propertyName
		 *            The property
		 * @param factory
		 *            The factory to use
		 */
		public void map(final Class<?> type, final String propertyName,
				final Class<? extends IPanelFactory<?>> factory) {
			try {
				final Method readMethod = type.getMethod("get"
						+ Strings.capitalize(propertyName));
				this.factories.put(readMethod, factory);
			} catch (final SecurityException e) {
				e.printStackTrace();
			} catch (final NoSuchMethodException e) {
				e.printStackTrace();
			}
		}

		/**
		 * @param readMethod
		 *            The property access method
		 * @return The default panel factory for the given type
		 */
		private Class<? extends IPanelFactory<?>> defaultFactory(
				final Method readMethod) {
			return this.defaultFactories.get(readMethod.getReturnType());
		}

		/**
		 * @param readMethod
		 *            The read method
		 * @return The factory for the given read method
		 */
		private Class<? extends IPanelFactory<?>> factory(
				final Method readMethod) {

			// Look up factory for read method
			return this.factories.get(readMethod);
		}

		/**
		 * @param method
		 *            The property read method to inspect
		 * @return True if the method should be ignored. For example, you might
		 *         not want Object.getClass() to be treated as a property.
		 */
		private boolean shouldIgnore(final Method method) {
			final String declaringClassName = method.getDeclaringClass()
					.getName();
			return declaringClassName.startsWith("java.")
					|| declaringClassName.startsWith(".javax");
		}
	}

	/**
	 * Meta-data key for bean property descriptor
	 */
	public static final MetaData.Key<PropertyDescriptor> PROPERTY = new MetaData.Key<PropertyDescriptor>();

	private static final long serialVersionUID = -6916869078349411606L;

	/**
	 * The model to extract factories from
	 */
	private final IModel<?> model;

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
	public BeanExtractor(final IModel<?> model) {
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
	public BeanExtractor(final Settings settings, final IModel<?> model) {
		this.settings = settings;
		this.model = model;
	}

	public Iterator<IPanelFactory<?>> iterator() {
		return factories().iterator();
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
	protected IModel<?> newModel(final PropertyDescriptor property,
			final IModel<?> model) {
		return new PropertyModel<Object>(model, property.getName());
	}

	/**
	 * A factory method that creates a component factory for a given bean
	 * property and model.
	 * 
	 * @param property
	 *            The bean property descriptor
	 * @param model
	 *            The model for the component
	 * @return A panel factory
	 */
	protected IPanelFactory<?> newPanelFactory(
			final PropertyDescriptor property, final IModel<?> model) {
		try {
			final Method readMethod = property.getReadMethod();
			if (!this.settings.shouldIgnore(readMethod)) {
				final Class<? extends IPanelFactory<?>> factoryClass = factory(readMethod);
				final Constructor<? extends IPanelFactory<?>> constructor = factoryClass
						.getConstructor(IModel.class);
				return constructor.newInstance(model);
			}
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
		} catch (final InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	private List<IPanelFactory<?>> factories() {
		final List<IPanelFactory<?>> factories = new ArrayList<IPanelFactory<?>>();
		try {

			// Get properties
			final BeanInfo info = Introspector.getBeanInfo(this.model
					.getObject().getClass());

			// Go through properties
			for (final PropertyDescriptor property : info
					.getPropertyDescriptors()) {

				// Create a panel factory for the given property
				final IModel<?> propertyModel = newModel(property, this.model);
				if (propertyModel != null) {

					// Create panel factory
					final IPanelFactory<?> factory = newPanelFactory(property,
							propertyModel);

					// If it was created
					if (factory != null) {

						// Set bean property meta-data on factory
						factory.getMetaData().put(PROPERTY, property);

						// Add to factory list
						factories.add(factory);
					}
				}
			}
		} catch (final IntrospectionException e) {
			e.printStackTrace();
		}
		return factories;
	}

	/**
	 * @param readMethod
	 *            The property read method
	 * @return The panel factory for this property read method
	 */
	private Class<? extends IPanelFactory<?>> factory(final Method readMethod) {

		// Try annotation
		final PanelFactory annotation = readMethod
				.getAnnotation(PanelFactory.class);
		if (annotation != null) {
			return annotation.factory();
		}

		// Try read method map
		final Class<? extends IPanelFactory<?>> factoryClass = this.settings
				.factory(readMethod);
		if (factoryClass != null) {
			return factoryClass;
		}

		// Try default
		final Class<? extends IPanelFactory<?>> defaultFactoryClass = this.settings
				.defaultFactory(readMethod);
		if (defaultFactoryClass != null) {
			return defaultFactoryClass;
		}

		throw new IllegalStateException("No IPanelFactory found for "
				+ readMethod);
	}
}
