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
		horbox = this.getHorbox(),
		tabwidth = horbox._tabWidth,
		buriedwidth = horbox._tabBuriedWidth,
		title = '';
		//title = this.getTitle();
		// TODO: set style for tabwidth and tabburiedwidth
	out.push('<li class="', zcls, '-outer">');
	out.push('<div class="', zcls, '-tab" id="', uuid, '-tab">', title, '</div>');
	out.push('<div ', this.domAttrs_(), '>');
	out.push('<div id="', uuid, '-cave" class="', zcls, '-cnt">');
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	out.push('</div></div></li>');
}