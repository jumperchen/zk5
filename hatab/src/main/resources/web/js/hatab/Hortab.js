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
 * <p>Default {@link #getZclass}: z-hortab.
 */
hatab.Hortab = zk.$extends(zul.LabelImageWidget, {
	$init: function () {
		this.$supers('$init', arguments);
		this.listen({onClose: this}, -1000);
	},
	$define: {
		/**
		 * Returns whether this tab is closable. If closable, a button is displayed
		 * and the onClose event is sent if an user clicks the button.
		 * <p>
		 * Default: false.
		 * @return boolean
		 */
		/**
		 * Sets whether this tab is closable. If closable, a button is displayed and
		 * the onClose event is sent if an user clicks the button.
		 * <p>
		 * Default: false.
		 * @param boolean closable
		 */
		closable: _zkf = function() {
			this.rerender();
		},
		image: _zkf,
		/**
		 * Returns whether this tab is disabled.
		 * <p>
		 * Default: false.
		 * @return boolean
		 */
		/**
		 * Sets whether this tab is disabled. If a tab is disabled, then it cann't
		 * be selected or closed by user, but it still can be controlled by server
		 * side program.
		 * @param boolean disabled
		 */
		disabled: _zkf, // TODO: check
		/**
		 * Returns whether this tab is selected.
		 * @return boolean
		 */
		/**
		 * Sets whether this tab is selected.
		 * @param boolean selected
		 */
		selected: function(selected) {
			this._sel();
		}
	},
	/**
	 * Returns the horbox which owns this component.
	 * @return Horbox
	 */
	getHorbox: function() {
		return this.parent ? this.parent.parent : null;
	},
	/**
	 * Returns the index of this panel, or -1 if it doesn't belong to any tabs.
	 * @return int
	 */
	getIndex: function() {
		return this.getChildIndex();
	},
	getZclass: function() {
		return this._zclass == null ? "z-hortab" : this._zclass;
	},
	/**
	 * Returns the panel associated with this tab.
	 * @return Tabpanel
	 */
	getLinkedPanel: function() {
		var w;
		return (w = this.getHorbox()) && (w = w.getHorpanels()) ?
			w.getChildAt(this.getIndex()): null;
	},
	_doCloseClick : function(evt) {
		if (!this._disabled) {
			this.fire('onClose');
			evt.stop();
		}
	},
	_toggleBtnOver : function(evt) { // kept for now
		jq(evt.domTarget).toggleClass(this.getZclass() + "-close-over");
	},
	_sel: function(notify, init) {
		var horbox = this.getHorbox();
		if (!horbox) return;
	
		var	oldtab = horbox._selTab;
		if (oldtab != this || init) {
			if (oldtab) {
				var p = this.getLinkedPanel();
				if (p) p._changeSel(oldtab.getLinkedPanel());
			}
			if (oldtab && oldtab != this)
				this._setSel(oldtab, false, false, init);
			this._setSel(this, true, notify, init);
		}
	},
	_setSel: function(tab, toSel, notify, init) {
		var horbox = this.getHorbox(),
			zcls = this.getZclass(),
			panel = tab.getLinkedPanel(),
			bound = this.desktop;
		if (tab.isSelected() == toSel && notify)
			return;
	
		if (toSel)
			horbox._selTab = tab; //avoid loopback
		tab._selected = toSel;
		
		if (!bound) return;
		
		if (toSel)
			jq(tab).addClass(zcls + "-seld");
		else
			jq(tab).removeClass(zcls + "-seld");
	
		if (panel)
			panel._sel(toSel, !init);
	
		if (tab == this) {
			if (horbox.isVertical())
				tabs._scrollcheck("vsel", this); // TODO check this
		}
		
		if (notify)
			this.fire('onSelect', {items: [this], reference: this.uuid});
	},
	setHeight: function (height) {
		this.$supers('setHeight', arguments);
		if (this.desktop) {
			zWatch.fireDown('beforeSize', this.parent);
			zWatch.fireDown('onSize', this.parent);
		}
	},
	setWidth: function (width) {
		this.$supers('setWidth', arguments);
		if (this.desktop) {
			zWatch.fireDown('beforeSize', this.parent);
			zWatch.fireDown('onSize', this.parent);
		}
	},
	// TODO: check all, swap x, y
	//protected
	doClick_: function(evt) {
		if (this._disabled)
			return;
		this._sel(true);
		this.$supers('doClick_', arguments);
	},
	domClass_: function (no) {
		var scls = this.$supers('domClass_', arguments);
		if (!no || !no.zclass) {
			var added = this.isDisabled() ? this.getZclass() + '-disd' : '';
			if (added) scls += (scls ? ' ': '') + added;
		}
		return scls;
	},
	domContent_: function () {
		var label = zUtl.encodeXML(this.getLabel()),
			img = this.getImage();
		if (!label) label = '&nbsp;';
		if (!img) return label;
		img = '<img src="' + img + '" align="absmiddle" class="' + this.getZclass() + '-img"/>';
		return label ? img + ' ' + label: img;
	},
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Tab
		if (v != 'min') v = false;
		this.$super(zul.tab.Tab, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Tab
		if (v != 'min') v = false;
		this.$super(zul.tab.Tab, 'setHflex', v);
	},
	bind_: function (desktop, skipper, after) {
		this.$supers(zul.tab.Tab, 'bind_', arguments);
		var closebtn = this.$n('close'),
			tab = this;
		if (closebtn) {
			this.domListen_(closebtn, "onClick", '_doCloseClick');
			if (!closebtn.style.cursor)
				closebtn.style.cursor = "default";
			if (zk.ie6_)
				this.domListen_(closebtn, "onMouseOver", '_toggleBtnOver')
					.domListen_(closebtn, "onMouseOut", '_toggleBtnOver');
		}
	
		after.push(function () {tab.parent._fixHgh();});
			//Bug 3022274: required so it is is called before, say, panel's slideDown
			//_sel will invoke _fixWidth but it is too late since it uses afterMount
		after.push(function () {
			zk.afterMount(function () {
				if (tab.isSelected()) 
					tab._sel(false, true);
			});
		});
	},
	unbind_: function () {
		var closebtn = this.$n('close');
		if (closebtn) {
			this.domUnlisten_(closebtn, "onClick", '_doCloseClick');
			if (zk.ie6_)
				this.domUnlisten_(closebtn, "onMouseOver", '_toggleBtnOver')
					.domUnlisten_(closebtn, "onMouseOut", '_toggleBtnOver');
		}
		this.$supers(zul.tab.Tab, 'unbind_', arguments);
	},
	//event handler//
	onClose: function () {
		if (this.isSelected()) {
			var self = this,
				p = this.parent;
			
			// Bug 2931212, send onSelect after onClose
			setTimeout(function () {
				if (!self.parent || self.parent != p)
					return; // nothing to do
				for (var tab = self; tab = tab.nextSibling;)
					if (!tab.isDisabled()) {
						tab._sel(true);
						return;
					}
				for (var tab = self; tab = tab.previousSibling;)
					if (!tab.isDisabled()) {
						tab._sel(true);
						return;
					}
			});
		} else if (this.getHorbox().inAccordionMold()) {
			this.getHorbox()._syncSize();
		}
	}
});