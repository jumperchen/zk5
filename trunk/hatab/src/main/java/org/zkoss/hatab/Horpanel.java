/* Hatabpanel.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Sep 17, 2010 11:44:33 AM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.hatab;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

/**
 * 
 * @author simon
 */
public class Horpanel extends XulElement {
	
	private boolean _selected;
	
	// TODO: setBgcolor, setBgimage *
	// TODO: manage setHeight
	// TODO: _disabled
	
	static {
		addClientEvent(Horpanel.class, Events.ON_SELECT, CE_IMPORTANT);
	}
	
	// component logic //
	/**
	 * 
	 */
	public Horbox getHorbox() {
		return (Horbox) getParent();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndex() {
		final Horbox horbox = getHorbox();
		if (horbox == null)
			return -1;
		int j = 0;
		for (Iterator it = horbox.getChildren().iterator();; ++j)
			if (it.next() == this)
				return j;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return _selected;
	}

	/**
	 * Sets whether this horpanel is selected.
	 */
	public void setSelected(boolean selected) {
		if (_selected != selected) {
			final Horbox horbox = getHorbox();
			if (horbox != null) {
				// Note: we don't update it here but let its parent does the job
				horbox.setSelectedPanel(this);
			} else {
				_selected = selected;				
				smartUpdate("selected", _selected);
			}
		}
	}
	
	// helper //
	/*package*/ void setSelectedDirectly(boolean selected) {
		_selected = selected;
	}
	
	// component //
	public String getZclass() {
		return _zclass == null ? "z-horpanel" : _zclass;
	}
	
	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Horbox))
			throw new UiException("Wrong parent: "+parent);
		super.beforeParentChanged(parent);
	}
	
	public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
		final String cmd = request.getCommand();
		if (cmd.equals(Events.ON_SELECT)) {
			SelectEvent evt = SelectEvent.getSelectEvent(request);
			Set selItems = evt.getSelectedItems();
			if (selItems == null || selItems.size() != 1)
				throw new UiException("Exactly one selected tab is required: " + selItems); // debug purpose
			final Horbox horbox = getHorbox();
			if (horbox != null)
				horbox.selectPanelDirectly((Horpanel) selItems.iterator().next(), true);
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
		if (_selected)
			render(renderer, "selected", _selected);
	}
	
}
