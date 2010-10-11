/*
 * UnwireEvent.java
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

public class UnwireEvent extends Event {

	private final String _id;

	public UnwireEvent(String name, Component target, String id) {
		super(name, target);
		_id = id;
	}

	/** Converts an AU request to a wire event.
	 */
	public static final UnwireEvent getUnwireEvent(AuRequest request) {
		final Component comp = request.getComponent();
		if (comp == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, request);
		final Map data = request.getData();
		if (data == null)
			throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA,
				new Object[] {data, request});
		return new UnwireEvent(request.getCommand(), comp, (String)data.get("id"));
	}

	/** Returns the id of the component.
	 */
	public final String getId() {
		return _id;
	}
}
