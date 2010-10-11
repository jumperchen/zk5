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

	public Wire() {
	}

	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
	}

	/**
	 * No child is allowed.
	 */
	protected boolean isChildable() {
		return false;
	}
}
