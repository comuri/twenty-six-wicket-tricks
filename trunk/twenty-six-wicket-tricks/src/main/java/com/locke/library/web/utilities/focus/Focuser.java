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
package com.locke.library.web.utilities.focus;

import java.io.Serializable;

import org.apache.wicket.Component;

import com.locke.library.locator.ISource;
import com.locke.library.web.behaviors.focus.FocusBehavior;

/**
 * Sets the focus on something {@link IFocusable}, by adding a
 * {@link FocusBehavior} to the component returned by the {@link ISource} of
 * that {@link IFocusable}.
 * 
 * @author Jonathan Locke
 */
public class Focuser implements Serializable {

	private static final long serialVersionUID = 452250835800945960L;

	private final ISource<Component> componentSource;

	/**
	 * @param focusable
	 *            The focusable to use when setting focus
	 */
	public Focuser(ISource<Component> componentSource) {
		this.componentSource = componentSource;
	}

	/**
	 * Sets focus to the first component returned by the locator
	 */
	public void setFocus() {
		final Component focus = componentSource.first();
		if (focus != null) {
			focus.add(new FocusBehavior());
		}
	}
}
