/* swifttab.js

	Purpose:   To render the html with swifttab , support movable / closable feature.

	Description:

	History:
		2010/9/20, Created by TonyQ
		2010/9/21, updated by TonyQ - purpose

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
function (out) {
	var zcls = this.getZclass(),
		uuid = this.uuid;
	out.push('<li ', this.domAttrs_(), '>','<a class="', zcls, '-link">','<span class="', zcls, '-text">');

	if (this.getMovable()) {
        //for ie6 problem for float will break the background ,
        //so here we use padding-left + absolute to do tab-sorter.
		out.push('<span id="', uuid, '-move" class="', zcls, '-move"></span> ',
            '<span class="',zcls,'-content">',this.domContent_(),'</span></span>' );
	}else{
        out.push( this.domContent_(), '</span>' );
    }

	if (this.isClosable())
		out.push('<a id="', uuid, '-close" class="', zcls, '-close"', 'onClick="return false;" ></a>');
	out.push('</a></li>');

}