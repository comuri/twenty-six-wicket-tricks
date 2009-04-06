package com.locke.library.web.wow.layouts;

import java.util.Iterator;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AbstractRepeaterLayout extends AbstractLayout {

	static final String CHILD_ID = "panel";

	private static final long serialVersionUID = -4138436487196089947L;

	private final IDataProvider<Panel> provider = new IDataProvider<Panel>() {

		private static final long serialVersionUID = 3137979826799455116L;

		public void detach() {
		}

		public Iterator<Panel> iterator(final int first, final int count) {
			return getPanels().subList(first, first + count).iterator();
		}

		public IModel<Panel> model(final Panel model) {
			return new Model<Panel>(model);
		}

		public int size() {
			return getPanels().size();
		}
	};

	public AbstractRepeaterLayout(final String id, final IPanelSource source) {
		super(id, source);

		// Add components from the component source
		for (final Panel panel : source.panels(new Iterator<String>() {
			public boolean hasNext() {
				return true;
			}

			public String next() {
				return CHILD_ID;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		})) {
			getPanels().add(panel);
		}
	}

	protected IDataProvider<Panel> getProvider() {
		return this.provider;
	}
}
