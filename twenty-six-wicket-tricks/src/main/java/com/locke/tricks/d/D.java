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
package com.locke.tricks.d;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.lang.Bytes;

import com.locke.Trick;
import com.locke.library.web.panels.attachment.AttachmentPanel;
import com.locke.library.web.panels.attachment.IAttachment;
import com.locke.library.web.panels.attachment.AttachmentPanel.Feature;

/**
 * Trick 'D'
 * 
 * @author Jonathan Locke
 */
@SuppressWarnings("serial")
public class D extends Trick {

	private List<IAttachment> attachments = new ArrayList<IAttachment>();

	public D() {
		add(new AttachmentPanel("attachmentPanel", getCss(), Bytes
				.kilobytes(256),
				new AbstractReadOnlyModel<List<IAttachment>>() {
			
					@Override
					public List<IAttachment> getObject() {
						return attachments;
					}
				}, Feature.POPUP_EDITOR_LINK, Feature.DOWNLOAD_LINK,
				Feature.REMOVE_LINK));
	}
}
