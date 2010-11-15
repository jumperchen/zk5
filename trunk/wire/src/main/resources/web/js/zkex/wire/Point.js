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
      var Point = zkex.wire.Point = zk.$extends(zk.Object, {
        _id:'',
        _zclass:'',
        _point:'',
        parent:null,
        _editable:false,
        _wirecount:0,
        $init:function(parent,id,clz,point,editable){
            this._id = id;
            this._zclass = clz;
            this._point = point;
            this.parent = parent;
            this._element = this.createPoint(this._id,this._zclass);

            this._element[0].obj = this;
            jq(parent).append(this._element);
            this._updatePosition(this._element,[0,0]);
            if (editable) this.setEditable(true);            

        },
        addWireCount:function(){
            this._wirecount++;
        },
        decreaseWireCount:function(){
            if(this._wirecount>0) this._wirecount--;
        },
        getWireCount:function(){
            return this._wirecount;
        },
        getEditable:function(){
            return this._editable;
        },
        setEditable:function(editable){
            if (this._editable != !!editable) { //!! for boolean
                if (editable) {
                    this._element.addClass(Point.WIRECLASS);                         
                    this._makeEditable();
                }else{
                    this._element.removeClass(Point.WIRECLASS);                         
                    this._cancelEditable();
                }
                this._editable = !!editable;
            }
        },
        _makeEditable:function(){
        	if(!this._scissors){
        		var that = this;
	            this._scissors = this.createScissors(this._id,this._zclass);
	            this._scissors[0].obj = this;
	            jq(this.parent).append(this._scissors);
	            this._scissors.hide();
	            this._updatePosition(this._scissors,[-20,-20]);
	            jq(this._element).hover(function(){
	            	if(that.clearing) window.clearTimeout(that.clearing);
                    if(that._wirecount > 0 ) that._scissors.show();
	            },function(){
	            	if(that._wirecount > 0 ) that.clearing = setTimeout(
                        function(){ that._scissors.hide(); },1000);
	            })
                jq(this._scissors).click(function(){
                    that.parent.fire("onUnWire", { boxid:that._id,
                        joint:that._point });
                    that._scissors.hide();
                })
        	}

            if(!this._drag){
	            this._drag = new zk.Draggable(this, this._element, {
	                handle: this._element,
	                fireOnMove: false,
	                ghosting: false,
	                starteffect:Point._dragstart,
	                draw:Point._dragdraw,
	                endghosting: zk.$void,
	                endeffect: Point._dragend,
	                zIndex: 99999
	            });
            }
        },
        _cancelEditable:function(){
        	if(this._scissors){
        		jq(this._scissors).hide();
        		jq(this._element).unbind("hover");
        	}else{
        		this._drag.distory();
        		this._drag = null ;
        	}
        },

        updatePosition:function(){
           this._updatePosition(this._element,[0,0]);
        },
        _updatePosition:function(_element,_margin){
            var box = jq(this.parent),
                width = box.width(), height = box.height(),
                    margin = _margin || [8, 7];
            var termoffset = Point.getOffset(0, 0, width, height,
                this._point, margin); //0,0 because it's relative to parent.

            _element.css({
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
            return jq("<div id='" + uuid + "-point-"+this._point+"' class='"
                + clz + "-point'></div>");
        },
        createScissors:function(uuid,clz){
            return jq("<div style='display:none;' id='" + uuid + "-scissors-"
                + this._point + "' class='" + clz + "-scissors'></div>");
        },
        getCenterOffset:function(){
            var ofs = this.offset();
            return [ofs[0]+this._element.width()/2,ofs[1]+
                this._element.height()/2]
        }
    },{
        _dragstart:function(dg){
           dg.drawer = new zkex.wire.Drawer(null,document.body,dg.node._uuid+
               "-fake-drawer");
           dg.startpoc = jq(dg.node).offset();
           dg.startpoc = [dg.startpoc.left,dg.startpoc.top];
        },
        _dragdraw:function(dg, ofs, evt){
            
            var overing = jq(evt.domTarget);
            if(dg.lastover && dg.lastover[0].id != overing[0].id ){
                dg.lastover.removeClass(Point.WIRE_OVER_CLASS);                    
            }            
            if(overing.is("."+ Point.WIRECLASS)){
                dg.lastover = overing.addClass(Point.WIRE_OVER_CLASS);
            }
            var drawmethod = zkex.wire.Drawmethod[dg.node.obj.parent.getDrawmethod()];
            if (drawmethod) {
                drawmethod.draw(dg.drawer, [dg.startpoc[0],dg.startpoc[1]] ,
                    [evt.pageX,evt.pageY], zkex.wire.Wire.opt);
            }else{
                zk.error("draw method not found :["+dg.node.obj.parent.getDrawmethod()+"]");
            }
        },
        _dragend:function(dg,evt){
            dg.lastover.removeClass("z-wire-over");
            var target  = jq(evt.domTarget);
            if(target.is("."+zkex.wire.Point.WIRECLASS)){
                //fire onWire event
                zk.Widget.$(target.parent()).fire("onWire", {
                    inbox: dg.node.parentNode.id,
                    outbox: target.parent()[0].id,
                    joint:dg.node.obj._point+","+target[0].obj._point,
                    drawmethod:dg.node.obj.parent.getDrawmethod()
                });

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
            var padding = 6;
            margin = margin || [0, 0];
            if (point.indexOf("e") != -1) { //right
                Xcenter = left + width + padding + margin[0];
            } else if (point.indexOf("w") != -1) { //left
                Xcenter = left - margin[0];
            } else { //center
                Xcenter = left + width / 2;
            }
            if (point.indexOf("s") != -1) { //down
                Ycenter = top + height + padding + margin[1];
            } else if (point.indexOf("n") != -1) { //top
                Ycenter = top - margin[1];
            } else { //center
                Ycenter = top + height / 2;
            }

            return [Xcenter, Ycenter];
        },
        WIRE_OVER_CLASS:"z-wire-over",
        WIRECLASS:"z-wireable",
        POINTS: {
            "nw": 1,
            "n": 2,
            "ne": 3,
            "w": 4,
            "e": 6,
            "sw": 7,
            "s": 8,
            "se": 9
        }
    });

})();
