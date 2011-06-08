/* CompositeDrawable.java

{{IS_NOTE
 Purpose:
  
 Description:
  
 History:
  Jun 8, 2011 11:56:44 PM , Created by simonpai
}}IS_NOTE

Copyright (C) 2011 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
package org.zkoss.canvas;

import java.util.Arrays;
import java.util.List;

import org.zkoss.json.JSONArray;
import org.zkoss.json.JSONAware;

/**
 *
 * @author simonpai
 */
public class CompositeDrawable extends Drawable {
	
	protected List<Drawable> _drawables;
	
	/**
	 * 
	 * @param drawables
	 */
	public CompositeDrawable(Drawable ... drawables) {
		_drawables = Arrays.asList(drawables);
	}
	
	/**
	 * 
	 * @param drawables
	 */
	public CompositeDrawable(List<Drawable> drawables) {
		_drawables = drawables;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONAware getShapeJSONObject() {
		JSONArray result = new JSONArray();
		for(Drawable d : _drawables)
			result.add(d);
		return result;
	}
	
	@Override
	public String getType() {
		return "comp";
	}

}
