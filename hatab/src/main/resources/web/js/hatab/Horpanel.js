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
	
	// TODO: bgcolor, bgimage
	
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
		selected: function(selected) {
			this._sel();
		}
	},
	/** Returns the horbox owns this component.
	 * @return Horbox
	 */
	getHorbox: function() {
		return this.parent ? this.parent.parent : null;
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
				//this._changeSel(oldpanel);
				this._setSel(oldpanel, false, false, init);
			}
			this._setSel(this, true, notify, init);
		}
	},
	/*
	// Bug 3026669
	_changeSel: function (oldPanel) {
		if (oldPanel) {
			var cave = this.$n('cave');
			if (cave && !cave.style.width && (oldPanel = oldPanel.$n('cave')))
				cave.style.width = oldPanel.style.width;
		}
	},
	*/
	_setSel: function(panel, toSel, notify, init) {
		var horbox = this.getHorbox(),
			zcls = this.getZclass(),
			bound = this.desktop,
			tab = this.$n('tab');
		if (panel.isSelected() == toSel && notify)
			return;

		if (toSel)
			horbox._selPanel = panel; //avoid loopback
		panel._selected = toSel;
		
		if (!bound) return;
		
		if (toSel) {
			jq(panel).addClass(zcls + "-seld");
			jq(tab).addClass(zcls + "-tab-seld");
		} else {
			jq(panel).removeClass(zcls + "-seld");
			jq(tab).removeClass(zcls + "-tab-seld");
		}
		
		panel._selAnima(toSel, !init);
		
		if (notify)
			this.fire('onSelect');
			//this.fire('onSelect', {items: [this], reference: this.uuid});
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
	/*
	_fixPanelHgh: function() {
		var horbox = this.getHorbox();
		var tbx = horbox.$n(),
		hgh = tbx.style.height;
		if (hgh && hgh != "auto") {
   			var n = this.$n(),
   				hgh = zk(tbx).revisedHeight(tbx.offsetHeight);
   			hgh = zk(n.parentNode).revisedHeight(hgh);
   			for (var e = n.parentNode.firstChild; e; e = e.nextSibling)
   				if (e != n) hgh -= e.offsetHeight;
   			hgh -= n.firstChild.offsetHeight;
   			hgh = zk(n.lastChild).revisedHeight(hgh);
   			if (zk.ie8)
   				hgh -= 1; // show the bottom border
   			var cave = this.getCaveNode();
   			cave.style.height = jq.px0(hgh);
       		if (zk.ie && !zk.ie8) {
       			var s = cave.style,
       			z = s.zoom;
       			s.zoom = 1;
       			s.zoom = z;
       			s.overflow = 'hidden';
       		}
		}
	},
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