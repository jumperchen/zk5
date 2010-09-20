/* swifttab.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
function (out) {
	var zcls = this.getZclass(),
		uuid = this.uuid;
	out.push('<li ', this.domAttrs_(), '>','<a class="', zcls, '-link">','<span class="', zcls, '-text">');

	if(this.isMovable()) {
		out.push('<span id="', uuid, '-sort" class="', zcls, '-sort">||</span> ');
	}
	out.push( this.domContent_(), '</span>' );
	if (this.isClosable())
		out.push('<a id="', uuid, '-close" class="', zcls, '-close"', 'onClick="return false;" ></a>');
	out.push('</a></li>');

}