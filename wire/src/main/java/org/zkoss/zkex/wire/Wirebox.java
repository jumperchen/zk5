package org.zkoss.zkex.wire;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.impl.XulElement;

public class Wirebox extends XulElement implements Serializable {

	private ArrayList<Wire> _wires;

	private String _points = "";

	public Wirebox() {
		_wires = new ArrayList<Wire>();
	}

	private void addWire(Wire w) {

		_wires.add(w);

	}

	private void removeWire(Wire w) {
		_wires.remove(w);
	}

	public List<Wire> getWires() {
		// TODO check here if risk to let user get real instance for list
		// i mean , user can really modifly the collection . by Tony
		return _wires;
	}

	private void setPoints(String points, boolean ignoreUpdate) {
		_points = points;
		if (!ignoreUpdate) {
			smartUpdate("points", _points);
		}
	}

	public void setPoints(String points) {
		setPoints(points, false);
	}

	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);

		render(renderer,"points",_points);
	}

	public String getZclass() {
		return this._zclass == null ? "z-wirebox" : this._zclass;
	}
}
