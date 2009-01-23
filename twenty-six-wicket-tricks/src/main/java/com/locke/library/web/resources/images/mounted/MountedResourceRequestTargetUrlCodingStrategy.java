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
package com.locke.library.web.resources.images.mounted;

import java.util.Locale;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Resource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.AbstractRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.resource.ISharedResourceRequestTarget;
import org.apache.wicket.request.target.resource.SharedResourceRequestTarget;

/**
 * Nice URL coding strategy for mounted resources.
 * 
 * @author Jonathan Locke
 */
public class MountedResourceRequestTargetUrlCodingStrategy extends
		AbstractRequestTargetUrlCodingStrategy {

	/**
	 * The "<scope>/<name>" prefix that must match to use this coding strategy
	 */
	private String resourceKey;

	/**
	 * @param mountPath
	 *            The full path to mount on (including the name and extension)
	 * @param resource
	 *            The scope of this resource
	 * @param name
	 *            The name of the resource
	 * @param locale
	 *            The resource locale
	 * @param style
	 *            The resource style
	 */
	public MountedResourceRequestTargetUrlCodingStrategy(String mountPath,
			Resource resource, String name, Locale locale, String style) {
		super(mountPath);

		// Get resource key for the given resource
		this.resourceKey = WebApplication.get().getSharedResources()
				.resourceKey(resource.getClass(), name, locale, style);
	}

	/**
	 * {@inheritDoc}
	 */
	public IRequestTarget decode(RequestParameters requestParameters) {
		requestParameters.setResourceKey(resourceKey);
		return new SharedResourceRequestTarget(requestParameters);
	}

	/**
	 * {@inheritDoc}
	 */
	public CharSequence encode(IRequestTarget requestTarget) {
		return getMountPath();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean matches(IRequestTarget requestTarget) {

		// If it's a shared resource request
		if (requestTarget instanceof ISharedResourceRequestTarget) {

			// Get the resource key being requested
			String key = ((ISharedResourceRequestTarget) requestTarget)
					.getResourceKey();

			// Return true if the resource key matches our resource
			return key.startsWith(resourceKey);
		}
		return false;
	}
}