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
package com.locke.library.web.behaviors.focus;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * Behavior which sets input focus to the component it is attached to. This is a
 * temporary behavior, which means it will be removed after the component is
 * rendered.
 * 
 * @author Jonathan Locke
 */
public class FocusBehavior extends AbstractBehavior {

	private static final long serialVersionUID = -6848204456741032984L;

	private Component component;

	/**
	 * {@inheritDoc}
	 */
	public void bind(Component component) {
		this.component = component;
		component.setOutputMarkupId(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void renderHead(IHeaderResponse iHeaderResponse) {
		super.renderHead(iHeaderResponse);
		iHeaderResponse.renderOnLoadJavascript("document.getElementById('"
				+ component.getMarkupId() + "').focus()");
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTemporary() {
		return true;
	}
}