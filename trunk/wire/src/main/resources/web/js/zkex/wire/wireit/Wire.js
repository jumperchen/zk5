/*
 * Wire.js
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
/*global YAHOO */
/**
 *
 * modified from http://github.com/neyric/wireit/blob/v0.6.0a/js/RightSquareArrowWire.js
 * commit  c9b903abad6bb62140ae
 *
 * The stright drawmethod .
 * Every draw method should implements
 * draw: function(drawer,p1,p2,opt) {}
 */

zkex.wire.Drawmethod.straight={
   /**
    * Drawing method
    */
   draw: function(drawer,p1,p2,opt) {    //potix tonyq
      var margin = [4,4];

      // Get the positions of the terminals
      //var p1 = this.terminal1.getXY();
      //var p2 = this.terminal2.getXY();

      var min=[ Math.min(p1[0],p2[0])-margin[0], Math.min(p1[1],p2[1])-margin[1]];
      var max=[ Math.max(p1[0],p2[0])+margin[0], Math.max(p1[1],p2[1])+margin[1]];

		// Store the min, max positions to display the label later
		//this.min = min;//potix tonyq
		//this.max = max;//potix tonyq

      // Redimensionnement du canvas
      var lw=Math.abs(max[0]-min[0]);
      var lh=Math.abs(max[1]-min[1]);

      // Convert points in canvas coordinates
      p1[0] = p1[0]-min[0];
      p1[1] = p1[1]-min[1];
      p2[0] = p2[0]-min[0];
      p2[1] = p2[1]-min[1];

      drawer.SetCanvasRegion(min[0],min[1],lw,lh);//potix tonyq

      var ctxt=drawer.getContext(); //potix tonyq

      // Draw the border
      ctxt.lineCap=opt.bordercap;//potix tonyq
      ctxt.strokeStyle=opt.bordercolor;//potix tonyq
      ctxt.lineWidth=opt.width+opt.borderwidth*2;//potix tonyq
      ctxt.beginPath();
      ctxt.moveTo(p1[0],p1[1]);
      ctxt.lineTo(p2[0],p2[1]);
      ctxt.stroke();

      // Draw the inner bezier curve
      ctxt.lineCap=opt.cap;//potix tonyq
      ctxt.strokeStyle=opt.color;//potix tonyq
      ctxt.lineWidth=opt.width;//potix tonyq
      ctxt.beginPath();
      ctxt.moveTo(p1[0],p1[1]);
      ctxt.lineTo(p2[0],p2[1]);
      ctxt.stroke();
   }

};
