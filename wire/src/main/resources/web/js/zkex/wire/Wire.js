/*  Wire.js
 *
 Purpose: To provide a movable and lighter tab.
 Description:
 Reference to WireIt JS Wiring Liberary, version 0.5.0.
 http://javascript.neyric.com/wireit/
 History:
 Dec 11, 2009 12:11:50 PM, Created by joy
 2010/9/27 , Updated by TonyQ
 Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
(function() {

    zkex.wire.Wire = zk.$extends(zul.Widget, {
        _in: "",
        _out: "",
        $define: {
            config: function(conf) {
                this._config = zk.copy(zkex.wire.Wire.parseConfig(conf), zkex.wire.Wire.opt);
            },
            joint: function(joint) {
                this._joint = joint.split(/,/g);
            }
        },
        setInId:function(str){
            this.setIn(str);
        },
        setOutId:function(str){
            this.setOut(str);
        },
        setIn: function(val) {
            this._in = val;
        },
        getIn: function() {
            return zk.Widget.$(this._in);
        },
        setOut: function(val) {
            this._out = val;
        },
        getOut: function() {
            return zk.Widget.$(this._out);
        },
        $init:function(){
            this.$supers(zkex.wire.Wire, '$init', arguments);
            this._config = zk.copy({},zkex.wire.Wire.opt);
        },
        bind_: function(desktop, skipper, after) {
            this.$supers(zkex.wire.Wire, 'bind_', arguments);

            var inbox = this.getIn(), outbox = this.getOut();
            inbox.addPoint(this._joint[0]);
            outbox.addPoint(this._joint[1]);
            //addPoint
            this.drawer = new zkex.wire.Drawer(document.body,this._config["className"]);

            var drawmethod = zkex.wire.Drawmethod[this._config["drawingMethod"]];
            if (drawmethod) {
                drawmethod.draw(this.drawer, inbox.getPointPosition_(this._joint[0]), outbox.getPointPosition_(this._joint[1]), this.getConfig());
            }else{
                zk.error("draw method not found :["+this._config["drawingMethod"]+"]");
            }

        },

        unbind_: function() {
            this.$supers(zkex.wire.Wire, 'unbind_', arguments);
        }

    }, {
        opt:{
            // the default option value from
            // http://github.com/neyric/wireit/blob/v0.6.0a/js/Wire.js
            className: "zk-wire-canvas",
            cap: 'round',
            bordercap: 'round',
            width: 3,
            borderwidth: 1,
            color: 'rgb(173, 216, 230)',
            bordercolor: '#0000ff',
            drawingMethod:'straight',
            // only beziers use these option below
            coeffMulDirection: 100,
            directionIn:[0,1],
            directionOut:[0,1]
        },
        parseConfig: function(conf) {
            tokens = (conf || "").split(/,/), token, result = {};

            for (var i = 0, len = tokens.length; i < len; ++i) {
                token = tokens[i].split("=");
                result[token[0]] = token[1];
            }
            return result;
        }
    });
})();
