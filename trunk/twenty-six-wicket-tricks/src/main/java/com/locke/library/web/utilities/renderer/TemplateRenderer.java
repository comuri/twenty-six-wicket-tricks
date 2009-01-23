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
package com.locke.library.web.utilities.renderer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;

/**
 * Renders template pages using a pool of background threads.
 * 
 * @author Jonathan Locke
 */
public class TemplateRenderer {

	/**
	 * Optional WebApplication to use when rendering template
	 */
	private final WebApplication application;

	/**
	 * Thread pool
	 */
	private final ExecutorService executor;

	/**
	 * Construct. Uses a dummy application to render. Unfortunately, this means
	 * your application's settings and mounts will not be available. However,
	 * you CAN use this constructor in a running web application.
	 */
	public TemplateRenderer() {
		this(null);
	}

	/**
	 * Construct. Unfortunately, this constructor cannot be used in a running
	 * web application (at least in Wicket 1.4) because the {@link WicketTester}
	 * object will change settings in the application passed in, which will
	 * cause your application to stop working. However, you CAN use this method
	 * in a separate VM if you want to do templating to send emails that way.
	 * Hopefully in the future, {@link WicketTester} and Wicket can be reworked
	 * to make it possible to use {@link WicketTester} inside a running web
	 * application.
	 * 
	 * @param application
	 *            The application to use when rendering
	 */
	public TemplateRenderer(WebApplication application) {
		this.application = application;
		executor = Executors.newFixedThreadPool(getThreadPoolSize());
	}

	/**
	 * @param page
	 *            The page to render
	 * @return The rendered web page as a String
	 */
	public String render(final WebPage page) {
		try {
			return executor.submit(new Callable<String>() {
				public String call() throws Exception {
					final WicketTester tester = application != null ? new WicketTester(
							application)
							: new WicketTester();
					tester.startPage(page);
					tester.assertNoErrorMessage();
					final String html = tester.getServletResponse()
							.getDocument();
					tester.destroy();
					return html;
				}
			}).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return Number of threads to do renderings with (renderings must happen
	 *         on a background thread because thread locals cannot be changed by
	 *         {@link WicketTester} or the current request would stop working)
	 */
	protected int getThreadPoolSize() {
		return 2;
	}
}
