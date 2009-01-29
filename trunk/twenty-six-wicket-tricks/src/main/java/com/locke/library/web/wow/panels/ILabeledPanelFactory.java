package com.locke.library.web.wow.panels;

import org.apache.wicket.markup.html.panel.Panel;

public interface ILabeledPanelFactory<M> extends IPanelFactory<M> {

	/**
	 * @param id
	 *            Component id
	 * @return The label
	 */
	Panel newLabel(String id);
}
