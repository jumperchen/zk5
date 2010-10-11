/*
 * Point.js
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
      zkex.wire.Point = zk.$extends(zk.Object, {
        _id:'',
        _zclass:'',
        _point:'',
        parent:null,
        $init:function(parent,id,clz,point){
            this._id = id;
            this._zclass = clz;
            this._point = point;
            this.parent = parent;

            this._element = this.createPoint(this._id,this._zclass);
            this._element.addClass(zkex.wire.Point.WIRECLASS);
            this._element[0].obj = this;
            jq(parent).append(this._element);
            this._drag = new zk.Draggable(this, this._element, {
                handle: this._element,
                fireOnMove: false,
                ghosting: false,
                starteffect:zkex.wire.Point._dragstart,
                draw:zkex.wire.Point._dragdraw,
                endghosting: zk.$void,
                endeffect: zkex.wire.Point._dragend,
                zIndex: 99999
            });
        },
        updatePosition:function(ofs){
            var box = jq(this.parent), offset = ofs || box.offset(),
                width = box.width(), height = box.height(),
                    margin = [8, 7];
            var termoffset = zkex.wire.Point.getOffset(0, 0, width, height,
                this._point, margin); //0,0 because it's relative to parent.

            this._element.css({
                "left": jq.px(termoffset[0]),
                "top": jq.px(termoffset[1])
            });
        },
        destroy:function(){
           jq(this._element).remove();
        },
        offset:function(){
            var ofs = jq(this._element).offset();
            return [ofs.left,ofs.top];
        },
        createPoint:function(uuid,clz){
            return jq("<div id='" + uuid + "' class='" + clz + "'></div>");
        },
        getCenterOffset:function(){
            var ofs = this.offset();
            return [ofs[0]+this._element.width()/2,ofs[1]+this._element.height()/2]
        }
    },{
        _dragstart:function(dg){
           dg.drawer = new zkex.wire.Drawer(null,document.body,dg.node._uuid+"-fake-drawer");
           dg.startpoc = jq(dg.node).offset();
           dg.startpoc = [dg.startpoc.left,dg.startpoc.top];
        },
        _dragdraw:function(dg, ofs, evt){
            var drawmethod = zkex.wire.Drawmethod["bezier"];
            if (drawmethod) {
                drawmethod.draw(dg.drawer, [dg.startpoc[0],dg.startpoc[1]] , [evt.pageX,evt.pageY], zkex.wire.Wire.opt);
            }else{
                zk.error("draw method not found :[bezier]");
            }
        },
        _dragend:function(dg,evt){
            var target  = jq(evt.domTarget);
            if(target.is("."+zkex.wire.Point.WIRECLASS)){
                zk.Widget.$(target.parent()).fire("onWire", {
                    inbox: dg.node.parentNode.id,
                    outbox: target.parent()[0].id,
                    joint:dg.node.obj._point+","+target[0].obj._point
                });
                //fire onWire event
            }
            dg.drawer.destroy();
            dg.drawer=null;
        },
        allowPoint:function(point){
            return zkex.wire.Point.POINTS[point];
        },
         /**
         * To get joint left-top relatived to the component's left,top,width , height.
         *
         * @param {Object} left
         * @param {Object} top
         * @param {Object} width
         * @param {Object} height
         * @param {Object} joint
         */
        getOffset: function(left, top, width, height, point, margin) {
            margin = margin || [0, 0];
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
        WIRECLASS:"wireable",
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
    });

})();
