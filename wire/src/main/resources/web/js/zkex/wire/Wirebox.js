/*  Wirewindow.js
 *
	Purpose: To provide a movable and lighter tab.

	Description:
		Reference to WireIt JS Wiring Liberary, version 0.5.0.
		http://javascript.neyric.com/wireit/
	History:
		2010/9/27 , created by TonyQ

 Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function () {
    zkex.wire.Wirebox = zk.$extends(zk.Widget, {
         $define: {
        },
        bind_: function(desktop, skipper, after) {
            this.$supers(zkex.wire.Wirebox, 'bind_', arguments);
		},
        unbind_: function () {
    		this.$supers(zkex.wire.Wirebox,'unbind_', arguments);
            //this.domListen_(node, 'onMouseOver').domListen_(node, 'onMouseOut');
        }
    	//,_doMouseOver: function (evt) {
    	//	if (this._terminalElement)
    	//		jq(this._terminalElement).show();
    	//},
    	//_doMouseOut: function (evt) {
    	//}
    })
})();