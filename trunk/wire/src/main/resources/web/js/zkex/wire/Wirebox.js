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
        _wires:[],
        _wirecount:0,
        $define: {
            pointstate: {},
            drawmethod:function(str){
                this._drawmethod = str || "bezierArrow";
            }
        },
        _updatePoints: function() {
            for (var point in this._pointstate) {
                this._pointstate[point].updatePosition();
            }
        },
        setPoints:function(p){
            if (this.$n()) {
                if ( p ==zkex.wire.Wirebox.POINT_ALL ) {
                    this.addPoint(p);
                    this._updatePoints();
                    this._points = zkex.wire.Wirebox.POINT_ALL;
                    return;
                }
                    
                var terms = (p || "").split(",");
                for (var i = 0, len = terms.length; i < len; ++i) {
                    if (zkex.wire.Point.allowPoint(terms[i])) {
                        this.addPoint(terms[i]);
                    } else {
                        zk.error("wirebox didnt support point[" + terms[i] + "]");
                    }
                }
                this._updatePoints();
            }
            this._points = p || "";
       },
       /**
        * check if point is created
        * @param {Object} point
        * @return boolean point is created or not , return true when not created.
        */
        _isBlankPoint: function(point) {
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
        /**
         * called by wire , to sync wire information and create point.
         * @param {Object} wire
         * @param {Object} point
         */
        addWire_:function(wire,point){
            if (this._isBlankPoint(point)) {
                var pointel = new zkex.wire.Point(this,this.uuid ,
                    this.getZclass(),point,false); //make editable false.
                this._pointstate[point] = pointel;
            }
            this._pointstate[point].addWireCount();
            this._wires.push(wire);
        },
        /**
         * called by wire , to sync wire information and remvoe point .
         */
        removeWire_:function(wire,point){
            //we assumed here should exist the point
            var pointel = this._pointstate[point];
            pointel.decreaseWireCount();

            //non-editable means add by wire, and wires count is 0 means all
            //wire is removed , so we remove this wire .
            if (pointel.getWireCount()==0 && !pointel.getEditable()) {
                pointel.destroy();
                this._pointstate[point] = null;
            }
            this._wires.$remove(wire);
        },
        addPoint: function(point) {
            var term,scissors,success = false;
            if (point == zkex.wire.Wirebox.POINT_ALL) {
                for (var i = 0 ,len= zkex.wire.Wirebox.POINTS.length; i < len ; ++i) {
                    success = this.addPoint(zkex.wire.Wirebox.POINTS[i]) || success;
                }
                return success;
            }
            if (this._isBlankPoint(point)) {
                var pointel = new zkex.wire.Point(this,this.uuid ,
                    this.getZclass(),point,true);
                this._pointstate[point] = pointel;
                success = true;
            }else{

                if(!this._pointstate[point].getEditable()){
                    this._pointstate[point].setEditable(true);
                }

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
        }
    }, {
        POINT_ALL: "*",
        POINTS: ["nw","n","ne","w","e","sw","s","se"],
        STATE_WAIT: 1,
        STATE_CONNECTED: 2
    })
})();
