/*  Wirewindow.js
 *
 Purpose: To provide a movable and lighter tab.
 Description:
 Reference to WireIt JS Wiring Liberary, version 0.5.0.
 http://javascript.neyric.com/wireit/
 History:
 2010/9/27 , created by TonyQ
 Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function() {
    /**
     * Helper with Point
     */
    var Point = {
        /**
         * To get joint left-top relatived to the component's left,top,width , height.
         *
         * TODO make sure draggable works
         * @param {Object} left
         * @param {Object} top
         * @param {Object} width
         * @param {Object} height
         * @param {Object} joint
         */
        getOffset: function(left, top, width, height, point, margin) {
            margin = margin || [0, ];
            if (point.indexOf("e") != -1) { //right
                Xcenter = left + width + margin[0];
            } else if (point.indexOf("w") != -1) { //left
                Xcenter = left;
            } else { //center
                Xcenter = left + width / 2;
            }
            if (point.indexOf("s") != -1) { //down
                Ycenter = top + height + margin[1];
            } else if (point.indexOf("n") != -1) { //top
                Ycenter = top;
            } else { //center
                Ycenter = top + height / 2;
            }

            return [Xcenter, Ycenter];
        },
        create: function(id, clz) {
            return "<div id='" + id + "' class='" + clz + "'></div>";
        }
    };

    zkex.wire.Wirebox = zk.$extends(zk.Widget, {
        $define: {
            pointstate: {},
            points: function(p,fireupdate) {
                if(this.$n()){
                    this._drawPoints(p);
                    //TODO fire event to notify server here
                }
                return p || "";
            }
        },
        _updatePoints: function() {
            var jqthis = jq(this.$n()), offset = jqthis.offset(),
                width = jqthis.width(), height = jqthis.height(),
                    margin = [8, 7];
            for (var point in this._pointstate) {
                var termoffset = Point.getOffset(0, 0, width, height, point,
                    margin); //0,0 because it's relative to parent.
                jq(this.$n("term-" + point)).css({
                    "left": jq.px(termoffset[0]),
                    "top": jq.px(termoffset[1])
                });
            }

        },
        _drawPoints:function(p){
            var terms = (p || "").split(",");
            for (var i = 0, len = terms.length; i < len; ++i) {
                if (zkex.wire.Wirebox.POINTS[terms[i]]) {
                    this.addPoint(terms[i],true);
                } else if (terms[i] == zkex.wire.Wirebox.POINT_ALL) {
                    for (var point in zkex.wire.Wirebox.POINTS) {
                        this.addPoint(point,true);
                    }
                } else {
                    zk.error("wirebox didnt support point[" + terms[i] + "]");
                }
            }
            this._updatePoints();
        },
        _checkPoint: function(point) {
            return zkex.wire.Wirebox.POINTS[point] && !this._pointstate[point];
        },
        _removePoint:function(point){
            if (this._pointstate[point]) {
                jq(this.$n("term-" + point)).remove();
                delete this._pointstate[point] ;
            }
        },
        getPointPosition_:function(point){
            var jqoffset = jq(this.$n("term-" + point)).offset();
            return  [jqoffset.left+5,jqoffset.top+5];
        },
        addPoint: function(point,ignoreUpdatePosition) {
            var term,success = false;
            if (point == zkex.wire.Wirebox.POINT_ALL) {
                for (var point in zkex.wire.Wirebox.POINTS) {
                    this.addPoint(point,true);
                }
                success = true;
            }
            if (this._checkPoint(point)) {
                term = Point.create(this.uuid + "-term-" + point, this.getZclass() + "-term");
                jq(this.$n()).append(term);
                this._pointstate[point] = zkex.wire.Wirebox.STATE_WAIT;
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

            if(this._points) this._drawPoints(this._points);
        },
        getZclass: function() {
            return this._zclass || "z-wirebox";
        },
        unbind_: function() {
            this.$supers(zkex.wire.Wirebox, 'unbind_', arguments);
        }
    }, {
        POINT_ALL: "*",
        POINTS: {
            "nw": 1,
            "n": 2,
            "ne": 3,
            "w": 4,
            "e": 6,
            "sw": 7,
            "s": 8,
            "se": 9
        },
        STATE_WAIT: 1,
        STATE_CONNECTED: 2
    })
})();
