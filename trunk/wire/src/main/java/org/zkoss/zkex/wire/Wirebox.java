package org.zkoss.zkex.wire;

import java.io.IOException;

import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;



public class Wirebox extends XulElement {
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);
	}

	public String getZclass(){
		return this._zclass ==null ? "z-wirebox" : this._zclass;
	}
}
