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
package com.locke.library.web.components.forms;

import org.apache.wicket.markup.html.form.Form;

import com.locke.library.web.locators.FormComponentMatcher;
import com.locke.library.web.locators.FormErrorMatcher;
import com.locke.library.web.utilities.focus.Focuser;

/**
 * A Form subclass which demonstrates how to use {@link Focuser} to
 * automatically set the focus.
 * 
 * @author Jonathan Locke
 */
public class ErrorFocusingForm extends Form<ErrorFocusingForm> {

	private static final long serialVersionUID = 8888465606224141811L;

	/**
	 * True if the form should auto-focus
	 */
	private boolean autofocus = true;

	/**
	 * @param id
	 *            Component id
	 */
	public ErrorFocusingForm(String id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (autofocus) {
			new Focuser(new FormComponentMatcher(this)).setFocus();
			autofocus = false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onError() {
		super.onError();
		new Focuser(new FormErrorMatcher(this)).setFocus();
	}
}
