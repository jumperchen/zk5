(function(){

    function _getNextX(helper, filter){
        if (helper.next(filter).size() != 0) {
            return helper.next().offset().left - helper.offset().left;
        }
        return -1;
    }
    function _getPrevX(helper, filter){
        if (helper.prev(filter).size() != 0) {
            return 0;
        }
        return 1;
    }

    swifttab.Swifttab = zk.$extends(zul.tab.Tab, {
		_updateBound:function(ghost){

		},
        _makeSortable: function(){
            var handle = this.$n("sort"), instance = jq(this.$n()), zcls = this.getZclass(), nextX = 0, prevX = 0 , lastBound;
            if (handle && !this._drag) {
                handle.style.cursor = "move";
                this._drag = new zk.Draggable(this, null, {
                    handle: handle,
                    fireOnMove: false,
                    constrint: "horizontal",
                    starteffect: function(){

                    },
                    ghosting: function(dg, ofs, evt){
                        var ghost = instance.clone();
                        ghost.css("position", "relative").css("opacity", "0.8");
                        instance.after(ghost);
                        instance.hide();
                        nextX = _getNextX(ghost, "." + zcls);
                        prevX = _getPrevX(ghost, "." + zcls);
                        return ghost[0];
                    },
                    draw: function(dg, ofs, evt){
                        var newX = ofs[0], jqNode = jq(dg.node);
                        jqNode.css("left", newX);
                        if (nextX != -1 && newX > nextX) {
                            jqNode.next().after(jqNode);
                            jqNode.css("left", "0");
                            nextX = _getNextX(jqNode, "." + zcls);
                            prevX = _getPrevX(jqNode, "." + zcls);
                        } else if (prevX <= 0 && newX < prevX) {
                            jqNode.prev().prev().before(jqNode);
							//one more prve because the instance is prev
                            jqNode.css("left", "0");
                            nextX = _getNextX(jqNode, "." + zcls);
                            prevX = _getPrevX(jqNode, "." + zcls);
                        }
                    },
                    endghosting: function(dg, origin){
                        var el = dg.node; //ghost
                        jq(el).after(instance);
                        instance.show();
                        jq(el).remove();
                    },
                    endeffect: function(){

                    },
                    zIndex: 99999 //Bug 2929590
                });
            }
        },
        bind_: function(desktop, skipper, after){
            this.$supers(swifttab.Swifttab, 'bind_', arguments);
            this._makeSortable();
        },
        unbind_: function(){
            this.$supers(swifttab.Swifttab, "unbind_", arguments);
        },
        getZclass: function(){
            return (this._zclass != null ? this._zclass : "z-swifttab");
        }
    });
})();
