/* Swifttab.js
 *
	Purpose: To provide a movable and lighter tab.

	Description:

	History:
		2010/9/20, Created by TonyQ

 Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function(){
    /**
     * To get the current index of tab from the items. (index start with 0 )
     * @param {Array} bounds the left position for each items
     *     (count with the parent offset left)
     * @param {Number} left for the current position
     *     (count with parent offset left)
     */
    function _getIndex(bounds, offset){
        for (var i = 1, len = bounds.length; i < len; ++i) {
            if (offset < bounds[i]) {
                return i - 1;
            }
        }
        return len - 1;

    }
    /**
     * the Swifttab widget definition
     */
    swifttab.Swifttab = zk.$extends(zul.tab.Tab, {
        /**
         * we consider movable from the tabs's type.
         */
        isMovable: function(){
            if (this.parent != null)
                return swifttab.Mtabs.isInstance(this.getTabs());
            return false;
        },
        getTabs: function(){
            return this.parent;
        },
        /**
         * init the draggable feature and prepare something
         */
        _makeSortable: function(){
            var handle = this.$n("sort");

            if (handle && !this._drag) {
                handle.style.cursor = "move";

                this._drag = new zk.Draggable(this, null, {
                    handle: handle,
                    scroll: this.getTabs().$n("header"),
                    scrollSpeed: 5,
                    fireOnMove: false,
                    constrint: "horizontal",
                    ghosting: swifttab.Swifttab._ghosting,
                    starteffect: swifttab.Swifttab._starteffect,
                    draw: swifttab.Swifttab._draw,
                    endghosting: swifttab.Swifttab._endghosting,
                    endeffect: swifttab.Swifttab._endeffect,
                    zIndex: 99999
                });
            }
        },
        bind_: function(desktop, skipper, after){
            this.$supers(swifttab.Swifttab, 'bind_', arguments);

            if (this.isMovable()) {
                this._makeSortable();
            }
        },
        unbind_: function(){
            if (this._drag) {
                this._drag.destroy();
                this._drag = null; //prevent the binding error.
            }
            this.$supers(swifttab.Swifttab, "unbind_", arguments);
        },
        /**
         * default zclass is "z-siwfttab"
         */
        getZclass: function(){
            return (this._zclass != null ? this._zclass : "z-swifttab");
        }
    }, {
        _ghosting: function(dg, ofs, evt){
            var ghost = jq(dg.control.$n()).clone();
            ghost[0].style.position = "relative";
            ghost.css("opacity", "0.8");
            return ghost[0];
        },
        _starteffect: function(dg){
            dg._startIndex = -1;

            // reload for closable tabs
            var instance = jq(dg.control.$n());
            dg._items = instance.parent().children("." +
            dg.control.getZclass()).not(instance);
            dg._bounds = [0];
            dg._widths = [0];
            dg._items.each(function(num){
                var width = jq(this).width();
                dg._bounds[dg._bounds.length] = (dg._widths[num] +
                parseInt(width / 2, 10));
                dg._widths[dg._widths.length] = dg._widths[num] + width;
            });
            //default behavior
            dg._sortIndex = dg.control.getChildIndex();//todo here
            dg._startIndex = dg._sortIndex;

            //fix for tabs scrolling
            instance.after(dg.node);
            instance.hide();
        },
        _draw: function(dg, ofs, evt){
                // I dont really know why *2 ,
                // but *2 is fiting the number . by TonyQ
            var currentOfsLeft = ofs[0] + dg.z_scrl[0] * 2,
                indicator = _getIndex( dg._bounds, dg._widths[dg._sortIndex] +
                currentOfsLeft);


            dg.node.style.left = (currentOfsLeft) + "px";
            if (indicator != dg._sortIndex) { // moved
                //for last node
                if (indicator == dg._bounds.length - 1)
                    dg._items.eq(indicator - 1).after(dg.node);
                else
                    dg._items.eq(indicator).before(dg.node);

                if (indicator > dg._sortIndex) // move to right
                    dg.node.style.left = (currentOfsLeft -
                    dg._items.eq(dg._sortIndex).width()) +
                    "px";
                else //move to left
                     dg.node.style.left = (dg._items.eq(dg._sortIndex - 1).
                         width() + currentOfsLeft) + "px";

                dg._sortIndex = indicator; //update location
            }
        },
        _endghosting: function(dg, origin){
            jq(dg.node).remove();
        },
        _endeffect: function(dg){
            var currentTab = dg.control,
                tabs = currentTab.parent,
                panel = currentTab.getLinkedPanel(),
                panels = panel.parent,
                exchangedTab = zk.Widget.$(dg._items.eq(dg._sortIndex));

            if (dg._sortIndex == tabs.nChildren - 1) {
                tabs.appendChild(currentTab, false);
                panels.appendChild(panel, false);
            } else {
                panels.insertBefore(panel, exchangedTab.getLinkedPanel(),
                    false);
                tabs.insertBefore(currentTab, exchangedTab, false);
                currentTab.show();
            }

            if (dg._startIndex != dg._sortIndex) {
                tabs.fire("onTabMove", {
                    start: dg._startIndex,
                    end: dg._sortIndex
                });
            }
        }
    });
})();
