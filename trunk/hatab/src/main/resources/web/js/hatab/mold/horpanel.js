/* horpanel.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
function(out) {
	// TODO: 1. move widget DOM to outer li
	// TODO: 2. merge push
	
	var uuid = this.uuid,
		zcls = this.getZclass(),
		horbox = this.getHorbox(),
		index = this.getIndex();
		tabwidth = horbox.getTabWidth(),
		buriedwidth = horbox.getTabBuriedWidth(),
		bgc = this.getBgcolor(),
		bgi = this.getBgimage(),
		panelStyle = bgc || bgi,
		title = '';
		//title = this.getTitle();
	
	out.push('<li class="', zcls, '-outer">');
	out.push('<div class="', zcls, '-tab" id="', uuid, '-tab"');
	if(tabwidth || (index && buriedwidth) || bgc || bgi) {
		out.push(' style="');
		if(tabwidth) 
			out.push('width:', tabwidth, ';');
		if(index && buriedwidth) 
			out.push('margin-left:-', buriedwidth, ';');
		if(bgc)
			out.push('background-color:', bgc, ';');
		if(bgi)
			out.push('background-image:url(', bgi, ');');
		out.push('"');
	}
	out.push('>', title, '</div>');
	out.push('<div ', this.domAttrs_());
	if(bgc || bgi){
		out.push(' style="');
		if(bgc)
			out.push('background-color:', bgc, ';');
		if(bgi)
			out.push('background-image:url(', bgi, ');'); // TODO: background-position
		out.push('"');
	}
	out.push('>');
	out.push('<div id="', uuid, '-cave" class="', zcls, '-cnt">');
	for (var w = this.firstChild; w; w = w.nextSibling)
		w.redraw(out);
	out.push('</div></div></li>');
}