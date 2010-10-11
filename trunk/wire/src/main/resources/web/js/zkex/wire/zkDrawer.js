//function getXY(terminal, parentNode, joint) {
//}

//function setDirection(isTargetNode, joint) {
//}

/**
 * Here we go throught wireit's interface .
 * @param {Object} minx
 * @param {Object} miny
 * @param {Object} width
 * @param {Object} height
 */

zkex.wire.Drawer=function(){



};
zkex.wire.Drawer.prototype={
    opt:{
        // the default option value from
        // http://github.com/neyric/wireit/blob/v0.6.0a/js/Wire.js
        className: "WireIt-Wire",
        cap: 'round',
        bordercap: 'round',
        width: 3,
        borderwidth: 1,
        color: 'rgb(173, 216, 230)',
        bordercolor: '#0000ff',
    },
    //potix tonyq from http://github.com/neyric/wireit/blob/v0.6.0a/js/CanvasElement.js
    SetCanvasRegion: UA.ie ? // IE
               function(left,top,width,height){
                  var el = this.element;
                  WireIt.sn(el,null,{left:left+"px",top:top+"px",width:width+"px",height:height+"px"});
                  el.getContext("2d").clearRect(0,0,width,height);
                  this.element = el;
               } :
               ( (UA.webkit || UA.opera) ?
                  // Webkit (Safari & Chrome) and Opera
                  function(left,top,width,height){
                     var el = this.element;
                     var newCanvas=WireIt.cn("canvas",{className:el.className || el.getAttribute("class"),width:width,height:height},{left:left+"px",top:top+"px"});
                     var listeners=Event.getListeners(el);
                     for(var listener in listeners){
								if(listeners.hasOwnProperty(listener)) {
									var l=listeners[listener];
									Event.addListener(newCanvas,l.type,l.fn,l.obj,l.adjust);
								}
                     }
                     Event.purgeElement(el);
                     el.parentNode.replaceChild(newCanvas,el);
                     this.element = newCanvas;
                  } :
                  // Other (Firefox)
                  function(left,top,width,height){
                     WireIt.sn(this.element,{width:width,height:height},{left:left+"px",top:top+"px"});
                  }),
    //potix tonyq  from http://github.com/neyric/wireit/blob/v0.6.0a/js/CanvasElement.js
    getContext: function(mode) {
       return this.element.getContext(mode || "2d");
    }
};
