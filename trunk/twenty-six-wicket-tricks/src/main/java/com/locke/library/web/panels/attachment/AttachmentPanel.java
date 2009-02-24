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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.resource.ByteArrayResource;
import org.apache.wicket.util.lang.Bytes;

import com.locke.library.utilities.io.stream.StreamTooLongException;
import com.locke.library.utilities.io.stream.Streams;
import com.locke.library.web.generics.AjaxLink;
import com.locke.library.web.generics.GenericPanel;
import com.locke.library.web.generics.ResourceLink;
import com.locke.library.web.models.NullModel;
import com.locke.library.web.panels.feedback.LocalFeedbackContainer;
import com.locke.library.web.panels.feedback.LocalFeedbackPanel;

public class AttachmentPanel extends GenericPanel<List<IAttachment>> {

	private static final long serialVersionUID = -6099385202190709755L;

	public enum Feature {
		DOWNLOAD_LINK, POPUP_EDITOR_LINK, REMOVE_LINK, UPLOAD_FORM
	}

	private final Feature[] features;
	private final Bytes maximumSize;

	/**
	 * @param id
	 *            Component id
	 * @param css
	 *            Contributor to add css
	 * @param maximumSize
	 *            Maximum size of uploads
	 * @param model
	 *            List of attachments to edit
	 * @param features
	 *            Any features to enable
	 */
	public AttachmentPanel(String id, final HeaderContributor css,
			final Bytes maximumSize, final IModel<List<IAttachment>> model,
			Feature... features) {

		super(id, model);

		// Save maximum size
		this.maximumSize = maximumSize;

		// Save features list
		this.features = features;

		// To enable list updating
		setOutputMarkupId(true);

		// Local feedback for attachment uploading and deleting messages
		LocalFeedbackContainer container = new LocalFeedbackContainer(
				"feedbackContainer");
		container.add(new LocalFeedbackPanel("feedback", container));

		// Add list of attachments
		container.add(new AttachmentList("attachments"));

		// Add upload form
		container.add(new AttachmentUploadForm("uploadForm"));

		// Modal window to pop up when editing attachments
		final ModalWindow modalWindow = new AttachmentModalWindow(
				"popupEditorModalWindow", css, getPopupWidth(),
				getPopupHeight(), isPopupResizable());
		container.add(modalWindow);

		// Add edit link
		container.add(new AjaxLink("popupEditorLink") {

			private static final long serialVersionUID = 1104804510460622982L;

			@Override
			public void onClick(final AjaxRequestTarget target) {
				modalWindow.show(target);
			}

			@Override
			public boolean isVisible() {
				return featureEnabled(Feature.POPUP_EDITOR_LINK);
			}
		});

		// Add local feedback container
		add(container);
	};

	protected int getPopupHeight() {
		return 300;
	}

	protected int getPopupWidth() {
		return 500;
	}

	protected boolean isPopupResizable() {
		return true;
	}

	protected void onRemove(final IAttachment attachment) {
	}

	protected void onUpload(final IAttachment attachment) {
	}

	private boolean featureEnabled(Feature feature) {
		for (Feature current : features) {
			if (current == feature) {
				return true;
			}
		}
		return false;
	}

	private List<IAttachment> getAttachments() {
		return getModelObject();
	}

	private class AttachmentList extends ListView<IAttachment> {

		private static final long serialVersionUID = 4357037374832242757L;

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public AttachmentList(final String name) {
			super(name, AttachmentPanel.this.getModel());
			setReuseItems(false);
			setVersioned(false);
		}

		@Override
		public boolean isVisible() {
			return getAttachments().size() > 0;
		}

		/**
		 * @see ListView#populateItem(ListItem)
		 */
		protected void populateItem(final ListItem<IAttachment> listItem) {
			final IAttachment attachment = listItem.getModelObject();
			final ResourceLink link = new ResourceLink("downloadLink",
					new ByteArrayResource(attachment.getContentType(),
							attachment.getData(), attachment.getName())) {
				private static final long serialVersionUID = -8612670802816986666L;

				@Override
				public boolean isEnabled() {
					return featureEnabled(Feature.DOWNLOAD_LINK);
				}
			};
			listItem.add(link);
			link.add(new Label("name", attachment.getName()));
			listItem.add(new Label("size", " ("
					+ Bytes.bytes(attachment.getData().length) + ")"));
			listItem.add(new AjaxLink("remove") {
				private static final long serialVersionUID = -2716516281399252302L;

				@Override
				public boolean isVisible() {
					return featureEnabled(Feature.REMOVE_LINK);
				}

				@Override
				public void onClick(final AjaxRequestTarget target) {
					getAttachments().remove(listItem.getModelObject());
					target.addComponent(AttachmentPanel.this);
				}
			});
		}
	}

	private class AttachmentModalWindow extends ModalWindow {

		private static final long serialVersionUID = -1865276493502357112L;

		public AttachmentModalWindow(String id, final HeaderContributor css,
				final int width, final int height, final boolean resizable) {
			super(id);
			setPageMapName("modalWindow");
			setCookieName("modalWindow");
			setResizable(resizable);
			if (!resizable) {
				setInitialWidth(width);
				setInitialHeight(height);
				setWidthUnit("px");
				setHeightUnit("px");
			}
			setPageCreator(new ModalWindow.PageCreator() {

				private static final long serialVersionUID = 8076667038722998101L;

				public Page createPage() {
					final List<IAttachment> copy = new ArrayList<IAttachment>();
					copy.addAll(getAttachments());
					final IModel<List<IAttachment>> modelCopy = new AbstractReadOnlyModel<List<IAttachment>>() {
						private static final long serialVersionUID = -4695271045570185200L;

						@Override
						public List<IAttachment> getObject() {
							return copy;
						}
					};
					return new AttachmentModalWindowPage(
							AttachmentModalWindow.this, css, maximumSize,
							modelCopy) {
						private static final long serialVersionUID = -2023485724726353163L;

						@Override
						protected void onOk() {
							getAttachments().clear();
							getAttachments().addAll(copy);
						}
					};
				}
			});
			setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
				private static final long serialVersionUID = 5647310174048827545L;

				public void onClose(AjaxRequestTarget target) {
					target.addComponent(AttachmentPanel.this);
				}
			});
		}
	}

	private class AttachmentUploadForm extends Form<Void> {

		private static final long serialVersionUID = 7108943320263760353L;

		public AttachmentUploadForm(final String id) {
			super(id);
			setMultiPart(true);
			setMaxSize(maximumSize);
			final FileUploadField fileUploadField = new FileUploadField(
					"fileUploadField", new NullModel<FileUpload>());
			fileUploadField.setRequired(true);
			add(fileUploadField);
			add(new Button("upload") {
				private static final long serialVersionUID = 3735462214777763584L;

				@Override
				public void onSubmit() {
					final FileUpload upload = fileUploadField.getFileUpload();
					if (upload.getSize() > maximumSize.bytes()) {
						error(String.format("uploadTooBig", maximumSize
								.toString()));
						return;
					}
					final String name = upload.getClientFileName();
					for (IAttachment current : getAttachments()) {
						if (current.getName().equalsIgnoreCase(name)) {
							error(getString("alreadyUploaded",
									new Model<IAttachment>(current)));
							return;
						}
					}
					final String contentType = upload.getContentType();
					final ByteArrayOutputStream out = new ByteArrayOutputStream();
					try {
						Streams.copy(upload.getInputStream(), out, maximumSize);
					} catch (StreamTooLongException e) {
						error(String.format(getString("uploadTooBig"),
								maximumSize.toString()));
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
					final byte[] data = out.toByteArray();
					if (upload != null) {
						final IAttachment attachment = new IAttachment() {
							private static final long serialVersionUID = 675645047249073211L;

							public String getContentType() {
								return contentType;
							}

							public byte[] getData() {
								return data;
							}

							public String getName() {
								return name;
							}
						};
						onUpload(attachment);
					}
				}
			});
		}

		@Override
		public boolean isVisible() {
			return featureEnabled(Feature.UPLOAD_FORM);
		}
	}
}
