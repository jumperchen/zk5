/* Swifttab.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
swifttab.Mtabpanel = zk.$extends(zul.tab.Tabpanel, {
    $init: function () {
		this.$supers(swifttab.Mtabpanel,'$init', arguments);
	},
    bind_: function(desktop, skipper, after){
        this.$supers(swifttab.Mtabpanel, 'bind_', arguments);
    },
    unbind_: function(){
        this.$supers(swifttab.Mtabpanel, "unbind_", arguments);
    }

});
