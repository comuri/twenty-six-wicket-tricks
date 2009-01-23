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
package com.locke.library.web.resources.xml;

import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

public abstract class XmlWebResource extends DynamicWebResource
{
	private static final long serialVersionUID = -1266088092355168070L;

	private String xml;

	public XmlWebResource(final String filename)
	{
		super(filename);
	}

	@Override
	public IResourceStream getResourceStream()
	{
		return new StringResourceStream(getCachedXml());
	}

	protected abstract String getXml();

	private String getCachedXml()
	{
		if (xml == null)
		{
			xml = getXml();
		}
		return xml;
	}

	@Override
	protected ResourceState getResourceState()
	{
		return new ResourceState()
		{

			@Override
			public String getContentType()
			{
				return "text/xml";
			}

			@Override
			public byte[] getData()
			{
				return getXml().getBytes();
			}
		};
	}
}
