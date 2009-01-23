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
package com.locke.tricks.c;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;

import com.locke.library.web.panels.caching.CachingPanel;

@SuppressWarnings("serial")
public class ExpensivePanel extends CachingPanel {

	public ExpensivePanel(String id) {
		super(id, Scope.APPLICATION);
		setMaximumContentAge(Duration.seconds(30));
		add(new Label("label", new AbstractReadOnlyModel<String>() {

			@Override
			public String getObject() {
				return Time.now().toString();
			}
		}));
	}
}
