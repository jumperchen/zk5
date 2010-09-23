/* Swifttab.js

	Purpose:

	Description:

	History:
		2010/9/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/(function(){
    /**
     * To get the current index of tab from the items. (index start with 0 )
     * @param {Array} bounds the left offset for each items(count with the parent offset left)
     * @param {Number} offset for the current offset(count with parent offset left)
     */
    function _getIndex(bounds, offset){
        for (var i = 1 ,len = bounds.length; i < len; ++i) {
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
         * we consider movable from the parent tabs.
         */
        isMovable: function(){
            if(this.parent != null)
                return swifttab.Mtabs.isInstance(this.getTabs());
            return false;
        },
        getTabs:function(){
            return this.parent;
        },
        /**
         * init the draggable feature and prepare something
         */
        _makeSortable: function(){
            var handle = this.$n("sort");

            if (handle && !this._drag) {
                var instance = jq(this.$n()),
            	currentTab = this, zcls = this.getZclass(),startIndex = -1,
            	sortIndex = -1, bounds = [] ,widths = [],
            	tabs = this.getTabs() , items ;//TODO
                handle.style.cursor = "move";
                this._drag = new zk.Draggable(this, null, {
                    handle: handle,
                    scroll: tabs.$n("header"),
                    scrollSpeed:5,
                    fireOnMove: false,
                    constrint: "horizontal",
                    ghosting: function(dg, ofs, evt){
                        var ghost = instance.clone();
                        ghost[0].style.position = "relative";
                        ghost.css("opacity", "0.8");
                        return ghost[0];
                    },
                    starteffect: function(dg){
                        // reload for closable tabs
                        items = instance.parent().children("." + zcls ).
                            not(instance);
                        bounds = [0];
                        widths = [0];
                        items.each(function(num){
                            var width = jq(this).width();
                            bounds[bounds.length] = (widths[num] +
                                parseInt(width / 2, 10));
                            widths[widths.length] = widths[num] + width;
                        });
                        //default behavior
                        sortIndex = zk.Widget.$(instance).getChildIndex();
                        startIndex = sortIndex;

                        //fix for tabs scrolling
                        instance.after(dg.node);
                        instance[0].style.display = "none";
                    },
                    draw: function(dg, ofs, evt){
                        var exchange = false,
                            // i dont really know why *2 ,
                            // but *2 is fiting the number
                            currentOfsLeft = ofs[0] +  dg.z_scrl[0]*2 ,
                            indicator = _getIndex(bounds,
                                   widths[sortIndex] +	currentOfsLeft );


                        dg.node.style.left = (currentOfsLeft) + "px";
                        if (indicator != sortIndex) { // moved
                            //for last node
                            if (indicator == bounds.length - 1) {
                                items.eq(indicator - 1).after(dg.node);
                            } else {
                                items.eq(indicator).before(dg.node);
                            }

                            if (indicator > sortIndex) // move to right
                                dg.node.style.left = (currentOfsLeft -
                                    items.eq(sortIndex).width()) + "px";
                            else //move to left
                                 dg.node.style.left = (items.eq(sortIndex - 1).
                                     width() + currentOfsLeft) + "px";

                            sortIndex = indicator; //update location
                        }
                    },
                    endghosting: function(dg, origin){
                        var el = dg.node; //ghost
                        jq(el).remove();
                    },
                    endeffect: function(dg){
                    	//update widgets
                		var panel = currentTab.getLinkedPanel() ,
                			panels = panel.parent ,
                			exchangedTab = zk.Widget.$(items.eq(sortIndex)) ;

                    	if(sortIndex == tabs.nChildren - 1){
                    		tabs.appendChild(currentTab,false);
                    		panels.appendChild(panel,false);
                    	}else{
                    		panels.insertBefore(panel,
                    				exchangedTab.getLinkedPanel(),false);
                    		tabs.insertBefore(currentTab,exchangedTab,false);
                    	}
                		instance.show();

                        if (startIndex != sortIndex) {
                            tabs.fire("onTabMove", {
                                start: startIndex,
                                end: sortIndex
                            });
                        }
                    },
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
    });
})();
