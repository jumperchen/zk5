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

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zkex.wire.event.WireEvent;
import org.zkoss.zkex.wire.event.WireEvents;

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
			// we dont redner "in" because it will cause javascript error in ie
			// "in" in javascript is a preserved keyboard.
			render(renderer, "inId", _in.getUuid());

		}
		if (_out != null) {
			// keep same style with in
			render(renderer, "outId", _out.getUuid());
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
	public void setIn(Wirebox in) {
		if (!Objects.equals(_in, in)) {
			if (_in != null)
				_in.removeWire(this);
			_in = in;
			_in.addWire(this);
		}
	}

	/**
	 * set the ID of wirebox in
	 *
	 * @param id
	 *            wirebox's id
	 */
	public void setIn(String id) {
		setIn((Wirebox) this.getFellow(id));
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
	public void setOut(Wirebox out) {
		if (!Objects.equals(_out, out)) {
			if (_out != null)
				_out.removeWire(this);
			_out = out;
			_out.addWire(this);
			if (getParent() == null) {
				attach();
			}
		}
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
	 * drawingMethod: this support 6 types from wireit .
	 * <ul>
	 * <li>
	 * straight</li>
	 * <li>
	 * bezier</li>
	 * <li>
	 * arrow</li>
	 * <li>
	 * bezierArrow</li>
	 * <li>
	 * leftSquareArrow</li>
	 * <li>
	 * rightSquareArrow</li>
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

	public void attach() {
		if (getParent() == null && _out != null && _out.getParent() != null) {
			setParent(_out.getParent());
			_out.addWire(this);
			if (_in != null)
				_in.addWire(this);
		}
	}

	/**
	 * detach from view . (means it will not longer exist in dom)
	 *
	 * @param fireUnwire
	 *            for trigger to fire a unwire event or not , default is true.
	 * @see {@link WireEvent#getUnWireEvent(org.zkoss.zk.au.AuRequest)}
	 *
	 */
	public void detach(boolean fireUnwire) {
		if (fireUnwire) {
			WireEvent wireEvent = new WireEvent(WireEvents.ON_UNWIRE, _out, this, null);
			Events.postEvent(wireEvent);
		}
		if (_in != null)
			_in.removeWire(this);
		if (_out != null)
			_out.removeWire(this);
		super.detach();
	}

	/**
	 * default detach method , detach from view . (means it will not longer exist in dom)
	 */
	public void detach() {
		detach(true);
	}

	public String getJoint() {
		return _joint;
	}

	public void setJoint(String joint) {
		this._joint = joint;
	}
}
