/* Hatab.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Sep 16, 2010 12:08:04 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.hatab;

import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.EventListener;
//import org.zkoss.zul.Tabbox.Listener;
import org.zkoss.zul.impl.XulElement;

/**
 * 
 * @author simon
 */
public class Horbox extends XulElement{
	
	private transient Horpanels _hatabpanels;
	private transient Horpanel _selPanel;
	/** The event listener used to listen onSelect for each tab. */
	///* package */transient EventListener _listener;
	
	public Horpanels getHatabpanels(){
		return _hatabpanels;
	}
	
	/**
	 * 
	 * @return
	 */
	public Horpanel getSelectedPanel(){
		return _selPanel;
	}
	
	/**
	 * 
	 * @param panel
	 */
	public void setSelectedPanel(Horpanel panel){
		if (panel == null)
			throw new IllegalArgumentException("null tabpanel");
		if (panel.getHorbox() != this)
			throw new UiException("Not a child: " + panel);
		
		//_selPanel = panel;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSelectedIndex(){
		return _selPanel != null ? _selPanel.getIndex() : -1;
	}
	
	/**
	 * 
	 * @param j
	 */
	public void setSelectedIndex(int j){
		if(_hatabpanels == null)
			throw new IllegalStateException("No hatabpenals");
		setSelectedPanel((Horpanel) _hatabpanels.getChildren().get(j));
	}
	
}
