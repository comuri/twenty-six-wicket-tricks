package com.locke.library.web.wow.extraction;

import org.apache.wicket.markup.html.panel.Panel;

public interface ILabeledPanelExtractor<M> extends IPanelExtractor<M> {

	/**
	 * @param id
	 *            Label id
	 * @return The label
	 */
	Panel label(String id);
}
