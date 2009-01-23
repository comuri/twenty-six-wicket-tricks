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
package com.locke.tricks.v;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

import com.locke.Trick;
import com.locke.library.locator.Visit;
import com.locke.library.web.generics.Link;
import com.locke.library.web.locators.ComponentMatcher;
import com.locke.library.web.locators.FormComponentMatcher;
import com.locke.library.web.panels.feedback.GlobalFeedbackPanel;
import com.locke.library.web.visitors.error.ShowError;
import com.locke.tricks.f.ErrorFocusingTestForm;

/**
 * Trick 'V'
 * 
 * @author Jonathan Locke
 */
@SuppressWarnings("serial")
public class V extends Trick {

	public V() {

		final RepeatingView view = new RepeatingView("labels");
		for (int i = 0; i < 5; i++) {
			view.add(new Label(view.newChildId(), "label" + i)
					.setOutputMarkupId(true));
		}
		add(view);

		add(new Link("red") {

			@Override
			public void onClick() {
				new Visit(new ComponentMatcher(view), new Redden());
			}
		});

		add(new GlobalFeedbackPanel("feedback"));
		add(new ErrorFocusingTestForm("form") {

			@Override
			protected void onError() {
				super.onError();
				new Visit(new FormComponentMatcher(this), new ShowError(
						"fieldError"));
			}
		});
	}
}
