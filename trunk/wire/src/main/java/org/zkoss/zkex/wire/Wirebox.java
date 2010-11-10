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
import java.util.Iterator;
import java.util.List;

import org.zkoss.lang.Library;
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
	private ArrayList _ins;

	private ArrayList _outs;

	private String _points = "";

	public String _drawmethod = "bezierArrow";

	public Wirebox() {
		String method = Library.getProperty("wire.drawmethod");
		if(method !=null ){
			_drawmethod = method;
		}
			
		_ins = new ArrayList();
		_outs= new ArrayList();
	}

	/* package */void addIn(Wire w) {
		if(!_ins.contains(w)) _ins.add(w);
	}
	
	/* package */void addOut(Wire w) {
		if(!_outs.contains(w)) _outs.add(w);
	}

	public ArrayList getAvailableWires(String joint) {
		ArrayList wires = getWires(joint);
		ArrayList outputs = new ArrayList();
		
		for (Iterator ir = wires.iterator(); ir.hasNext();) {
			Wire w = (Wire) ir.next();
			if(w.getParent()!=null){
				outputs.add(w);
			}
		}
		
		return outputs;
		
	}
	public ArrayList getWires(String joint) {
		List wires = getWires();
		ArrayList outputs = new ArrayList();
		for (int i = 0; i < wires.size(); ++i) {
			Wire w = (Wire) wires.get(i);
			String[] joints = w.getJoint().split(",");

			if (w.getIn() == this && joints[0] != null && joints[0].equals(joint)) {
				outputs.add(w);
			} else if (w.getOut() == this && joints[1] != null && joints[1].equals(joint)) {
				outputs.add(w);
			}
		}
		return outputs;
	}

	/* package */void removeIn(Wire w) {
		 _ins.remove(w);
	}
	/* package */void removeOut(Wire w) {
		 _outs.remove(w);
	}

	public List getWires() {
		ArrayList list= new ArrayList();
		list.addAll(_ins);
		list.addAll(_outs);
		return list;
	}

	public void setPoints(String points) {
		_points = points;
		if (!Objects.equals(_points, points)) {
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
		} else if (cmd.equals(WireEvents.ON_UNWIRE)) {
			WireEvent evt = WireEvent.getUnWireEvent(request);
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}

	public String getDrawmethod() {
		return _drawmethod;
	}

	public void setDrawmethod(String drawmethod) {
		if (!Objects.equals(this._drawmethod, drawmethod)) {
			smartUpdate("drawmethod", drawmethod);
		}
		this._drawmethod = drawmethod;
	}

	public ArrayList getIns() {
		return _ins;
	}

	public void setIns(ArrayList ins) {
		this._ins = ins;
	}

	public ArrayList getOuts() {
		return _outs;
	}

	public void setOuts(ArrayList outs) {
		this._outs = outs;
	}

}
