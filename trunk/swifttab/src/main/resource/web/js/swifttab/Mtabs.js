/* Mtabs.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
swifttab.Mtabs = zk.$extends(zul.tab.Tabs, {
    $init: function () {
		this.$supers(swifttab.Mtabs,'$init', arguments);
	},
    bind_: function(desktop, skipper, after){
        this.$supers(swifttab.Mtabs, 'bind_', arguments);
    },
    unbind_: function(){
        this.$supers(swifttab.Mtabs, "unbind_", arguments);
    }
});
