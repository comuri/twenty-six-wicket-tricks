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

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * This class automatically mounts a {@link RenderedDynamicImageResource}. You
 * can get instances of {@link Image} via {@link #newImage(String id)}.
 * 
 * @author Jonathan Locke
 */
public abstract class MountedDynamicImageResource extends
		RenderedDynamicImageResource {

	private static final long serialVersionUID = -9117628603222075688L;

	/**
	 * True if the subclass has been mounted already
	 */
	private static final Map<Class<? extends MountedDynamicImageResource>, Boolean> mounted = new HashMap<Class<? extends MountedDynamicImageResource>, Boolean>();

	/**
	 * The name of this image resource
	 */
	private final String name;

	/**
	 * Construct a "png" format image resource.
	 * 
	 * @see #MountedDynamicImageResource(String, String, int, int, String)
	 */
	public MountedDynamicImageResource(final String path, final String name,
			int width, int height) {
		this(path, name, width, height, "png");
	}

	/**
	 * Construct.
	 * 
	 * @param path
	 *            Path to mount on like "/images" (the image name and format
	 *            will be appended automatically to produce something like
	 *            "/images/image.png")
	 * @param name
	 *            The resource name to use like "image"
	 * @param width
	 *            The width of the image
	 * @param height
	 *            The height of the image
	 * @param format
	 *            The image format (like "png" or "gif")
	 */
	public MountedDynamicImageResource(final String path, final String name,
			int width, int height, String format) {
		super(width, height, format);

		// Save name for component construction
		this.name = name;

		synchronized (mounted) {

			// If not already mounted
			if (mounted.get(getClass()) == null) {
				mounted.put(getClass(), Boolean.TRUE);

				// Share this resource
				WebApplication.get().getSharedResources().add(getClass(), name,
						getLocale(), null, this);

				// And mount it on the given path
				WebApplication.get().mount(
						new MountedResourceRequestTargetUrlCodingStrategy(path
								+ "/" + name + "." + format, this, name,
								getLocale(), null));
			}
		}
	}

	/**
	 * @param id
	 *            Component id
	 * @return An {@link Image} component that references this mounted, shared
	 *         resource.
	 */
	public Image newImage(String id) {
		return new Image(id, new ResourceReference(getClass(), name));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected abstract boolean render(Graphics2D graphics);
}
