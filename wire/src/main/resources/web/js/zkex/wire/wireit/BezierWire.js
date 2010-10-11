/*
 * BezierWire.js
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
 * http://github.com/neyric/wireit/blob/v0.6.0a/js/BezierWire.js
 * commit  c9b903abad6bb62140ae
 *
 * The bezier drawmethod .
 * Every draw method should implements
 * draw: function(drawer,p1,p2,opt) {}
 */

zkex.wire.Drawmethod.bezier = {
	/**
    * Redraw the Wire
    */
   draw: function(drawer,p1,p2,opt) { //potix tonyq

      // Get the positions of the terminals
      //var p1 = this.terminal1.getXY();//potix tonyq
      //var p2 = this.terminal2.getXY();//potix tonyq

      // Coefficient multiplicateur de direction
      // 100 par defaut, si distance(p1,p2) < 100, on passe en distance/2
      var coeffMulDirection = opt.coeffMulDirection;//potix tonyq


      var distance=Math.sqrt(Math.pow(p1[0]-p2[0],2)+Math.pow(p1[1]-p2[1],2));
      if(distance < coeffMulDirection){
         coeffMulDirection = distance/2;
      }

      // Calcul des vecteurs directeurs d1 et d2 :
      var d1 = [opt.directionIn[0]*coeffMulDirection, //potix tonyq
                opt.directionIn[1]*coeffMulDirection];//potix tonyq
      var d2 = [opt.directionOut[0]*coeffMulDirection,//potix tonyq
                opt.directionOut[1]*coeffMulDirection];//potix tonyq

      var bezierPoints=[];
      bezierPoints[0] = p1;
      bezierPoints[1] = [p1[0]+d1[0],p1[1]+d1[1]];
      bezierPoints[2] = [p2[0]+d2[0],p2[1]+d2[1]];
      bezierPoints[3] = p2;
      var min = [p1[0],p1[1]];
      var max = [p1[0],p1[1]];
      for(var i=1 ; i<bezierPoints.length ; i++){
         var p = bezierPoints[i];
         if(p[0] < min[0]){
            min[0] = p[0];
         }
         if(p[1] < min[1]){
            min[1] = p[1];
         }
         if(p[0] > max[0]){
            max[0] = p[0];
         }
         if(p[1] > max[1]){
            max[1] = p[1];
         }
      }
      // Redimensionnement du canvas
      var margin = [4,4];
      min[0] = min[0]-margin[0];
      min[1] = min[1]-margin[1];
      max[0] = max[0]+margin[0];
      max[1] = max[1]+margin[1];
      var lw = Math.abs(max[0]-min[0]);
      var lh = Math.abs(max[1]-min[1]);

   	// Store the min, max positions to display the label later
		//this.min = min; //potix tonyq
		//this.max = max; //potix tonyq

      drawer.SetCanvasRegion(min[0],min[1],lw,lh);

      var ctxt = drawer.getContext();
      for(i = 0 ; i<bezierPoints.length ; i++){
         bezierPoints[i][0] = bezierPoints[i][0]-min[0];
         bezierPoints[i][1] = bezierPoints[i][1]-min[1];
      }

      // Draw the border
      ctxt.lineCap = opt.bordercap; //potix tonyq
      ctxt.strokeStyle = opt.bordercolor; //potix tonyq
      ctxt.lineWidth = opt.width+opt.borderwidth*2; //potix tonyq
      ctxt.beginPath();
      ctxt.moveTo(bezierPoints[0][0],bezierPoints[0][1]);
      ctxt.bezierCurveTo(bezierPoints[1][0],bezierPoints[1][1],bezierPoints[2][0],bezierPoints[2][1],bezierPoints[3][0],bezierPoints[3][1]);
      ctxt.stroke();

      // Draw the inner bezier curve
      ctxt.lineCap = opt.cap; //potix tonyq
      ctxt.strokeStyle = opt.color; //potix tonyq
      ctxt.lineWidth = opt.width; //potix tonyq
      ctxt.beginPath();
      ctxt.moveTo(bezierPoints[0][0],bezierPoints[0][1]);
      ctxt.bezierCurveTo(bezierPoints[1][0],bezierPoints[1][1],bezierPoints[2][0],bezierPoints[2][1],bezierPoints[3][0],bezierPoints[3][1]);
      ctxt.stroke();
   }



};