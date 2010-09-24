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

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

/**
 * 
 * @author simon
 */
public class Horbox extends XulElement{
	
	// TODO:
	// 3. transient
	// 4. insertBefore
	// 5. put style in domStyle_()
	// 6. move widget dom to outer
	
	private transient Horpanel _selPanel;
	private String _tabWidth;
	private String _tabBuriedWidth;
	
	// attributes //
	/**
	 * 
	 * @return
	 */
	public String getTabwidth(){
		return _tabWidth;
	}
	
	/**
	 * 
	 * @param tabWidth
	 */
	public void setTabwidth(String tabWidth){
		if (tabWidth != null && tabWidth.length() == 0)
			tabWidth = null;

		if (Objects.equals(_tabWidth, tabWidth)) return;
		_tabWidth = tabWidth;
		smartUpdate("tabWidth", _tabWidth);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTabburiedwidth(){
		return _tabBuriedWidth;
	}
	
	/**
	 * 
	 * @param tabBuriedWidth
	 */
	public void setTabburiedwidth(String tabBuriedWidth){
		if (tabBuriedWidth != null && tabBuriedWidth.length() == 0)
			tabBuriedWidth = null;

		if (Objects.equals(_tabBuriedWidth, tabBuriedWidth)) return;
		_tabBuriedWidth = tabBuriedWidth;
		smartUpdate("tabBuriedWidth", _tabBuriedWidth);
	}
	
	// component logic //
	/**
	 * 
	 * @return
	 */
	public Horpanel getSelectedPanel(){
		return _selPanel;
	}
	
	/**
	 * 
	 * @param horpanel
	 */
	public void setSelectedPanel(Horpanel horpanel){
		selectPanelDirectly(horpanel, false);
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
		setSelectedPanel((Horpanel) getChildren().get(j));
	}
	
	// helper //
	/*package*/ void selectPanelDirectly(Horpanel horpanel, boolean byClient){
		if (horpanel == null)
			throw new IllegalArgumentException("null tab");
		if (horpanel.getHorbox() != this)
			throw new UiException("Not my child: " + horpanel);
		if (horpanel != _selPanel) {
			if (_selPanel != null)
				_selPanel.setSelectedDirectly(false);

			_selPanel = horpanel;
			_selPanel.setSelectedDirectly(true);
			if (!byClient)
				smartUpdate("selectedPanel", _selPanel.getUuid());
		}
	}
	
	// component //
	public String getZclass() {
		return _zclass == null ? "z-horbox" : _zclass;
	}
	
	public void beforeChildAdded(Component child, Component refChild) {
		if (!(child instanceof Horpanel))
			throw new UiException("Unsupported child for tabs: "+child);
		super.beforeChildAdded(child, refChild);
	}
	
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
		if(_selPanel == null && !getChildren().isEmpty())
			setSelectedIndex(getChildren().size() - 1);
		
		render(renderer, "tabWidth", _tabWidth);
		render(renderer, "tabBuriedWidth", _tabBuriedWidth);
	}
	
}
