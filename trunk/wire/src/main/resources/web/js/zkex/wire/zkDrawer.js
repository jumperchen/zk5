//function getXY(terminal, parentNode, joint) {
//}

//function setDirection(isTargetNode, joint) {
//}

/**
 * Here we go throught wireit's interface .
 */

zkex.wire.Drawer=function(parent,className){
    this.element = null; // for canvas
    this.parent = parent ; //dom parent for create
    this.className = className;
};
zkex.wire.Drawer.prototype={
    _createCanvas:function(left,top,width,height){
        var canvas = zk.canvas.Canvas.create(width, height);
        if(this.className) canvas.setAttribute('class', this.className);
        jq(canvas).css({
            left: jq.px(left) ,
            top: jq.px(top)
        });
        if(this.parent) this.parent.appendChild(canvas);
        return canvas;//convas.getContext("2d")
    },
    _getCanvas:function(left,top,width,height){
         this.element = this.element || this._createCanvas(left,top,width,height);
         return this.element;
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

             this.parent.replaceChild(newCanvas,el);
             this.element = newCanvas;
          } :
          // Other (Firefox)
          function(left,top,width,height){
             var canvas = this._getCanvas(left,top,width,height);
             jq(canvas).attr({width:width,height:height}).css({left:left+"px",top:top+"px"});
          }),
    //potix tonyq  from http://github.com/neyric/wireit/blob/v0.6.0a/js/CanvasElement.js
    getContext: function(mode) {
       return this.element ? this.element.getContext(mode || "2d") : null;
    }
};


/**
 * here we defined the draw method set,
 * every drawmethod should implements draw function.
 *
 * abstract void draw(drawer,p1,p2,opt)
 */
zkex.wire.Drawmethod = {};
