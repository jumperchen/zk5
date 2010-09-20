/*
 * Mtabpanel.java
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
