package org.zkoss.swifttab;

import java.io.IOException;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;
import org.zkoss.zul.Tabs;


public class Mtabs extends Tabs{
	static {
		addClientEvent(Swifttab.class, Events.ON_CHANGE, CE_IMPORTANT | CE_NON_DEFERRABLE);
	}
	private boolean _movable = false;

	public boolean isMovable() {
		return _movable;
	}

	public void onChange(Event event){
//		event.
	}
	public void setMovable(boolean movable) {
		if (_movable != movable) {
			this._movable = movable;
			smartUpdate("movable", _movable);
		}
	}
	protected void renderProperties(ContentRenderer renderer) throws IOException {
		super.renderProperties(renderer);

		render(renderer, "movable", _movable);
	}
}
