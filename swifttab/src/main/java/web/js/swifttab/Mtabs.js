swifttab.Mtabs = zk.$extends(zul.tab.Tabs, {
    $init: function () {
		this.$supers('$init', arguments);
		this.listen({onChange: this}, -1000);
	},
    bind_: function(desktop, skipper, after){
        this.$supers(zul.tab.Tabs, 'bind_', arguments);
    },
    unbind_: function(){
        this.$supers(zul.tab.Tabs, "unbind_", arguments);
    },
    //event handler//
	onClose: function () {
        alert("hi");
    }
});
