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
package com.locke.tricks.l;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.locke.Trick;
import com.locke.library.web.panels.feedback.GlobalFeedbackPanel;
import com.locke.library.web.panels.feedback.LocalFeedbackContainer;
import com.locke.library.web.panels.feedback.LocalFeedbackPanel;

/**
 * Trick 'L'
 * 
 * @author Jonathan Locke
 */
@SuppressWarnings("serial")
public class L extends Trick {

	private final class MyForm extends Form<Void> {

		private String text;

		MyForm(String id) {
			super(id);
			add(new TextField<String>("requiredTextField",
					new PropertyModel<String>(this, "text")).setRequired(true));
		}

		@Override
		protected void onSubmit() {
			info(String.format(getString("submitted"), text));
		}
	}

	public L() {

		// Global feedback
		add(new GlobalFeedbackPanel("globalFeedbackPanel"));

		// Form with local feedback
		{
			LocalFeedbackContainer localFeedbackContainer = new LocalFeedbackContainer(
					"localFeedbackContainer");
			localFeedbackContainer.add(new LocalFeedbackPanel(
					"localFeedbackPanel", localFeedbackContainer));
			Form<Void> form = new MyForm("form");
			localFeedbackContainer.add(form);
			add(localFeedbackContainer);
		}

		// Form with local feedback
		{
			LocalFeedbackContainer localFeedbackContainer = new LocalFeedbackContainer(
					"localFeedbackContainer2");
			localFeedbackContainer.add(new LocalFeedbackPanel(
					"localFeedbackPanel", localFeedbackContainer));
			Form<Void> form = new MyForm("form");
			localFeedbackContainer.add(form);
			add(localFeedbackContainer);
		}

		// Show message in global feedback
		info(getString("globalMessage"));
	}
}
