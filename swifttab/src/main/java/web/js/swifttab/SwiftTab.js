zk.$package("swifttab");
swifttab.SwiftTab = zk.$extends(zul.tab.Tab, {

	bind_: function(desktop, skipper, after){
		this.$supers('bind_', arguments);
		this.$n("sort");
	},
	unbind_: function(){
	},
	getZclass:function(){
		return (this._zclass !=null ? this._zclass : "z-swifttab");
	}
});
