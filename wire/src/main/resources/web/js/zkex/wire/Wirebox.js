/*
 * Wirebox.js
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function() {
    zkex.wire.Wirebox = zk.$extends(zk.Widget, {
        _points:'',
        $define: {
            pointstate: {},
        },
        _updatePoints: function() {
            var offset = jq(this.$n()).offset();
            for (var point in this._pointstate) {
                this._pointstate[point].updatePosition(offset);
            }
        },
        setPoints:function(p){
            if (this.$n()) {
                var terms = (p || "").split(",");
                for (var i = 0, len = terms.length; i < len; ++i) {
                    if (zkex.wire.Point.allowPoint(terms[i])) {
                        this.addPoint(terms[i], true);
                    } else if (terms[i] == zkex.wire.Wirebox.POINT_ALL) {
                        for (var point in zkex.wire.Wirebox.POINTS) {
                            this.addPoint(point, true);
                        }
                        this._updatePoints();
                        this._points = zkex.wire.Wirebox.POINT_ALL;
                        return;
                    } else {
                        zk.error("wirebox didnt support point[" + terms[i] + "]");
                    }
                }
                this._updatePoints();
            }
            this._points = p || "";
       },
        _checkPoint: function(point) {
            return zkex.wire.Point.allowPoint(point) && !this._pointstate[point];
        },
        _removePoint:function(point){
            if (this._pointstate[point]) {
                this._pointstate[point].destroy();
                delete this._pointstate[point] ;
            }
        },
        getPointPosition_:function(point){
            if (this._pointstate[point]) {
                return this._pointstate[point].getCenterOffset();
            }
        },
        addPoint: function(point,ignoreUpdatePosition) {
            var term,scissors,success = false;
            if (point == zkex.wire.Wirebox.POINT_ALL) {
                for (var point in zkex.wire.Wirebox.POINTS) {
                    this.addPoint(point,true);
                }
                success = true;
            }
            if (this._checkPoint(point)) {
                var pointel = new zkex.wire.Point(this,this.uuid+"-point-"+point,this.getZclass()+"-point",point);
                this._pointstate[point] = pointel;
                success = true;
            }
            if(!ignoreUpdatePosition && success){
                this._updatePoints();
            }
            return success;
        },
        $init: function() {
            this.$supers(zkex.wire.Wirebox, '$init', arguments);
            this._pointstate = {};
        },
        bind_: function(desktop, skipper, after) {
            this.$supers(zkex.wire.Wirebox, 'bind_', arguments);
            if(this._points) this.setPoints(this._points);
        },
        getZclass: function() {
            return this._zclass || "z-wirebox";
        },
        unbind_: function() {
            this.$supers(zkex.wire.Wirebox, 'unbind_', arguments);
        },
    }, {
        POINT_ALL: "*",
        STATE_WAIT: 1,
        STATE_CONNECTED: 2
    })
})();
