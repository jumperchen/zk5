/**
 * 
 */
package org.zkoss.selector.model;

import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author simonpai
 */
public class SimpleSelectorSequence {
	
	private Combinator _combinator;
	private String _type;
	private String _id;
	private Set<String> _classes;
	// attributes        [xxx=yyy]
	// pseudo classes    :xxx
	
	// pseudo element    ::xxx
	
	public SimpleSelectorSequence(){
		_classes = new HashSet<String>();
	}
	
	public Combinator getCombinator(){
		return _combinator;
	}
	
	public void setCombinator(Combinator combinator){
		_combinator = combinator;
	}
	
	public String getType(){
		return _type;
	}
	
	public void setType(String type){
		_type = type;
	}
	
	public String getId(){
		return _id;
	}
	
	public void setId(String id){
		_id = id;
	}
	
	public Set<String> getClasses(){
		return _classes;
	}
	
	public void addClass(String clazz){
		if(!_classes.contains(clazz)) _classes.add(clazz);
	}
	
}
