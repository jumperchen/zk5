(function(){
    //private  front_
    // protected end_
    function _getNextX(helper){
        if (helper.next("." + zcls).size() != 0) {
            return helper.next().offset().left;
        }
        return -1;
    }
    function _getPrevX(helper){
        if (helper.prev("." + zcls).size() != 0) {
            return helper.prev().offset().right;
        }
        return -1;
    }

    //TODO remove me
    function _inspectAll(){
        var datas = [];
        jq(".z-swifttab").each(function(){
            datas.push(jq(this).offset().left);
        });

        return (datas.join(";"));
    }
    swifttab.Swifttab = zk.$extends(zul.tab.Tab, {
        $define: {},
        $init: function(){
            //onChange
            //onSelect
            //onClose
        },
        bind_: function(desktop, skipper, after){
            this.$supers(swifttab.Swifttab, 'bind_', arguments);
            var handler = jq(this.$n("sort")), instance = jq(this.$n()), zcls = this.getZclass();

            handler.css("cursor", "move");
            handler.mousedown(function(){
                console.log("down");
                var helper = instance.clone(), nextX = 0, prevX = 0, currentX = 0;
                instance.after(helper);
                instance.hide();
                jq(window.body).after(instance);

                nextX = getNextX(helper);
                console.log(nextX);
                prevX = getPrevX(helper);
                currentX = helper.offset().left;

                helper.css("position", "relative").css("opacity", "0.2");
                jq(window).bind("mousemove.tab_drag", {
                    instance: instance,
                    helper: helper
                }, function(e){
                    //TODO check this
                    var newX = e.pageX - currentX;
                    helper.css("left", newX);
					console.log(newX);
                    if (nextX != -1 && newX > nextX) {
                        helper.next().after(helper);
                        helper.css("left", "0");
                        currentX = helper.offset().left;
                        nextX = getNextX(helper);
                        prevX = getPrevX(helper);
                        console.log(currentX + ":"+ e.pageX +":" +nextX+" , "+inspectAll());
                    } else if (prevX != -1 && newX < prevX) {
                        helper.prev().before(helper);
						helper.css("left", "0");
                        currentX = helper.offset().left;
                        nextX = getNextX(helper);
                        prevX = getPrevX(helper);
                    }
                });
                jq(window).mouseup(function(){
                    jq(window).unbind("mousemove.tab_drag");
                    console.log("up");
                    helper.after(instance);
                    instance.show();
                    helper.remove();
                });
                return false;
            });


        },
        unbind_: function(){
            //destroy
            this.$supers(swifttab.SwiftTab, "unbind_", arguments);
        },
        getZclass: function(){
            return (this._zclass != null) ? this._zclass : "z-swifttab";
        }
    });
})();
