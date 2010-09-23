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
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 *
 * This is a tab box for movable(which you can drag them easily) and more
 * swiftly tab implements.
 *
 * @listen onTabMove(start:nStartIndex,end:nEndIndex)
 */
public class Mtabs extends Tabs {

	private static final long serialVersionUID = 5598881168273491786L;

	static {
		addClientEvent(Mtabs.class, MoveTabEvent.NAME, CE_IMPORTANT | CE_NON_DEFERRABLE);
	}

	private boolean _noResponse = false;

	/**
	 * Trigger after user drag and move the index.
	 *
	 * @param event
	 * @see MoveTabEvent#getMovedTab()
	 */
	public void onTabMove(MoveTabEvent event) {
	}

	/**
	 * To change the tab's index , from startIndex to endIndex ,
	 * index start with 0
	 *
	 * @param startIndex
	 *            which the tab to move
	 * @param endIndex
	 *            where the tab move to
	 */
	private void changeTab(int startIndex, int endIndex) {

		if (startIndex == endIndex)
			return;

		int refer = endIndex > startIndex ? endIndex + 1 : endIndex;

		Tabpanels panels = (Tabpanels) getTabbox().getTabpanels();

		Tab startTab = (Tab) getChildren().get(startIndex);
		try {
			_noResponse = true;

			if (refer == getChildren().size()) {
				panels.appendChild(startTab.getLinkedPanel());
				appendChild(startTab);
			} else {
				Tab referTab = (Tab) getChildren().get(refer);
				panels.insertBefore(startTab.getLinkedPanel(),
						referTab.getLinkedPanel());

				insertBefore(startTab, referTab);

			}

			// avoid selected not work problem.
			// because it change the selected status , but you can't re-set it agagin ,
			// so you need to change the index and change back that again.
			getTabbox().setSelectedIndex( startIndex == 0 ? 1 : 0 );
			getTabbox().setSelectedTab(startTab);

		} finally {
			_noResponse = false;
		}
	}

	/**
	 * This method is for Mpanel and Swifttab to consider if they should update
	 * html , while tabs append new childs.
	 *
	 * @return
	 */
	/* package */boolean isNoResponse() {
		return _noResponse;
	}

	/**
	 * Here to handle onTabMove event.
	 */
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();

		if (cmd.equals(MoveTabEvent.NAME)) {
			MoveTabEvent evt = MoveTabEvent.getMoveTabEvent(request);
			changeTab(evt.getStartIndex(), evt.getEndIndex());
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

}
