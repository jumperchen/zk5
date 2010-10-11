/*  Wire.js
 *
	Purpose: To provide a movable and lighter tab.

	Description:
		Reference to WireIt JS Wiring Liberary, version 0.5.0.
		http://javascript.neyric.com/wireit/
	History:
		Dec 11, 2009 12:11:50 PM, Created by joy
		2010/9/27 , Updated by TonyQ

 Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function () {

zkex.wire.Wire = zk.$extends(zul.Widget, {
    _in:"",
    _out:"",
	$define: {
		config: null,
		joint: null
	},
    setIn: function(val){
        this._in=val;
    } ,
    getIn: function() {return zk.Widget.$(this._in);},
    setOut: function(val){
        this._out = val ;
    } ,
    getOut: function() {return zk.Widget.$(this._out);},
	bind_: function (desktop, skipper, after) {
		this.$supers(zkex.wire.Wire,'bind_', arguments);
	},

	unbind_: function () {
		this.$supers(zkex.wire.Wire,'unbind_', arguments);
	}

});
})();