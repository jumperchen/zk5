/*
 * Mtabs.java
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/9/20, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.swifttab;

import org.zkoss.swifttab.event.MoveTabEvent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabs;

public class Mtabs extends Tabs {
	/**
	 *
	 */
	private static final long serialVersionUID = 5598881168273491786L;

	static {
		addClientEvent(Swifttab.class, MoveTabEvent.NAME, CE_IMPORTANT
				| CE_NON_DEFERRABLE);
	}

	private boolean _noSmartUpdate = false;

	protected void addMoved(Component arg0, Page arg1, Page arg2) {
		if (!_noSmartUpdate) {
			super.addMoved(arg0, arg1, arg2);
		}
	}

	public boolean appendChild(Component component, boolean ignoreDom) {
		if (ignoreDom) {
			_noSmartUpdate = true;
		}

		boolean result = this.appendChild(component);

		if (ignoreDom) {
			_noSmartUpdate = false;
		}
		return result;

	}

	public boolean insertBefore(Component comp1, Component comp2,
			boolean ignoreDom) {
		if (ignoreDom) {
			_noSmartUpdate = true;
		}

		boolean result = this.insertBefore(comp1, comp2);

		if (ignoreDom) {
			_noSmartUpdate = false;
		}
		return result;
	}

	public void onTabMove(MoveTabEvent event) {
		int startIndex = event.getStartIndex();
		int endIndex = event.getEndIndex();
		/*
		 * panels.appendChild(panel,false); }else{ panels.insertBefore(panel,
		 * exchangedTab.getLinkedPanel(),false);
		 */

		if (!(getTabbox().getTabpanels() instanceof Mtabpanels)) {
			throw new UnsupportedOperationException(
					"mtabs should be with mpanels");
		}

		Mtabpanels panels = (Mtabpanels) getTabbox().getTabpanels();

		Tab startTab = (Tab) getChildren().get(startIndex);
		Tab exchangedTab = (Tab) getChildren().get(startIndex);

		if (endIndex == getChildren().size() - 1) {
			appendChild(startTab, true);
			panels.appendChild(startTab.getLinkedPanel(), true);
		} else {
			panels.insertBefore(startTab.getLinkedPanel(), exchangedTab
					.getLinkedPanel(), true);

			insertBefore(startTab, exchangedTab, true);

		}
	}

}
