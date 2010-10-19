/**
 * 
 */
package org.zkoss.selector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.selector.lang.Token;
import org.zkoss.selector.lang.Tokenizer;
import org.zkoss.selector.model.SimpleSelectorSequence;

/**
 * 
 * @author simonpai
 */
public class Selector {
	
	private String _source;
	private List<SimpleSelectorSequence> _sequences;
	
	public Selector(String str){
		_sequences = new LinkedList<SimpleSelectorSequence>();
		_source = str;
		
		List<Token> tokens = new ArrayList<Token>();
		Tokenizer t = new Tokenizer(str);
		while(t.hasNext()) tokens.add(t.next());
		
		//parse(str);
	}
	
	private void parse(){
		
	}
	
}
