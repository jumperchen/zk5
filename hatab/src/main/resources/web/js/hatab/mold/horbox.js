/* horbox.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
function(out) {
	var zcls = this.getZclass(),
		uuid = this.uuid;
	
	out.push('<div ', this.domAttrs_(), '><ul id="', uuid, '-list" class="', zcls, '-list">');
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	out.push('</ul></div>');
}