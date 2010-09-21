/* Horbox.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
/**
 * A widget as horizontal accordion tabbox. Works like Tabbox in accordion mold,
 * but in horizontal direction. 
 * 
 * <p>Default {@link #getZclass}: z-horbox.
 */
hatab.Horbox = zk.$extends(zul.Widget, {
	
	$define: {
		/**
		 * 
		 */
		/**
		 * 
		 */
		tabWidth: _zkf = function () {
			this.rerender();
		}
		//TODO: set tab negative margin value
	},
	/**
	 * 
	 */
	getZclass: function () {
		return this._zclass == null ? "z-horbox" : this._zclass;
	},
	/**
	 * Returns the selected index.
	 * @return int
	 */
	getSelectedIndex: function() {
		return this._selPanel ? this._selPanel.getIndex() : -1 ;
	},
	/**
	 * Sets the selected index.
	 * @param int index
	 */
	setSelectedIndex: function(index) {
		this.setSelectedPanel(this.getChildAt(index));
	},
	/**
	 * Returns the selected panel.
	 * @return Panel
	 */
	getSelectedPanel: function() {
		return this._selPanel;
	},
	/**
	 * Sets the selected panel.
	 * @param Panel panel
	 */
	setSelectedPanel: function(panel) {
		if (typeof panel == 'string')
			panel = zk.Widget.$(panel);
		if (this._selPanel != panel) {
			if (panel)
				panel.setSelected(true);
				//it will set _selPanel (but we still set it later just in case)
			this._selPanel = panel;
		}
	},
	//TODO: check all below
	bind_: function (desktop, skipper, after) {
		this.$supers(hatab.Horbox, 'bind_', arguments);
		
		zWatch.listen({onResponse: this});
		
		var panel = this._selPanel;
		if (panel)
			after.push(function() {
				panel.setSelected(true);
			});
	},
	unbind_: function () {
		zWatch.unlisten({onResponse: this});
		this.$supers(hatab.Horbox, 'unbind_', arguments);
	},
	onResponse: function () {
		if (this._shallSize) {
			zWatch.fire('onSize', this);
			this._shallSize = false;
		}
	},
	_syncSize: function () {
		this._shallSize = true;
		if (!this.inServer && this.desktop)
			this.onResponse();
	},
	//super//
	removeChildHTML_: function (child) {
		this.$supers('removeChildHTML_', arguments);
		/*
		if (this.isVertical() && child.$instanceof(hatab.Hortabs))
			jq(child.uuid + '-line', zk).remove();
		*/
	},
	onChildAdded_: function (child) {
		this.$supers('onChildAdded_', arguments);
		/*
		if (child.$instanceof(hatab.Hortabs))
			this.tabs = child;
		else if (child.$instanceof(hatab.Horpanels)) {
			this.tabpanels = child;
		}
		*/
		this.rerender();
	},
	onChildRemoved_: function (child) {
		this.$supers('onChildRemoved_', arguments);
		/*
		if (child == this.toolbar)
			this.toolbar = null;
		else if (child == this.tabs)
			this.tabs = null;
		else if (child == this.tabpanels)
			this.tabpanels = null;
		if (!this.childReplacing_)
			this.rerender();
		*/
	},
	setWidth: function (width) {
		this.$supers('setWidth', arguments);
		if (this.desktop)
			zWatch.fireDown('onSize', this);
	},
	setHeight: function (height) {
		this.$supers('setHeight', arguments);
		if (this.desktop)
			zWatch.fireDown('onSize', this);
	}
});