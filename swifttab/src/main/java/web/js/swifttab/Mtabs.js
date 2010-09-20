/* Mtabs.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/
swifttab.Mtabs = zk.$extends(zul.tab.Tabs, {
    $init: function () {
		this.$supers('$init', arguments);
		this.listen({onTabMove: this}, -1000);
	},
    bind_: function(desktop, skipper, after){
        this.$supers(zul.tab.Tabs, 'bind_', arguments);
    },
    unbind_: function(){
        this.$supers(zul.tab.Tabs, "unbind_", arguments);
    },
    //event handler//
	onTabMove: function () {
        alert("hi");
    }
});
