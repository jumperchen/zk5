/*
 * Mtabpanels.java
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
import org.zkoss.zul.Tabpanels;

public class Mtabpanels extends Tabpanels {
	/**
	 *
	 */
	private static final long serialVersionUID = 4710013927740971691L;
	private boolean _stopSmartUpdate = false;

	protected void addMoved(Component arg0, Page arg1, Page arg2) {
		if (!_stopSmartUpdate) {
			super.addMoved(arg0, arg1, arg2);
		}
	}

	public boolean appendChild(Component component, boolean ignoreDom) {
		if (ignoreDom) {
			_stopSmartUpdate = true;
		}

		boolean result = this.appendChild(component);

		if (ignoreDom) {
			_stopSmartUpdate = false;
		}
		return result;

	}

	public boolean insertBefore(Component comp1, Component comp2,
			boolean ignoreDom) {
		if (ignoreDom) {
			_stopSmartUpdate = true;
		}

		boolean result = this.insertBefore(comp1, comp2);

		if (ignoreDom) {
			_stopSmartUpdate = false;
		}
		return result;
	}
}
