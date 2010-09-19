/* horpanel.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
function(out) {
	var uuid = this.uuid,
		zcls = this.getZclass(),
		tab = this.getLinkedTab();
	
	out.push('<li class="', zcls, '-outer" id="', uuid, '">');
	if (tab) tab.redraw(out);
	out.push('<div id="', uuid, '-real"', this.domAttrs_({id:1}), '>',
			'<div id="', uuid, '-cave" class="', zcls, '-cnt">');
	
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	
	out.push('</div></div></li>');
}