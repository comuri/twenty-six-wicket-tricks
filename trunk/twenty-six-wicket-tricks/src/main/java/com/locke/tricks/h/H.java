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
package com.locke.tricks.h;

import org.apache.wicket.markup.html.basic.Label;

import com.locke.Trick;
import com.locke.library.web.components.labels.HtmlLabel;

/**
 * Trick 'H'
 * 
 * @author Jonathan Locke
 */
public class H extends Trick {

	public H() {
		add(new Label("escaped",
				"This is a normal Label, which always displays <i>escaped</i> <b>HTML</b>"));
		add(new HtmlLabel("raw",
				"This is an HtmlLabel, which can display <i>raw</i> <b>HTML</b>"));
	}
}
