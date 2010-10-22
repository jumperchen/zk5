/**
 * 
 */
package org.zkoss.selector.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author simonpai
 */
public abstract class StateMachine<E, C, IN> {
	
	protected Map<E, State<E, C, IN>> _states;
	protected E _current;
	protected boolean _run;
	protected int _step;
	protected boolean _debug;
	
	public StateMachine(){
		_states = new HashMap<E, State<E, C, IN>>();
		_debug = false;
		init();
		reset();
	}
	
	
	
	// system /
	public StateMachine<E, C, IN> setDebugMode(boolean mode){
		_debug = mode;
		return this;
	}
	
	// definition //
	public StateMachine<E, C, IN> setState(E token, State<E, C, IN> state){
		if(state == null) throw new IllegalArgumentException(
				"State cannot be null. Use removeState() to remove a state.");
		_states.put(token, state.setMaster(this));
		return this;
	}
	
	public State<E, C, IN> removeState(E token){
		return _states.remove(token).setMaster(null);
	}
	
	public State<E, C, IN> getState(E token){
		return getState(token, true);
	}
	
	public State<E, C, IN> getState(E token, boolean autoCreate){
		State<E, C, IN> result = _states.get(token);
		if(result == null && autoCreate) 
			_states.put(token, result = new State<E, C, IN>().setMaster(this));
		return result;
	}
	
	protected void init(){}
	protected abstract E getLandingPoint(IN input, C inputClass);
	protected abstract C getClass(IN input);
	
	// event handler //
	protected void onReset(){}
	protected void onStart(IN input, C inputClass, E landing){}
	protected void onBeforeStep(IN input, C inputClass){}
	protected void onAfterStep(IN input, C inputClass, E origin, E destination){}
	protected void onStop(boolean endOfInput){}
	
	protected void onReject(IN input){
		throw new StateMachineException("Rejected at step " + _step + 
				" with current state: " + _current + ", input: " + input);
	}
	
	protected void onDebug(String message){
		System.out.println(message);
	}
	
	
	
	// operation //
	public void run(Iterator<IN> inputs){
		_run = true;
		while(_run && inputs.hasNext())
			run(inputs.next());
		onStop(!inputs.hasNext());
		doDebug("Stop");
	}
	
	public void run(IN input){
		
		C inputClass = getClass(input);
		
		doDebug("\nStep " + _step);
		doDebug("* Input: " + input + " (" + inputClass + ")");
		
		onBeforeStep(input, inputClass);
		
		final E origin = _current;
		E destination = null;
		
		if(inputClass == null) {
			doReject(input);
			return;
		}
		if(origin == null){
			destination = getLandingPoint(input, inputClass); // dest
			if(destination == null) {
				doReject(input);
				return;
			}
			onStart(input, inputClass, destination);
			getState(destination).onLand(input, inputClass, origin);
			
		} else {
			State<E, C, IN> state = getState(origin);
			
			if(state.isLeaving(input, inputClass)) {
				destination = state.getDestination(input, inputClass); // dest
				if(destination == null) {
					doReject(input);
					return;
				}
				state.onLeave(input, inputClass, destination);
				getState(destination).onLand(input, inputClass, origin);
				
			} else if(state.isReturning(input, inputClass)) {
				destination = origin; // dest
				state.onReturn(input, inputClass);
				
			} else { // rejected by state
				state.onReject(input, inputClass);
				doReject(input);
				return;
			}
		}
		
		_current = destination;
		
		doDebug("* State: " + origin + " -> " + destination);
		
		onAfterStep(input, inputClass, origin, destination);
		_step++;
	}
	
	public void start(Iterator<IN> inputs){
		reset();
		run(inputs);
	}
	
	public void start(IN input){
		reset();
		run(input);
	}
	
	public void suspend(){
		_run = false;
	}
	
	public void terminate(){
		reset();
	}
	
	
	
	// status query //
	// TODO: enhance
	public E getCurrentState(){
		return _current;
	}
	
	public boolean isTerminated(){
		return !_run && _current == null;
	}
	
	public boolean isSuspended(){
		return !_run && _current != null;
	}
	
	
	
	// default internal operation //
	protected final void doReject(IN input){
		_run = false;
		onReject(input);
	}
	
	protected final void doDebug(String message){
		if(_debug) onDebug(message);
	}
	
	private void reset(){
		_current = null;
		_run = false;
		_step = 0;
		doDebug("Reset");
		onReset();
	}
	
	/*package*/ final void terminateAt(IN input){
		getState(_current).onLeave(input, null, null);
		reset();
	}
	
	
	
	// exception //
	public static class StateMachineException extends RuntimeException {
		private static final long serialVersionUID = -6580348498729948101L;
		
		public StateMachineException(String msg) {
			super(msg);
		}
	}
	

}
