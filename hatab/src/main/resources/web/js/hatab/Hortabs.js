/* Horpanel.js

	Purpose:
		Horizontal Accordion Tabbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
/**
 * 
 * <p>Default {@link #getZclass}: z-hortabs.
 */
hatab.Hortabs = zk.$extends(zul.Widget, {
	// TODO: remove anything about default mold
	// swap x, y logic
	// no scrolling issues
	/** Returns the tabbox owns this component.
	 * @return Tabbox
	 */
	getTabbox: function() {
		return this.parent;
	},
	/**
	 * 
	 */
	getZclass: function() {
		return this._zclass == null ? "z-hortabs" : this._zclass;
	},
	/**
	 * 
	 */
	onSize: _zkf = function () {
		this._fixWidth();
	},
	onShow: _zkf,
	insertChildHTML_: function (child, before, desktop) {
		var last = child.previousSibling;
		if (before) 
			jq(before).before(child.redrawHTML_());
		else if (last) 
			jq(last).after(child.redrawHTML_());
		else {
			var edge = this.$n('edge');
			if (edge) 
				jq(edge).before(child.redrawHTML_());
			else
				jq(this.getCaveNode()).append(child.redrawHTML_());
		}
		child.bind(desktop);
	},
	domClass_: function (no) {
		return this.$supers('domClass_', arguments);
	},
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Tabs
		if (v != 'min') v = false;
		this.$super(hatab.Hortabs, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Tabs
		if (v != 'min') v = false;
		this.$super(hatab.Hortabs, 'setHflex', v);
	},
	bind_: function (desktop, skipper, after) {
		this.$supers(hatab.Hortabs, 'bind_', arguments);
		zWatch.listen({onSize: this, onShow: this});
		
		// reset
		this._inited = false;
		
		var self = this;
		after.push(
			function () {
				self._inited = true;
			}
		);
	},
	unbind_: function () {
		zWatch.unlisten({onSize: this, onShow: this});
		this.$supers(hatab.Hortabs, 'unbind_', arguments);
	},
	_isInited: function () {
		return this._inited;
	},
	/*
	_fixWidth: function() {
		var tabs = this.$n();
		
		var	tabbox = this.getTabbox(),
			tbx = tabbox.$n(),
			cave = this.$n("cave"),
			head = this.$n("header"),
			l = this.$n("left"),
			r = this.$n("right"),
			btnsize = tabbox._scrolling ? l && r ? l.offsetWidth + r.offsetWidth : 0 : 0;
			this._fixHgh();
			if (this.parent.isVertical()) {
				var most = 0;
				//LI in IE doesn't have width...
				if (tabs.style.width) {
					tabs._width = tabs.style.width;
					;
				} else {
					//vertical tabs have default width 50px
					this._forceStyle(tabs, "w", tabs._width ? tabs._width : "50px");
				}
			} else if (!tabbox.inAccordionMold()) {
				if (tbx.offsetWidth < btnsize) 
					return;
				if (tabbox.isTabscroll()) {
					var toolbar = tabbox.toolbar;
					if (toolbar) 
						toolbar = toolbar.$n();
					if (!tbx.style.width) {
						this._forceStyle(tbx, "w", "100%");
						this._forceStyle(tabs, "w", jq.px0(jq(tabs).zk.revisedWidth(tbx.offsetWidth)));
						if (tabbox._scrolling) 
							this._forceStyle(head, "w", jq.px0(tbx.offsetWidth - (toolbar ? toolbar.offsetWidth : 0) - btnsize));
						else 
							this._forceStyle(head, "w", jq.px0(jq(head).zk.revisedWidth(tbx.offsetWidth - (toolbar ? toolbar.offsetWidth : 0))));
					} else {
						this._forceStyle(tabs, "w", jq.px0(jq(tabs).zk.revisedWidth(tbx.offsetWidth)));
						this._forceStyle(head, "w", tabs.style.width);
						if (tabbox._scrolling) 
							this._forceStyle(head, "w", jq.px0(head.offsetWidth - (toolbar ? toolbar.offsetWidth : 0) - btnsize));
						else 
							this._forceStyle(head, "w", jq.px0(head.offsetWidth - (toolbar ? toolbar.offsetWidth : 0)));
					}
					if (toolbar && tabbox._scrolling) 
						this.$n('right').style.right = toolbar.offsetWidth + 'px';
				} else {
					if (!tbx.style.width) {
						if (tbx.offsetWidth) {
							var ofw = jq.px0(tbx.offsetWidth);
							this._forceStyle(tbx, "w", ofw);
							this._forceStyle(tabs, "w", ofw);	
						}
					} else {
						this._forceStyle(tabs, "w", jq.px0(tbx.offsetWidth));
					}
				}
			}
	},
	_fixHgh: function () {
		var tabs = this.$n(),
			tabbox = this.getTabbox(),
			tbx = tabbox.$n(),
			head = this.$n("header"),
			u = this.$n("up"),
			d = this.$n("down"),
			cave =  this.$n("cave"),
			btnsize = u && d ? isNaN(u.offsetHeight + d.offsetHeight) ? 0 : u.offsetHeight + d.offsetHeight : 0;
		//fix tabpanels's height if tabbox's height is specified
		//Ignore accordion since its height is controlled by each tabpanel
		if (tabbox.isVertical()) {
			var child = jq(tbx).children('div');
			allTab = jq(cave).children();
			if (tbx.style.height) {
				this._forceStyle(tabs, "h", jq.px0(jq(tabs).zk.revisedHeight(tbx.offsetHeight, true)));
			} else {
				this._forceStyle(tbx, "h", jq.px0(allTab.length * 35));//give it default height
				this._forceStyle(tabs, "h", jq.px0(jq(tabs).zk.revisedHeight(tbx.offsetHeight, true)));
			}
			//coz we have to make the header full
			if (tabbox._scrolling) {
				this._forceStyle(head, "h", jq.px0(tabs.offsetHeight - btnsize));
			} else {
				this._forceStyle(head, "h", jq.px0(jq(head).zk.revisedHeight(tabs.offsetHeight, true)));
			}
			//separator(+border)
			this._forceStyle(child[1], "h", jq.px0(jq(child[1]).zk.revisedHeight(tabs.offsetHeight, true)));
			//tabpanels(+border)
			this._forceStyle(child[2], "h", jq.px0(jq(child[1]).zk.revisedHeight(tabs.offsetHeight, true)));
		} else {
			if (head) //accordion have no head
				head.style.height = "";
		}
	},
	*/

	_forceStyle: function(node, attr, value) {
		if (!value)	return;
		switch (attr) {
		case "h":
			node.style.height = zk.ie6_ ? "0px" : ""; // recalculate for IE6
			node.style.height = value;
			break;
		case "w":
			node.style.width = zk.ie6_ ? "0px" : ""; // recalculate for IE6
			node.style.width = "";
			node.style.width = value;
			break;
		}
	},

	onChildRemoved_: function (child) {
		var p = this.parent;
		if (p && child == p._selTab)
			p._selTab = null;
		this._scrollcheck("init");
		this.$supers("onChildRemoved_", arguments);
	},
	onChildAdded_: function () {
		this._scrollcheck("init");
		this.$supers("onChildAdded_", arguments);
	},
	
	ignoreFlexSize_: function(attr) {
		var p = this.getTabbox();
		return (p.isVertical() && 'h' == attr)
			|| (p.isHorizontal() && 'w' == attr); 
	}
});