/*
 * Wire.java
 *
 * Purpose:
 *
 * Description:
 *
 * History:
 * 	Dec 11, 2009 12:23:13 PM , Created by joy
 * 	2010/9/27, updated by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zkex.wire;

import java.io.IOException;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

public class Wire extends HtmlBasedComponent {

	private static final long serialVersionUID = 20091211122313L;

	private Wirebox _in;

	private Wirebox _out;

	private String _config;

	private String _joint;

	public Wire() {
	}

	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);

		if (_in != null) {
			render(renderer, "in", _in.getUuid());
		}
		if (_out != null) {
			render(renderer, "out", _out.getUuid());
		}
		if (_joint != null) { // if they didnt give joint , we dont show it.
			render(renderer, "joint", _joint);
		}
		if (getParent() == null) {
			if (_out != null && _out.getParent() != null) {
				_out.getParent().appendChild(this);
			}// else here need warning , because wire didnt exist in any
				// container
		}
	}

	/**
	 * No child is allowed.
	 */
	protected boolean isChildable() {
		return false;
	}

	/**
	 * get the wirebox .
	 *
	 * @return
	 */
	public Wirebox getIn() {
		return _in;
	}

	/**
	 * set the wirebox .
	 *
	 * @return
	 */
	public void setIn(Wirebox _in) {
		this._in = _in;
	}

	/**
	 * set the ID of wirebox in
	 *
	 * @param id
	 *            wirebox's id
	 */
	public void setIn(String id) {
		this._in = (Wirebox) this.getFellow(id); // TODO check this works .
	}

	/**
	 * get the wirebox out
	 *
	 * @return
	 */
	public Wirebox getOut() {
		return _out;
	}

	/**
	 * set the wirebox out
	 *
	 * @param _out
	 */
	public void setOut(Wirebox _out) {
		this._out = _out;
	}

	/**
	 * set the id of wirebox out
	 *
	 * @param id
	 *            wirebox's id
	 */
	public void setOut(String id) {
		this._out = (Wirebox) this.getFellow(id);
	}

	/**
	 * we have the configs below (we follow wireit use
	 * "key1=value1,key2=value2,key3=value3" style) <Br />
	 *
	 * <ul>
	 * <li>
	 * color : the line color , ex. #DDDD22</li>
	 * <li>
	 * drawingMethod: this support 4 types from wireit .
	 * <ul>
	 * <li>
	 * straight</li>
	 * <li>
	 * bezier</li>
	 * <li>
	 * arrows</li>
	 * <li>
	 * bezierArrows</li>
	 * </ul>
	 * </li>
	 * <li>
	 * cap : the line cap</li>
	 * <li>
	 * width : the line width (without border)</li>
	 * <li>
	 * borderwidth : the border width</li>
	 * <li>
	 * bordercolor : the border color</li>
	 * <li>
	 * bordercap : the border cap</li>
	 *
	 * </ul>
	 *
	 * @return
	 */
	public String getConfig() {
		return _config;
	}

	public void setConfig(String _config) {
		this._config = _config;
	}

	@Override
	public void detach() {
		super.detach();

		// TODO here to remve the element , and fire a unwire event to out.
	}

	public String getJoint() {
		return _joint;
	}

	public void setJoint(String joint) {
		this._joint = joint;
	}
}
