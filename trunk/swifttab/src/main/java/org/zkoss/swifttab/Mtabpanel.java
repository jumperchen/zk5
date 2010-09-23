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

	/**
	 * Here to prevent the dom update when parents is Mtabs and it decide to
	 * ignore response for this class.
	 *
	 * This is for the movable feature, because we will update client first and
	 * then update server component
	 *
	 * The add moved will be called when its parent insertBefore or appndChild
	 * @see Mtabs#service(org.zkoss.zk.au.AuRequest, boolean)
	 */
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
