/* horbox.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
function(out) {
	out.push('<div ', this.domAttrs_(), '>');
	if (this.horpanels) 
		this.horpanels.redraw(out);
	out.push("</div>");
}