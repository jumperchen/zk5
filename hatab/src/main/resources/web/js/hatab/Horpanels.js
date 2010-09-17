/* Horpanels.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
/**
 * 
 * <p>Default {@link #getZclass}: z-horpanels.
 */
hatab.Horpanels = zk.$extends(zul.Widget, {
	
	getHorbox: function() {
		return this.parent;
	},
	getZclass: function() {
		return this._zclass? this._zclass : 'z-horpanels';
	},
	// TODO: check all
	// setHeight
	/*
	setWidth: function (val) {
		var n = this.$n(),
			tabbox = this.getTabbox(),
			isVer = n && tabbox ? tabbox.isVertical() : false;
		if (isVer && !this.__width)
			n.style.width = '';
	
		this.$supers('setWidth', arguments);
		
		if (isVer) {
			if (n.style.width)
				this.__width = n.style.width;
				
			zWatch.fireDown('beforeSize', this);
			zWatch.fireDown('onSize', this);
		}
	},
	setStyle: function (val) {
		var n = this.$n(),
			tabbox = this.getTabbox(),
			isVer = n && tabbox ? tabbox.isVertical() : false;
		if (isVer && !this.__width) {
			n.style.width = '';
		}
		this.$supers('setStyle', arguments);
	
		if (isVer) {
			if (n.style.width)
				this.__width = n.style.width;
				
			zWatch.fireDown('beforeSize', this);
			zWatch.fireDown('onSize', this);
		}
	},
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Tabpanels
		if (v != 'min') v = false;
		this.$super(zul.tab.Tabpanels, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Tabpanels
		if (v != 'min') v = false;
		this.$super(zul.tab.Tabpanels, 'setHflex', v);
	},
	bind_: function () {
		this.$supers(zul.tab.Tabpanels, 'bind_', arguments);
		if (this.getTabbox().isVertical()) {
			this._zwatched = true;
			zWatch.listen({onSize: this, beforeSize: this, onShow: this});			
			var n = this.$n();
			if (n.style.width)
				this.__width = n.style.width;
		}
	},
	unbind_: function () {
		if (this._zwatched) {
			zWatch.unlisten({onSize: this, beforeSize: this, onShow: this});
			this._zwatched = false;
		}
		this.$supers(zul.tab.Tabpanels, 'unbind_', arguments);
	},
	onSize: _zkf = function () {
		var parent = this.parent.$n();
		if (this.__width || !zk(parent).isRealVisible())
			return;
	
		var width = parent.offsetWidth,
			n = this.$n();
	
		width -= jq(parent).find('>div:first')[0].offsetWidth
				+ jq(n).prev()[0].offsetWidth;
	
		n.style.width = jq.px0(zk(n).revisedWidth(width));
	},
	onShow: _zkf,
	beforeSize: function () {
		this.$n().style.width = this.__width || '';
	}
	*/
		
});