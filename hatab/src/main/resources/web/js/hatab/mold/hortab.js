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
	
	// TODO: direction
	out.push('<div id="', this.uuid, '"', this.domAttrs_(), '>',
		'<div align="left" class="', zcls, '-header">');
	
	if (this.isClosable())
		out.push('<a id="', this.uuid, '-close" class="', zcls, '-close"></a>');
	
	// TODO: direction
	out.push('<div href="javascript:;" id="', this.uuid, '-tl" class="', zcls, '-tl">',
			'<div class="', zcls, '-tr">',
			'<span class="', zcls, '-tm">',
			'<span class="', zcls, '-text">', this.domContent_(),
			'</span></span></div></div></div></div>');
}