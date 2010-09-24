/* Horpanel.js

	Purpose:
		Horizontal Accordion Horbox
		
	Description:
		
	History:
		Sep 16, 2010 11:35:04 AM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
*/
/**
 * 
 * <p>Default {@link #getZclass}: z-horpanel.
 */
hatab.Horpanel = zk.$extends(zul.Widget, {
	
	// TODO: title
	
	$define: {
		/**
		 * 
		 */
		/**
		 * 
		 */
		/*
		title: function() {
			this.rerender();
		},
		*/
		/**
		 * 
		 */
		/**
		 * 
		 */
		bgcolor: _zkf = function() {
			this.rerender();
		},
		/**
		 * 
		 */
		/**
		 * 
		 */
		bgimage: _zkf,
		/**
		 * 
		 */
		/**
		 * 
		 */
		selected: function(selected) {
			this._sel();
		}
	},
	/** Returns the horbox owns this component.
	 * @return Horbox
	 */
	getHorbox: function() {
		return this.parent;
	},
	/**
	 * 
	 */
	getZclass: function() {
		return this._zclass? this._zclass : 'z-horpanel';
	},
	/** Returns the index of this panel, or -1 if it doesn't belong to any
	 * tabpanels.
	 * @return int
	 */
	getIndex: function() {
		return this.getChildIndex();
	},
	_sel: function (notify, init) {
		var horbox = this.getHorbox();
		if (!horbox) return;
		var oldpanel = horbox._selPanel;
		if (oldpanel != this || init) {
			if (oldpanel && oldpanel != this) {
				this._setSel(oldpanel, false, false, init);
			}
			this._setSel(this, true, notify, init);
		}
	},
	_setSel: function(panel, toSel, notify, init) {
		var horbox = this.getHorbox(),
			zcls = this.getZclass(),
			bound = this.desktop,
			tab = panel.$n('tab');
		if (panel.isSelected() == toSel && notify)
			return;

		if (toSel)
			horbox._selPanel = panel; //avoid loopback
		panel._selected = toSel;
		
		if (!bound) return;
		
		if (toSel) {
			jq(panel).addClass(zcls + "-seld");
			jq(tab).addClass(zcls + "-tab-seld");
			// set width
			panel._fixPanelWidth();
		} else {
			jq(panel).removeClass(zcls + "-seld");
			jq(tab).removeClass(zcls + "-tab-seld");
		}
		
		panel._selAnima(toSel, !init);
		
		if (notify)
			this.fire('onSelect', {items: [this], reference: this.uuid});
	},
	_selAnima: function (toSel, animation) {
		// animation
		if (animation) {
			var p = this.$n();
			zk(p)[toSel ? "slideDown" : "slideUp"](this, {anchor:"l"});
		} else {
			var $pl = jq(this.$n());
				//vis = $pl.zk.isVisible();
			if (toSel) {
				//if (!vis) {
					$pl.show();
					zWatch.fireDown('onShow', this);
				//}
			} else {
				zWatch.fireDown('onHide', this);
				$pl.hide();
			}
		}
	},
	_fixPanelWidth: function() {
		var horbox = this.getHorbox(),
			hb = horbox.$n(),
			width = zk(hb).revisedWidth(hb.offsetWidth),
			cave = this.$n('cave');
		for (var w = horbox.firstChild; w; w = w.nextSibling) {
			width -= w.$n('tab').offsetWidth;
			// TODO: add margin left back
		}
		// TODO: subtract padding
		cave.style.width = jq.px0(width);
	},
	/*
	onSize: _zkf = function() {
		var horbox = this.getHorbox();
		if (!zk(this.$n("real")).isVisible())
			return;
		this._fixPanelHgh(); //Bug 2104974
		if (zk.ie && !zk.ie8) zk(horbox.$n()).redoCSS(); //Bug 2526699 - (add zk.ie7)
	},
	onShow: _zkf,
	*/
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Horpanel
		if (v != 'min') v = false;
		this.$super(hatab.Horpanel, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Horpanel
		if (v != 'min') v = false;
		this.$super(hatab.Horpanel, 'setHflex', v);
	},
	bind_: function(desktop, skipper, after) {
		this.$supers(hatab.Horpanel, 'bind_', arguments);
		
		var tab = this.$n('tab');
		if (tab)
			this.domListen_(tab, "onClick", '_doTabClick');
		
		var panel = this;
		after.push(function () {
			zk.afterMount(function () {
    			if (panel.isSelected()) 
    				panel._sel(false, true);
			});
		});
		/*
		if (this.getHorbox().isHorizontal()) {
			this._zwatched = true;
			zWatch.listen({onSize: this, onShow: this});
		}
		*/
	},
	unbind_: function () {
		var tab = this.$n('tab');
		if (tab)
			this.domUnlisten_(tab, "onClick", '_doTabClick');
		/*
		if (this._zwatched) {
			zWatch.unlisten({onSize: this, onShow: this});
			this._zwatched = false;
		}
		*/
		this.$supers(hatab.Horpanel, 'unbind_', arguments);
	},
	_doTabClick : function(evt) {
		//if (this._disabled) return;
		this._sel(true);
		this.$supers('doClick_', arguments);
	}
});