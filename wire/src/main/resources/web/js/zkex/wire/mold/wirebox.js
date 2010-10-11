/*
 * wirebox.js
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
function (out, skipper) {
	var zcls = this.getZclass(),
		uuid = this.uuid;

	out.push('<div', this.domAttrs_(), '><div id="', uuid, '-cave" class="',zcls, '-cave">');
		for (var w = this.firstChild; w; w = w.nextSibling)
				w.redraw(out);

	out.push('</div></div>');
}