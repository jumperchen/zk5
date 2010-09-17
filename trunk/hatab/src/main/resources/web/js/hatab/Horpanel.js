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
	/** Returns the horbox owns this component.
	 * @return Horbox
	 */
	getHorbox: function() {
		return this.parent ? this.parent.parent : null;
	},
	isVisible: function() {
		return this.$supers('isVisible', arguments) && this.isSelected();
	},
	getZclass: function() {
		return this._zclass? this._zclass : 'z-horpanel';
	},
	/** Returns the tab associated with this tab panel.
	 * @return Tab
	 */
	getLinkedTab: function() {
		var horbox =  this.getHorbox();
		if (!horbox) return null;
		
		var tabs = horbox.getTabs();
		return tabs ? tabs.getChildAt(this.getIndex()) : null;
	},
	/** Returns the index of this panel, or -1 if it doesn't belong to any
	 * tabpanels.
	 * @return int
	 */
	getIndex: function() {
		return this.getChildIndex();
	},
	/** Returns whether this tab panel is selected.
	 * @return boolean
	 */
	isSelected: function() {
		var tab = this.getLinkedTab();
		return tab && tab.isSelected();
	},
	// TODO: check all
	// Bug 3026669
	_changeSel: function (oldPanel) {
		if (oldPanel) {
			var cave = this.$n('cave');
			if (cave && !cave.style.height && (oldPanel = oldPanel.$n('cave')))
				cave.style.height = oldPanel.style.height;
		}
	},
	_sel: function (toSel, animation) { //don't rename (zkmax counts on it)!!
		if (animation) {
			var p = this.$n("real"); //accordion uses 'real'
			zk(p)[toSel ? "slideDown" : "slideUp"](this);
		} else {
			var $pl = jq(this.$n("real")),
				vis = $pl.zk.isVisible();
			if (toSel) {
				if (!vis) {
					$pl.show();
					zWatch.fireDown('onShow', this);
				}
			} else if (vis) {
				zWatch.fireDown('onHide', this);
				$pl.hide();
			}
		}
	},
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
	domClass_: function () {
		return this.$supers('domClass_', arguments) + ' ' + this.getZclass() + '-cnt';
	},
	onSize: _zkf = function() {
		var horbox = this.getHorbox();
		if (!zk(this.$n("real")).isVisible())
			return;
		this._fixPanelHgh(); //Bug 2104974
		if (zk.ie && !zk.ie8) zk(horbox.$n()).redoCSS(); //Bug 2526699 - (add zk.ie7)
	},
	onShow: _zkf,
	//bug #3014664
	setVflex: function (v) { //vflex ignored for Horpanel
		if (v != 'min') v = false;
		this.$super(zul.tab.Horpanel, 'setVflex', v);
	},
	//bug #3014664
	setHflex: function (v) { //hflex ignored for Horpanel
		if (v != 'min') v = false;
		this.$super(zul.tab.Horpanel, 'setHflex', v);
	},
	bind_: function() {
		this.$supers(zul.tab.Horpanel, 'bind_', arguments);
		if (this.getHorbox().isHorizontal()) {
			this._zwatched = true;
			zWatch.listen({onSize: this, onShow: this});
		}
	},
	unbind_: function () {
		if (this._zwatched) {
			zWatch.unlisten({onSize: this, onShow: this});
			this._zwatched = false;
		}
		this.$supers(zul.tab.Tabpanel, 'unbind_', arguments);
	}
});