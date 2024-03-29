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
package com.locke;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.lang.Classes;

/**
 * Trick page
 */
public abstract class Trick extends WebPage {

	private final HeaderContributor css = CSSPackageResource
			.getHeaderContribution(Trick.class, Classes.simpleName(Trick.class)
					+ ".css");

	/**
	 * Construct.
	 */
	public Trick() {
		add(this.css);
		add(new Label("description", getString("description")));
		add(new Label("name", new PropertyModel<String>(this, "name")));
	}

	public HeaderContributor getCss() {
		return this.css;
	}

	public String getName() {
		return Classes.simpleName(getClass());
	}

	protected IModel<String> getStringModel(final String key) {
		return new StringResourceModel(key, this, null);
	}
}
