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
package com.locke.library.web.panels.attachment;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;

import com.locke.library.web.generics.AjaxLink;
import com.locke.library.web.panels.attachment.AttachmentPanel.Feature;

public abstract class AttachmentModalWindowPage extends WebPage {

	private static final long serialVersionUID = -7106156537501789738L;

	public AttachmentModalWindowPage(final ModalWindow window,
			final HeaderContributor css, final Bytes maximumSize,
			final IModel<List<IAttachment>> model) {

		add(css);
		add(new AjaxLink("ok") {
			private static final long serialVersionUID = -1202322017634128297L;

			public void onClick(AjaxRequestTarget target) {
				onOk();
				window.close(target);
			}
		});

		add(new AjaxLink("cancel") {
			private static final long serialVersionUID = -6141617859994868658L;

			public void onClick(AjaxRequestTarget target) {
				window.close(target);
			}
		});

		add(new AttachmentPanel("attachments", css, maximumSize, model,
				Feature.REMOVE_LINK, Feature.UPLOAD_FORM) {
			private static final long serialVersionUID = -9208749445562106471L;

			@Override
			protected void onRemove(final IAttachment attachment) {
				model.getObject().remove(attachment);
				info(getString("removedAttachment", new Model<IAttachment>(
						attachment)));
			}

			@Override
			protected void onUpload(final IAttachment attachment) {
				model.getObject().add(attachment);
				info(getString("uploadedAttachment", new Model<IAttachment>(
						attachment)));
			}
		});
	}

	protected abstract void onOk();
}
