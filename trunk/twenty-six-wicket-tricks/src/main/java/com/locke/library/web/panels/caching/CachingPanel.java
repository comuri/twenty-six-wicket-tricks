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
package com.locke.library.web.panels.caching;

import java.io.Serializable;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.response.StringResponse;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;

/**
 * Panel which only renders a given panel subclass every so often (according to
 * {@link #getMaximumContentAge()}) per application or per session.
 * <p>
 * For example, a subclass ExpensiveHomePagePanel might use
 * {@link Scope#APPLICATION} and a duration of 5 minutes to re-render only once
 * every five minutes for all users. At the same time, a subclass
 * ExpensiveUserHomePagePanel might use {@link Scope#SESSION} to only re-render
 * a user-specific panel once every 10 minutes.
 * 
 * @author Jonathan Locke
 */
public class CachingPanel extends Panel {

	private static final long serialVersionUID = 5388945891147087711L;

	private static final MetaDataKey<RenderState> STATE = new MetaDataKey<RenderState>() {
		private static final long serialVersionUID = -1370573271158586590L;
	};

	public enum Scope {
		APPLICATION, SESSION
	}

	private final Scope scope;

	public CachingPanel(String id, Scope scope) {
		super(id);
		this.scope = scope;
	}

	public CachingPanel(String id, IModel<?> model, Scope scope) {
		super(id, model);
		this.scope = scope;
	}

	public Duration getMaximumContentAge() {
		return getRenderState().maximumContentAge;
	}

	public void setMaximumContentAge(Duration maximumContentAge) {
		getRenderState().maximumContentAge = maximumContentAge;
	}

	@Override
	protected void onRender(MarkupStream markupStream) {
		final RequestCycle cycle = getRequestCycle();
		final Response response = cycle.getResponse();
		if (getRenderState().lastRenderedAt.elapsedSince().greaterThan(
				getRenderState().maximumContentAge)) {
			getRenderState().lastRenderedAt = Time.now();
			cycle.setResponse(getRenderState().buffer);
			super.onRender(markupStream);
			cycle.setResponse(response);
		} else {
			markupStream.skipComponent();
		}
		response.write(getRenderState().buffer.getBuffer());
	}

	private RenderState getRenderState() {
		if (scope == Scope.APPLICATION) {
			RenderState state = getApplication().getMetaData(STATE);
			if (state == null) {
				state = new RenderState();
				getApplication().setMetaData(STATE, state);
			}
			return state;
		}
		if (scope == Scope.SESSION) {
			RenderState state = getSession().getMetaData(STATE);
			if (state == null) {
				state = new RenderState();
				getSession().setMetaData(STATE, state);
			}
			return state;
		}
		throw new IllegalStateException("Invalid cache scope");
	}

	private class RenderState implements Serializable {
		private static final long serialVersionUID = -5015381829904041141L;
		private final StringResponse buffer = new StringResponse();
		private Time lastRenderedAt = Time.now();
		private Duration maximumContentAge = Duration.ONE_MINUTE;
	}
}
