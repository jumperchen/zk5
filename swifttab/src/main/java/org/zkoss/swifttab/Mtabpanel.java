package org.zkoss.swifttab;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Tabpanel;

public class Mtabpanel extends Tabpanel {

	protected void addMoved(Component oldparent, Page oldpg, Page newpg) {

		if (getLinkedTab().getParent() instanceof Mtabs) {
			Mtabs tabs = (Mtabs) getLinkedTab().getParent();
			if (tabs.isNoResponse()) {
				return;
			}
		}

		super.addMoved(oldparent, oldpg, newpg);
	}

}
