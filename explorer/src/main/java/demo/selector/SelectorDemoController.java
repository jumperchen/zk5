/**
 * 
 */
package demo.selector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletContext;

import org.zkoss.selector.ComponentIterator;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 
 * @author simonpai
 */
public class SelectorDemoController extends GenericForwardComposer {
	
	private static final long serialVersionUID = 6738045491398493198L;
	
	private static final String CLEAR = "clear.zul";
	
	private Window componentsWindow;
	private Textbox zulSrcBox;
	private Textbox selectorBox;
	private Listbox zulListbox; 
	private Label indexLb;
	private Label compLb;
	private Label parseLb;
	private Label timeLb;
	private Label iterLb;
	
	private String _currSelector;
	private ComponentIterator _iterator;
	private Component _selected;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		final String curPath = 
			((ServletContext) desktop.getWebApp().getNativeContext())
				.getRealPath("selector/case");
		File root = new File(curPath);
		File[] zuls = root.listFiles(new FileFilter(){
			public boolean accept(File f) {
				return f.isFile() && f.getName().endsWith(".zul");
			}
		});
		Arrays.sort(zuls, new java.util.Comparator<File>() {
			public int compare(File a, File b) {
				String aname = a.getName();
				String bname = b.getName();
				if(CLEAR.equals(aname)) return -1;
				if(CLEAR.equals(bname)) return 1;
				return aname.compareTo(bname);
			}
		});
		zulListbox.setItemRenderer(new ListitemRenderer(){
			public void render(Listitem listitem, Object data) {
				final File f = (File) data;
				final String fname = f.getName();
				listitem.setLabel(fname.substring(0, fname.length()-4));
				listitem.addEventListener("onClick", new EventListener(){
					public void onEvent(Event event) throws Exception {
						if(CLEAR.equals(fname)) {
							zulSrcBox.setValue("");
							Components.removeAllChildren(componentsWindow);
						} else {
							zulSrcBox.setValue(readFile(f));
							loadZul();
						}
					}
				});
			}
		});
		zulListbox.setModel(new ListModelList(zuls));
	}
	
	public void onClick$stepBtn(Event event) {
		step();
	}
	
	public void onOK$selectorBox(Event event) {
		step();
	}
	
	public void onClick$resetBtn(Event event) {
		_iterator = getComponentIterator(selectorBox.getValue());
		removeMark(_selected);
		_selected = null;
		indexLb.setValue("-");
		compLb.setValue("-");
		timeLb.setValue("-");
		iterLb.setValue("");
	}
	
	public void onChange$zulSrcBox(Event event) {
		loadZul();
	}
	
	public void onClick$clearBtn(Event event) {
		zulSrcBox.setValue("");
		Components.removeAllChildren(componentsWindow);
	}
	
	
	
	// helper //
	private ComponentIterator getComponentIterator(String selector){
		long pst = new Date().getTime();
		ComponentIterator result = new ComponentIterator(componentsWindow, selector);
		long ptime = new Date().getTime() - pst;
		parseLb.setValue("" + ptime + "ms");
		return result;
	}
	
	private void step(){
		String selector = selectorBox.getValue();
		if(!selector.equals(_currSelector)) {
			_currSelector = selector;
			_iterator = getComponentIterator(selector);
		}
		
		removeMark(_selected);
		long start = new Date().getTime();
		boolean hasNext = _iterator.hasNext();
		long timeCost = new Date().getTime() - start;
		addMark(_selected = hasNext? _iterator.next() : null);
		indexLb.setValue("" + (_iterator.getIndex() - 1));
		compLb.setValue(_selected == null? "null" : _selected.toString());
		iterLb.setValue(_iterator.toString());
		timeLb.setValue("" + timeCost + "ms");
	}
	
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
	
	private String readFile(File file) throws Exception {
		String newLine = System.getProperty("line.separator");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String next = "";
		StringBuffer sb = new StringBuffer();
		while((next = br.readLine()) != null)
			sb.append(next).append(newLine);
		return sb.toString();
	}
}

