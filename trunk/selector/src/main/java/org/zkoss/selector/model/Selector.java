/**
 * 
 */
package org.zkoss.selector.model;

import java.util.ArrayList;

import org.zkoss.selector.lang.Parser.ParseException;

/**
 * 
 * @author simonpai
 */
public class Selector extends ArrayList<SimpleSelectorSequence> {
	
	private static final long serialVersionUID = -9125226126564264333L;
	
	/**
	 * Add combinator to the last simple selector sequence
	 * @param combinator
	 */
	public void attachCombinator(Combinator combinator){
		if(isEmpty()) throw new ParseException(
				"Cannot have combinator prior to the first sequence of simple selectors.");
		get(size()-1).setCombinator(combinator);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Combinator getCombinator(int index){
		return get(index).getCombinator();
	}
	
	public enum Combinator {
		
		DESCENDANT(" "), CHILD(" > "), 
		ADJACENT_SIBLING(" + "), GENERAL_SIBLING(" ~ ");
		
		private String _str;
		
		Combinator(String str){
			_str = str;
		}

		@Override
		public String toString() {
			return _str;
		}
		
	}
	
	public String toDebugString(){
		StringBuffer sb = new StringBuffer();
		int size = size();
		for(int i=0; i<size; i++){
			SimpleSelectorSequence seq = get(i);
			sb.append("Sequence ").append(i).append(":\n")
				.append(seq.toDebugString()).append("\n");
			if(i < size-1)
				switch(seq.getCombinator()){
				case DESCENDANT:
					break;
				case CHILD:
					sb.append("Combinator: >\n\n");
					break;
				case ADJACENT_SIBLING:
					sb.append("Combinator: +\n\n");
					break;
				case GENERAL_SIBLING:
					sb.append("Combinator: ~\n\n");
					break;
				}
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int size = size();
		for(int i=0; i<size; i++){
			SimpleSelectorSequence seq = get(i);
			sb.append(seq);
			if(i < size-1) sb.append(seq.getCombinator());
		}
		return sb.toString();
	}
	
}
