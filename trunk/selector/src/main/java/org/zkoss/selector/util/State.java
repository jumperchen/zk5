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
public class State<E, C, IN> {
	
	protected StateMachine<E, C, IN> _master;
	
	// local properties //
	protected boolean _returnAll;
	protected Set<C> _returners;
	protected Set<IN> _minorReturners;
	protected Map<C, E> _transitions;
	protected Map<IN, E> _minorTransitions;
	
	public State(){
		_returners = new HashSet<C>();
		_minorReturners = new HashSet<IN>();
		_transitions = new HashMap<C, E>();
		_minorTransitions = new HashMap<IN, E>();
		init();
	}
	
	protected State<E, C, IN> setMaster(StateMachine<E, C, IN> master){
		_master = master;
		return this;
	}
	
	
	
	// definition //
	protected void init(){}
	
	public State<E, C, IN> addReturningClasses(C ... inputClasses){
		for(C c : inputClasses) _returners.add(c);
		return this;
	}
	
	public State<E, C, IN> addReturningClasses(Collection<C> collection){
		_returners.addAll(collection);
		return this;
	}
	
	public State<E, C, IN> addReturningInputs(IN ... inputs){
		for(IN i : inputs) _minorReturners.add(i);
		return this;
	}
	
	public State<E, C, IN> addReturningInputs(Collection<IN> collection){
		_minorReturners.addAll(collection);
		return this;
	}
	
	public State<E, C, IN> setReturningAll(boolean returnAll){
		_returnAll = returnAll;
		return this;
	}
	
	public State<E, C, IN> addTransition(C inputClass, E destination){
		_transitions.put(inputClass, destination);
		return this;
	}
	
	public State<E, C, IN> addTransitions(E destination, C ... inputClasses){
		for(C c : inputClasses) _transitions.put(c, destination);
		return this;
	}
	
	public State<E, C, IN> addMinorTransition(IN input, E destination){
		_minorTransitions.put(input, destination);
		return this;
	}
	
	public State<E, C, IN> addMinorTransitions(E destination, IN ... inputs){
		for(IN i : inputs) _minorTransitions.put(i, destination);
		return this;
	}
	
	
	
	// query //
	public boolean isReturningAll(){
		return _returnAll;
	}
	
	public boolean isReturning(IN input, C inputClass){
		return _returnAll || _returners.contains(inputClass) || 
			_minorReturners.contains(input);
	}
	
	public boolean isLeaving(IN input, C inputClass){
		return _transitions.containsKey(inputClass) || 
			_minorTransitions.containsKey(input);
	}
	
	// TODO: add switch to check/skip returning condition
	public E getDestination(IN input, C inputClass){
		E result = _minorTransitions.get(input);
		return (result != null)? result : _transitions.get(inputClass);
	}
	
	
	
	// event handler //
	protected void onLand(IN input, C inputClass, E origin){}
	protected void onReturn(IN input, C inputClass){}
	protected void onReject(IN input, C inputClass){}
	protected void onLeave(IN input, C inputClass, E destination){}
	
}
