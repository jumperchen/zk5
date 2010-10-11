/*
 * WireEvent.java
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.zkex.wire.event;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.wire.Wire;
import org.zkoss.zkex.wire.Wirebox;

/**
 * @author Joy Lo
 * @since 5.0.0
 */
public class WireEvent extends Event {

	private Wire _wire;

	/**
	 * pack a on-wire event from a au request, a example from client side fire event.
	 *
	 * <pre>
	 * fire("onWire", {
	 *     inbox: wire.getIn(),
	 *     outbox: wire.getOut(),
	 *     joint:wire._joint
	 * })
	 * </pre>
	 *
	 * @see Wirebox#service(AuRequest, boolean)
	 * @param request
	 *            the au Request from client.
	 * @return
	 */
	public static final WireEvent getOnWireEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();

		Wirebox inbox = (Wirebox) request.getDesktop().getComponentByUuidIfAny((String) data.get("inbox"));
		Wirebox outbox = (Wirebox) request.getDesktop().getComponentByUuidIfAny((String) data.get("outbox"));
		if (inbox == null || outbox == null || !data.containsKey("joint"))
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { data, request });

		Wire wire = new Wire();
		wire.setIn(inbox);
		wire.setOut(outbox);
		wire.setJoint((String) data.get("joint"));

		return new WireEvent(request.getCommand(), outbox, wire, null);
	}
	/**
	 * pack a unwire event from a au request,
	 * a example from client side fire event.
	 * <pre>
	 *  fire("onUnWire",
	 *  	{ boxid:that._id,
                        point:that._point
            }
        );
       </pre>
	 * @param request
	 * @return
	 */
	public static final WireEvent getUnWireEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();

		Wirebox targetbox = (Wirebox) request.getDesktop().getComponentByUuidIfAny((String) data.get("boxid"));

		if (targetbox == null  && !data.containsKey("joint"))
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { data, request });


		String joint = (String) data.get("joint");

		Wire unwiredWire = targetbox.getWire(joint);
		if(unwiredWire == null )
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { data, request });

		unwiredWire.detach(false);
		return new WireEvent(request.getCommand(), unwiredWire.getOut(), unwiredWire, null);
	}
	/**
	 * create a wire event
	 *
	 * @param name
	 * @param target
	 * @param wire
	 * @param data
	 */
	public WireEvent(String name, Component target, Wire wire, Object data) {
		super(name, target, data);
		this._wire = wire;
	}

	/**
	 * create a wire event
	 *
	 * @param name
	 * @param target
	 * @param wire
	 */
	public WireEvent(String name, Component target, Wire wire) {
		this(name, target, wire, null);
	}

	public Wire getWire(){
		return _wire;
	}
}
