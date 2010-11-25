/* TestCaseController.java

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Aug 27, 2010 3:55:26 PM , Created by simon
}}IS_NOTE

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package demo.canvas4z;

import org.zkoss.canvas.*;
import org.zkoss.canvas.Drawable.*;
import org.zkoss.canvas.util.Shapes;
import org.zkoss.zul.*;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * @author simon
 *
 */
@SuppressWarnings("serial")
public class TestCaseController extends GenericForwardComposer {
	
	private Grid grid;
	private Image img;
	
	/*
	private Drawable[] drawables = { 
		new Rectangle(25, 25, 50, 50), 
		new Path().moveTo(50, 25).lineTo(75, 75).lineTo(25, 75).closePath(),
		Shapes.heart(100),
		new Text("ZK", 25, 65)
		//new ImageSnapshot(img, 0, 0)
	};
	*/
	
	private DrawingStyleApplier[] _appliers = {
		new DrawingStyleApplier(){
			public String getName() {
				return "No Style";
			}
			public void doStyles(Drawable d) {
				// do nothing
			}
		},
		new DrawingStyleApplier(){
			public String getName() {
				return "Color, Alpha";
			}
			public void doStyles(Drawable d) {
				d.removeAllStyles()
					.setDrawingType(DrawingType.BOTH)
					.setStrokeStyle("#00FFFF")
					.setFillStyle("#FF00FF")
					.setAlpha(0.3);
			}
		},
		// TODO gradient, pattern
		new DrawingStyleApplier() {
			public String getName() {
				return "Line Style";
			}
			public void doStyles(Drawable d) {
				d.removeAllStyles()
					.setDrawingType(DrawingType.STROKE)
					.setLineWidth(5)
					.setLineJoin(LineJoin.ROUND);
			}
		},
		new DrawingStyleApplier() {
			public String getName() {
				return "Shadow";
			}
			public void doStyles(Drawable d) {
				d.removeAllStyles()
					.setDrawingType(DrawingType.FILL)
					.setShadowColor("#FFFF00")
					.setShadowOffset(20, 20)
					.setShadowBlur(10);
			}
		},
		new DrawingStyleApplier() {
			public String getName() {
				return "Transformation";
			}
			public void doStyles(Drawable d) {
				double cos60 = Math.cos(Math.PI/3);
				double sin60 = Math.sin(Math.PI/3);
				d.removeAllStyles()
					.setDrawingType(DrawingType.BOTH)
					.setTransformation(cos60, sin60, -sin60, cos60, 60, 0);
			}
		},
		new DrawingStyleApplier() {
			public String getName() {
				return "Clipping";
			}
			public void doStyles(Drawable d) {
				d.removeAllStyles()
					.setDrawingType(DrawingType.BOTH)
					.setClipping(new Path().moveTo(0, 0).lineTo(100, 0)
							.lineTo(0, 100).closePath());
			}
		}
	};
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		// columns
		for(DrawingStyleApplier app : _appliers)
			grid.getColumns().appendChild(createUnitColumn(app.getName()));
		
		Drawable[] drawables = { 
				new Rectangle(25, 25, 50, 50), 
				new Path().moveTo(50, 25).lineTo(75, 75).lineTo(25, 75).closePath(),
				Shapes.heart(100),
				new Text("ZK", 25, 65),
				new ImageSnapshot(img, 0, 0)
		};
		
		// rows
		for(Drawable d : drawables){
			Row r = new Row();
			
			for(DrawingStyleApplier app : _appliers)
				r.appendChild(createUnitCanvas(app.applyStyle((Drawable)d.clone())));
			
			grid.getRows().appendChild(r);
		}
	}
	
	private abstract class DrawingStyleApplier {
		public abstract void doStyles(Drawable d);
		public abstract String getName();
		
		public Drawable applyStyle(Drawable drawable){
			doStyles(drawable);
			if(drawable instanceof Text)
				((Text) drawable).setFont("40px serif");
			return drawable;
		}
	}
	
	private Column createUnitColumn(String text){
		Column result = new Column(text);
		result.setWidth("106px");
		return result;
	}
	
	private Canvas createUnitCanvas(Drawable drawable){
		Canvas result = new Canvas();
		result.setWidth("100px");
		result.setHeight("100px");
		if(drawable != null) result.add(drawable);
		return result;
	}
	
}
