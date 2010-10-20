/**
 * 
 */
package org.zkoss.selector.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author simonpai
 */
public class State<E extends Enum<E>, IN, C extends Enum<C>> {
	
	protected StateMachine<E, IN, C> _master;
	
	// local properties //
	protected Set<C> _returners;
	protected Map<C, E> _transitions;
	
	public State(){
		_returners = new HashSet<C>();
		_transitions = new HashMap<C, E>();
		init();
	}
	
	protected State<E, IN, C> setMaster(StateMachine<E, IN, C> master){
		_master = master;
		return this;
	}
	
	
	
	// definition //
	protected void init(){}
	
	public State<E, IN, C> addReturningClasses(C ... inputClasses){
		for(C c : inputClasses) _returners.add(c);
		return this;
	}
	
	public State<E, IN, C> addReturningClasses(Collection<C> collection){
		_returners.addAll(collection);
		return this;
	}
	
	public State<E, IN, C> addTransition(C inputClass, E destination){
		_transitions.put(inputClass, destination);
		return this;
	}
	
	public State<E, IN, C> addTransitions(E destination, C ... inputClasses){
		for(C c : inputClasses) _transitions.put(c, destination);
		return this;
	}
	
	
	
	// query //
	public boolean isReturning(C inputClass){
		return _returners.contains(inputClass);
	}
	
	public boolean isLeaving(C inputClass){
		return _transitions.keySet().contains(inputClass);
	}
	
	public E getDestination(C inputClass){
		return _transitions.get(inputClass);
	}
	
	
	
	// event handler //
	protected void onStart(IN input, C inputClass){}
	
	protected void onLand(IN input, C inputClass, E origin){}
	
	protected void onReturn(IN input, C inputClass){}
	
	protected void onReject(IN input, C inputClass){}
	
	protected void onLeave(IN input, C inputClass, E destination){}
	
	protected void onTerminate(IN input){}
	
}
