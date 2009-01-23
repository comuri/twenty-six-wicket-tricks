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
package com.locke.library.web.panels.feedback;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class LocalFeedbackPanel extends FeedbackPanel {

	private static final long serialVersionUID = -2321986159300632847L;

	public LocalFeedbackPanel(String id, final LocalFeedbackContainer container) {
		super(id, new IFeedbackMessageFilter() {
			private static final long serialVersionUID = -1883211501894198761L;

			public boolean accept(FeedbackMessage message) {
				return container.contains(message.getReporter(), true);
			}
		});
	}
}
