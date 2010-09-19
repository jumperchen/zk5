(function(){

    function _getIndex(bounds, offset){
        for (var i = 1; i < bounds.length; ++i) {
            if (offset < bounds[i]) {
                return i - 1;
            }
        }
        return bounds.length - 1;

    }
    swifttab.Swifttab = zk.$extends(zul.tab.Tab, {
        isMovable: function(){
            if(this.parent != null)
                return swifttab.Mtabs.isInstance(this.getTabs());
            return false;
        },
        getTabs:function(){
            return this.parent;
        },
        _makeSortable: function(){
            var handle = this.$n("sort"), instance = jq(this.$n()),
            	currentTab = this, zcls = this.getZclass(),startIndex = -1,
            	sortIndex = -1, bounds = [] ,widths = [],
            	tabs = this.getTabs() , items ;

            if (handle && !this._drag) {
                handle.style.cursor = "move";
                this._drag = new zk.Draggable(this, null, {
                    handle: handle,
                    fireOnMove: false,
                    constrint: "horizontal",
                    ghosting: function(dg, ofs, evt){
                        var ghost = instance.clone();
                        ghost[0].style.position = "relative";
                        ghost.css("opacity", "0.8");
                        return ghost[0];
                    },
                    starteffect: function(dg){
                    	currentTab._sel();
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
                        sortIndex = _getIndex(bounds, instance.offset().left);
                        startIndex = sortIndex;
                        instance.after(dg.node);
                        instance[0].style.display = "none";
                    },
                    draw: function(dg, ofs, evt){
                        var exchange = false,indicator = _getIndex(bounds,
                        		widths[sortIndex] +	ofs[0] );
                        dg.node.style.left = ofs[0] + "px";
                        if (indicator != sortIndex) { // moved
                            //for last node
                            if (indicator == bounds.length - 1) {
                                items.eq(indicator - 1).after(dg.node);
                            } else {
                                items.eq(indicator).before(dg.node);
                            }

                            if (indicator > sortIndex) // move to right
                                dg.node.style.left = (ofs[0] -
                                    items.eq(sortIndex).width()) + "px";
                            else //move to left
                                 dg.node.style.left = (items.eq(sortIndex - 1).
                                     width() + ofs[0]) + "px";

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
                        currentTab.fire("onTabMove", {start:startIndex,
                        	end:sortIndex});
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
            this.$supers(swifttab.Swifttab, "unbind_", arguments);
            if (this._drag) {
                this._drag.destroy();
            }
        },
        getZclass: function(){
            return (this._zclass != null ? this._zclass : "z-swifttab");
        }
    });
})();
