/*
 * MoveTabEvent.java
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/9/20, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package org.zkoss.swifttab.event;

import java.util.Map;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

/**
 *
 * @author tony
 *
 */
public class MoveTabEvent extends Event {

	public final static String NAME = "onTabMove";

	private int startIndex = -1;

	private int endIndex = -1;

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public MoveTabEvent(String command, Component target, int start, int end) {
		super(command, target);

		startIndex = start;
		endIndex = end;
	}

	public static final MoveTabEvent getMoveTabEvent(AuRequest request) {
		final Component tabs = request.getComponent();

		final Map data = request.getData();

		int startIndex = AuRequests.getInt(data, "start", -1);
		int endIndex = AuRequests.getInt(data, "end", -1);

		if (startIndex == -1 || endIndex == -1) {
			throw new IllegalArgumentException("startIndex/endIndex wrong.");
		}

		return new MoveTabEvent(request.getCommand(), tabs, startIndex, endIndex);
	}
}
