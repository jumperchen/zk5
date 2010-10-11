/*
 * Wirebox.java
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zkex.wire;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.lang.Objects;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zkex.wire.event.WireEvent;
import org.zkoss.zkex.wire.event.WireEvents;
import org.zkoss.zul.impl.XulElement;

public class Wirebox extends XulElement implements Serializable {

	{
		addClientEvent(Wirebox.class, WireEvents.ON_WIRE, CE_IMPORTANT | CE_NON_DEFERRABLE);
		addClientEvent(Wirebox.class, WireEvents.ON_UNWIRE, CE_IMPORTANT | CE_NON_DEFERRABLE);
	}

	private ArrayList _wires;

	private String _points = "";


	public String _drawmethod = "bezierArrow";

	public Wirebox() {
		_wires = new ArrayList();
	}

	/* package */void addWire(Wire w) {

		if (!_wires.contains(w))
			_wires.add(w);

	}


	public Wire getWire(String joint){
		List wires = getWires();

		for(int i=0;i<wires.size();++i){
			Wire w = (Wire) wires.get(i);

			String[] joints=w.getJoint().split(",");

			if(w.getIn()==this && joints[0]!= null && joints[0].equals(joint) ){
				return w;
			}else if(w.getOut() == this && joints[1] != null && joints[1].equals(joint)){
				return w;
			}
		}
		return null;
	}

	/* package */void removeWire(Wire w) {
		_wires.remove(w);
	}

	public List getWires() {
		// TonyQ:
		// TODO check here if it's risk to let user get real instance for list
		// I mean , user can really modify the collection .
		// Here is two choice , one for return real instance , another one for
		// return a clone.
		// I return the real instance first, because I think user should manage
		// it by them self.
		return _wires;
	}

	public void setPoints(String points) {
		_points = points;
		if ( !Objects.equals(_points, points)) {
			smartUpdate("points", _points);
		}
	}

	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);

		render(renderer, "points", _points);
		render(renderer, "drawmethod", _drawmethod);
	}

	/**
	 * the default z-class is "z-wirebox"
	 */
	public String getZclass() {
		return this._zclass == null ? "z-wirebox" : this._zclass;
	}

	public void service(AuRequest request, boolean everError) {

		final String cmd = request.getCommand();

		if (cmd.equals(WireEvents.ON_WIRE)) {
			// create wire and add it here
			WireEvent evt = WireEvent.getOnWireEvent(request);
			Events.postEvent(evt);
		}else if(cmd.equals(WireEvents.ON_UNWIRE)){
			WireEvent evt = WireEvent.getUnWireEvent(request);
			Events.postEvent(evt);
		}else
			super.service(request, everError);
	}

	public String getDrawmethod() {
		return _drawmethod;
	}


	public void setDrawmethod(String drawmethod) {
		if(!Objects.equals(this._drawmethod,drawmethod)){
			smartUpdate("_drawmethod", drawmethod);
		}
		this._drawmethod = drawmethod;
	}
}
