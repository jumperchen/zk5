/*
 * BezierArrowWire.js
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
 * http://github.com/neyric/wireit/blob/v0.6.0a/js/BezierArrowWire.js
 * commit  c9b903abad6bb62140ae
 *
 * The bezierArrow drawmethod .
 * Every draw method should implements
 * draw: function(drawer,p1,p2,opt) {}
 */

zkex.wire.Drawmethod.bezierArrow = { //potix tonyq
   draw: function(drawer,p1,p2,opt) { //potix tonyq

		var arrowWidth = Math.round(opt.width * 1.5 + 20); //potix tonyq
		var arrowLength = Math.round(opt.width * 1.2 + 20); //potix tonyq
		var d = arrowWidth/2; // arrow width/2
      var redim = d+3; //we have to make the canvas a little bigger because of arrows
      var margin=[4+redim,4+redim];

      // Get the positions of the terminals
    //  var p1 = this.terminal1.getXY(); //potix tonyq
    //  var p2 = this.terminal2.getXY();//potix tonyq

      // Coefficient multiplicateur de direction
      // 100 par defaut, si distance(p1,p2) < 100, on passe en distance/2
      var coeffMulDirection = opt.coeffMulDirection; ////potix tonyq


      var distance=Math.sqrt(Math.pow(p1[0]-p2[0],2)+Math.pow(p1[1]-p2[1],2));
      if(distance < coeffMulDirection){
         coeffMulDirection = distance/2;
      }

      // Calcul des vecteurs directeurs d1 et d2 :
      var d1 = [opt.directionIn[0]*coeffMulDirection,
                opt.directionIn[1]*coeffMulDirection];
      var d2 = [opt.directionOut[0]*coeffMulDirection,
                opt.directionOut[1]*coeffMulDirection];

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
      //var margin = [4,4];
      min[0] = min[0]-margin[0];
      min[1] = min[1]-margin[1];
      max[0] = max[0]+margin[0];
      max[1] = max[1]+margin[1];
      var lw = Math.abs(max[0]-min[0]);
      var lh = Math.abs(max[1]-min[1]);

		// Store the min, max positions to display the label later
		//this.min = min;//potix tonyq
		//this.max = max;//potix tonyq

      drawer.SetCanvasRegion(min[0],min[1],lw,lh);//potix tonyq

      var ctxt = drawer.getContext();//potix tonyq
      for(i = 0 ; i<bezierPoints.length ; i++){
         bezierPoints[i][0] = bezierPoints[i][0]-min[0];
         bezierPoints[i][1] = bezierPoints[i][1]-min[1];
      }

      // Draw the border
      ctxt.lineCap = opt.bordercap;
      ctxt.strokeStyle = opt.bordercolor;
      ctxt.lineWidth = opt.width+opt.borderwidth*2;
      ctxt.beginPath();
      ctxt.moveTo(bezierPoints[0][0],bezierPoints[0][1]);
      ctxt.bezierCurveTo(bezierPoints[1][0],bezierPoints[1][1],bezierPoints[2][0],bezierPoints[2][1],bezierPoints[3][0],bezierPoints[3][1]+arrowLength/2*opt.directionOut[1]);
      ctxt.stroke();

      // Draw the inner bezier curve
      ctxt.lineCap = opt.cap;
      ctxt.strokeStyle = opt.color;
      ctxt.lineWidth = opt.width;
      ctxt.beginPath();
      ctxt.moveTo(bezierPoints[0][0],bezierPoints[0][1]);
      ctxt.bezierCurveTo(bezierPoints[1][0],bezierPoints[1][1],bezierPoints[2][0],bezierPoints[2][1],bezierPoints[3][0],bezierPoints[3][1]+arrowLength/2* opt.directionOut[1]);
      ctxt.stroke();

		//Variables from drawArrows
		//var t1 = p1;
		var t1 = bezierPoints[2],t2 = p2;

		var z = [0,0]; //point on the wire with constant distance (dlug) from terminal2
		var dlug = arrowLength; //arrow length
		var t = 1-(dlug/distance);
		z[0] = Math.abs( t1[0] +  t*(t2[0]-t1[0]) );
		z[1] = Math.abs( t1[1] + t*(t2[1]-t1[1]) );

		// line which connects the terminals: y=ax+b
		var a,b;
		var W = t1[0] - t2[0];
		var Wa = t1[1] - t2[1];
		var Wb = t1[0]*t2[1] - t1[1]*t2[0];
		if (W !== 0) {
			a = Wa/W;
			b = Wb/W;
		}
		else {
			a = 0;
		}
		//line perpendicular to the main line: y = aProst*x + b
		var aProst, bProst;
		if (a === 0) {
			aProst = 0;
		}
		else {
			aProst = -1/a;
		}
		bProst = z[1] - aProst*z[0]; //point z lays on this line

		//we have to calculate coordinates of 2 points, which lay on perpendicular line and have the same distance (d) from point z
		var A = 1 + Math.pow(aProst,2),
			 B = 2*aProst*bProst - 2*z[0] - 2*z[1]*aProst,
			 C = -2*z[1]*bProst + Math.pow(z[0],2) + Math.pow(z[1],2) - Math.pow(d,2) + Math.pow(bProst,2),
			 delta = Math.pow(B,2) - 4*A*C;

		if (delta < 0) { return false; }

		var x1 = (-B + Math.sqrt(delta)) / (2*A),
			x2 = (-B - Math.sqrt(delta)) / (2*A),
			y1 = aProst*x1 + bProst,
			y2 = aProst*x2 + bProst;

		if(t1[1] == t2[1]) {
			var o = (t1[0] > t2[0]) ? 1 : -1;
			x1 = t2[0]+o*dlug;
			x2 = x1;
			y1 -= d;
			y2 += d;
		}

		// triangle fill
		ctxt.fillStyle = opt.color;
		ctxt.beginPath();
		ctxt.moveTo(t2[0],t2[1]);
		ctxt.lineTo(x1,y1);
		ctxt.lineTo(x2,y2);
		ctxt.fill();

		// triangle border
		ctxt.strokeStyle = opt.bordercolor;
		ctxt.lineWidth = opt.borderwidth;
		ctxt.beginPath();
		ctxt.moveTo(t2[0],t2[1]);
		ctxt.lineTo(x1,y1);
		ctxt.lineTo(x2,y2);
		ctxt.lineTo(t2[0],t2[1]);
		ctxt.stroke();

		return [p1,p2,t1,t2];
   }

};