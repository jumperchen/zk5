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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

public class Mtabs extends Tabs {

	/**
	 *
	 */
	private static final long serialVersionUID = 5598881168273491786L;

	static {
		addClientEvent(Mtabs.class, MoveTabEvent.NAME, CE_IMPORTANT | CE_NON_DEFERRABLE);
	}

	private boolean _noResponse = false;

	private boolean _swiftable = true;

	public Mtabs() {
		addEventListener(MoveTabEvent.NAME, tabMoveEventListener);
	}

	private final EventListener tabMoveEventListener = new EventListener() {

		public void onEvent(Event evt) throws Exception {
			MoveTabEvent event = (MoveTabEvent) evt;
			int startIndex = event.getStartIndex();
			int endIndex = event.getEndIndex();

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
					panels.insertBefore(startTab.getLinkedPanel(), referTab.getLinkedPanel());

					insertBefore(startTab, referTab);

				}

				getTabbox().setSelectedIndex(0);
				getTabbox().setSelectedTab(startTab);

			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				_noResponse = false;
			}

		}
	};

	protected boolean isNoResponse() {
		return _noResponse;
	}

	public boolean isSwiftable() {
		return _swiftable;
	}

	public void setSwiftable(boolean swiftable) {
		this._swiftable = swiftable;
		if (swiftable) {
			this.addEventListener(MoveTabEvent.NAME, tabMoveEventListener);
		} else {
			this.removeEventListener(MoveTabEvent.NAME, tabMoveEventListener);
		}
	}

	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();

		if (cmd.equals(MoveTabEvent.NAME)) {
			MoveTabEvent evt = MoveTabEvent.getMoveTabEvent(request);
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

}
