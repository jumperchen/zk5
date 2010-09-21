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

import java.io.IOException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

/**
 * 
 * @author simon
 */
public class Horbox extends XulElement{
	
	private transient Horpanel _selPanel;
	/** The event listener used to listen onSelect for each tab. */
	///* package */transient EventListener _listener;
	
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
		//setSelectedPanel((Horpanel) _hatabpanels.getChildren().get(j));
	}
	
	/**
	 * 
	 */
	public String getZclass() {
		return _zclass == null ? "z-horbox" : _zclass;
	}
	
	
	
	public void beforeChildAdded(Component child, Component insertBefore) {
		// TODO Auto-generated method stub
		super.beforeChildAdded(child, insertBefore);
	}
	
	public void beforeParentChanged(Component parent) {
		// TODO Auto-generated method stub
		super.beforeParentChanged(parent);
	}
	
	public boolean insertBefore(Component arg0, Component arg1) {
		// TODO Auto-generated method stub
		// select newly added tab
		return super.insertBefore(arg0, arg1);
	}

	protected void renderProperties(ContentRenderer renderer)
			throws IOException {
		super.renderProperties(renderer);
		
		// TODO
		
	}
	
}
