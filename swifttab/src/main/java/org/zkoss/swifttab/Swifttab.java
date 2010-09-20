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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tab;

public class Swifttab extends Tab {
	protected void addMoved(Component oldparent, Page oldpg, Page newpg) {

		if(this.getParent() instanceof Mtabs ){
			Mtabs tabs = (Mtabs) getParent();
			if(tabs.isNoResponse()){
				return;
			}
		}
		super.addMoved(oldparent, oldpg, newpg);
	}
}
