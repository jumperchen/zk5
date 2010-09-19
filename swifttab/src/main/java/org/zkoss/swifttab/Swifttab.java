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
