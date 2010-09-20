/*
 * Swifttab.java
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

public class Swifttab extends Tab {
	public void service(AuRequest request, boolean everError) {
		final String cmd = request.getCommand();

		if (cmd.equals(MoveTabEvent.NAME)) {
			MoveTabEvent evt = MoveTabEvent.getMoveTabEvent(request);
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}
}
