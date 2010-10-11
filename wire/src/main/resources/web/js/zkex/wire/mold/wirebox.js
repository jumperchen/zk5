/* wire.js

	Purpose:

	Description:

	History:
		Dec 11, 2009 16:52:58 PM , Created by joy

Copyright (C) 2009 Potix Corporation. All Rights Reserved.
*/

function (out, skipper) {
	var zcls = this.getZclass(),
		uuid = this.uuid;

	out.push('<div', this.domAttrs_(), '><div id="', uuid, '-cave" class="',zcls, '-cave">');
		for (var w = this.firstChild; w; w = w.nextSibling)
				w.redraw(out);

	out.push('</div></div>');
}