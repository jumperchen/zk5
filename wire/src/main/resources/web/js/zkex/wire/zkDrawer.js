/*
 * zkDrawer.js
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */

/**
 * Here we go throught wireit's interface .
 */
zkex.wire.Drawer = zk.$extends(zk.Object, {
    _id:null,
    _element:null,
    _parent:null,
    _className:null,
    $init:function(id,parent,className){
        this.$supers(zkex.wire.Drawer, '$init', arguments);
        this._id = id;
        this._element = null; // for canvas
        this._parent = parent ; //dom parent for create
        this._className = className;
    },
    _createCanvas:function(left,top,width,height){
        var canvas = zk.canvas.Canvas.create(width, height);
        if(this._id) canvas.id = this._id;
        if(this._className) canvas.setAttribute('class', this._className);
        jq(canvas).css({
            left: jq.px(left) ,
            top: jq.px(top)
        });
        if(this._parent) this._parent.appendChild(canvas);
        return canvas;//convas.getContext("2d")
    },
    _getCanvas:function(left,top,width,height){
         this._element = this._element || this._createCanvas(left,top,width,height);
         return this._element;
    },
    //potix tonyq from http://github.com/neyric/wireit/blob/v0.6.0a/js/CanvasElement.js
    SetCanvasRegion: jq.browser.msie ? // IE
       function(left,top,width,height){
          var canvas = this._getCanvas(left,top,width,height);
          jq(canvas).css({left:left+"px",top:top+"px",width:width+"px",height:height+"px"});
          canvas.getContext("2d").clearRect(0,0,width,height);
       } :
       ( (jq.browser.webkit || jq.browser.opera) ?
          // Webkit (Safari & Chrome) and Opera
          function(left,top,width,height){
             var el = this._getCanvas(left,top,width,height);
             var newCanvas= this._createCanvas(left,top,width,height);

             this._parent.replaceChild(newCanvas,el);
             this._element = newCanvas;
          } :
          // Other (Firefox)
          function(left,top,width,height){
             var canvas = this._getCanvas(left,top,width,height);
             jq(canvas).attr({width:width,height:height}).css({left:left+"px",top:top+"px"});
          }),
    //potix tonyq  from http://github.com/neyric/wireit/blob/v0.6.0a/js/CanvasElement.js
    getContext: function(mode) {
       return this._element ? this._element.getContext(mode || "2d") : null;
    },
    destroy:function(){
        jq(this._element).remove();
    }
});


/**
 * here we defined the draw method set,
 * every drawmethod should implements draw function.
 *
 * abstract void draw(drawer,p1,p2,opt)
 */
zkex.wire.Drawmethod = {};
