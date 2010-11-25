/* ToolPanelController.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		May 20, 2010 6:29:36 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package demo.canvas4z.zkpaint;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

/**
 * @author simon
 *
 */
public class ToolPanelController extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	
	// control
	private Listbox fontBox;
	private Listbox fillTypeBox;
	private Listbox strokeTypeBox;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// shape is set in CanvasBoardController
		
		// text
		fontBox.setSelectedIndex(0);
		
		// style
		fillTypeBox.setSelectedIndex(1);
		strokeTypeBox.setSelectedIndex(1);
		
	}
}
