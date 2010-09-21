/* Hatabpanel.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Sep 17, 2010 11:44:33 AM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.hatab;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;

/**
 * @author simon
 *
 */
public class Horpanel extends XulElement {
	
	static {
		addClientEvent(Horpanel.class, Events.ON_SELECT, CE_IMPORTANT);
	}
	
	public Horbox getHorbox() {
		return null;
	}

	public int getIndex() {
		return 0;
	}
	
	
	
}
