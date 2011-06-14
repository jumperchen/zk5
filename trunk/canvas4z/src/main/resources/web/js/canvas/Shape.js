/* Shape.js

	Purpose:
		
	Description:
		
	History:
		May 18, 2010 3:24:34 PM , Created by simon

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

/**
 * 
 */
canvas.Shape = zk.$extends(canvas.Drawable, {
	
	$init: function(){
		this.$super('$init');
	}
	
});

/**
 * 
 */
canvas.Rectangle = zk.$extends(canvas.Shape, {
	
	$init: function (x, y, w, h) {
		this.$super('$init');
		this.objtp = "rect";
		this.obj = new zk.Object();
		this.obj.x = x;
		this.obj.y = y;
		this.obj.w = w;
		this.obj.h = h;
	},
	/**
	 * 
	 */
	setPos: function (x, y) {
		this.obj.x = x;
		this.obj.y = y;
		return this;
	},
	/**
	 * 
	 */
	setSize: function (w, h) {
		this.obj.w = w;
		this.obj.h = h;
		return this;
	},
	/**
	 * Scales the rectangle. Reference point is (0,0)
	 */
	scale: function (a, b) {
		this.obj.x *= a;
		this.obj.y *= b;
		this.obj.w *= a;
		this.obj.h *= b;
	},
	/**
	 * Translate the rectangle
	 */
	translate: function (dx, dy) {
		this.obj.x += dx;
		this.obj.y += dy;
	},
	/**
	 * Return true if (x, y) is strictly inside the boundary of the rectangle.
	 */
	contains: function (x, y) {
		if(!x || !y) return false; // TODO: check
		var sx = this.obj.x, 
			sy = this.obj.y;
		return sx < x 
			&& x < sx + this.obj.w 
			&& sy < y 
			&& y < sy + this.obj.h;
	}
	
});

/**
 * 
 */
canvas.Path = zk.$extends(canvas.Shape, {
	
	$init: function () {
		this.$super('$init');
		this.objtp = "path";
		this.obj = new zk.Object();
		this.obj.sg = [];
	},
	/**
	 * 
	 */
	beginPath: function () {
		this.obj.sg = [];
		return this;
	},
	/**
	 * 
	 */
	closePath: function () {
		var s = new Object();
		s.tp = "cl";
		s.dt = [];
		this.obj.sg.push(s);
		return this;
	},
	/**
	 * 
	 */
	moveTo: function (x, y) {
		var s = Object();
		s.tp = "mv";
		s.dt = [x,y];
		this.obj.sg.push(s);
		return this;
	},
	/**
	 * 
	 */
	lineTo: function (x, y) {
		var s = Object();
		s.tp = "ln";
		s.dt = [x,y];
		this.obj.sg.push(s);
		return this;
	},
	/**
	 * 
	 */
	quadTo: function (cpx, cpy, x, y) {
		var s = Object();
		s.tp = "qd";
		s.dt = [cpx, cpy, x,y];
		this.obj.sg.push(s);
		return this;
	},
	/**
	 * 
	 */
	bezierTo: function (cpx1, cpy1, cpx2, cpy2, x, y) {
		var s = Object();
		s.tp = "bz";
		s.dt = [cpx1, cpy1, cpx2, cpy2, x, y];
		this.obj.sg.push(s);
		return this;
	},
	// arcTo
	/**
	 * Transforms the path
	 */
	transform: function (m) {
		var sg = this.obj.sg;
		for(var i=sg.length;i--;){
			var dt = sg[i].dt;
			for(var j=(dt.length)/2;j--;){
				var x2 = m[0]*dt[2*j] + m[2]*dt[2*j+1] + m[4];
				dt[2*j+1] = m[1]*dt[2*j] + m[3]*dt[2*j+1] + m[5];
				dt[2*j] = x2;
			}
		}
	},
	/**
	 * Scales the path. Reference point is (0,0).
	 */
	scale: function (a,b) {
		this.transform([a,0,0,b,0,0]);
	},
	/**
	 * Translates the path
	 */
	translate: function (dx, dy) {
		this.transform([1,0,0,1,dx,dy]);
	},
	/**
	 * 
	 */
	clone: function () {
		var p2 = new canvas.Path();
		p2._copyObj(this);
		p2._copyState(this);
	},
	//@Override
	contains: function (x, y) {
		//if(!x || !y) return false;
		// TODO: port Java 2D's algorithm to here
		return false;
	},
	// copy object data from path
	_copyObj: function (path) {
		this.obj.sg = [];
		var sg1 = path.obj.sg;
		for(var i=sg1.length;i--;){
			this.obj.sg[i] = new Object();
			this.obj.sg[i].tp = sg1[i].tp;
			this.obj.sg[i].dt = [];
			for(var j=sg1[i].dt.length;j--;) {
				this.obj.sg[i].dt[j] = sg1[i].dt[j];
			}
		}
	}
	
});
