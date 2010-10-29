/**
 * 
 */
package demo.selector;

import org.zkoss.selector.ComponentIterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 
 * @author simonpai
 */
public class SelectorDemoController extends GenericForwardComposer {
	
	private static final long serialVersionUID = 6738045491398493198L;
	
	Window componentsWindow;
	Textbox zulSrcBox;
	Textbox selectorBox;
	Button stepBtn;
	Label indexLb;
	Label compLb;
	Label iterLb;
	
	private String _currSelector;
	private ComponentIterator _iterator;
	private Component _selected;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		
	}
	
	public void onClick$stepBtn(Event event) {
		
		String selector = selectorBox.getValue();
		if(!selector.equals(_currSelector)) {
			_currSelector = selector;
			_iterator = new ComponentIterator(componentsWindow, selector);
		}
		
		removeMark(_selected);
		addMark(_selected = _iterator.hasNext()? _iterator.next() : null);
		indexLb.setValue("" + _iterator.getIndex());
		compLb.setValue(_selected == null? "null" : _selected.toString());
		iterLb.setValue(_iterator.toString());
	}
	
	public void onClick$resetBtn(Event event) {
		_iterator = new ComponentIterator(componentsWindow, selectorBox.getValue());
		removeMark(_selected);
		_selected = null;
		indexLb.setValue("-");
		compLb.setValue("-");
		iterLb.setValue("");
	}
	
	public void onChange$zulSrcBox(Event event) {
		loadZul();
	}
	
	
	
	// helper //
	private void loadZul(){
		Components.removeAllChildren(componentsWindow);
		try {
			Executions.createComponentsDirectly(zulSrcBox.getValue(), "zul", 
					componentsWindow, null);
		} catch (RuntimeException e) {
			Executions.createComponentsDirectly("Error loading ZUL", "zul", 
					componentsWindow, null);
		}
	}
	
	private void removeMark(Component selected){
		if(selected == null || !(selected instanceof HtmlBasedComponent)) return;
		HtmlBasedComponent comp = (HtmlBasedComponent) selected;
		String sc = comp.getSclass();
		comp.setSclass(sc.substring(0, sc.lastIndexOf(" __selected__")));
	}
	
	private void addMark(Component selected){
		if(selected == null || !(selected instanceof HtmlBasedComponent)) return;
		HtmlBasedComponent comp = (HtmlBasedComponent) selected;
		comp.setSclass(comp.getSclass() + " __selected__");
	}
	
	
	
	// zuls //
	
}

