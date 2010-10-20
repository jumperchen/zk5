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
public abstract class StateMachine<E extends Enum<E>, IN, C extends Enum<C>> {
	
	protected Map<E, State<E, IN, C>> _states;
	protected E _current;
	protected boolean _run;
	protected int _step;
	
	public StateMachine(){
		_states = new HashMap<E, State<E, IN, C>>();
		init();
		reset();
	}
	
	
	
	// definition //
	public StateMachine<E, IN, C> setState(E token, State<E, IN, C> state){
		if(state == null) throw new IllegalArgumentException("State cannot be null");
		_states.put(token, state.setMaster(this));
		return this;
	}
	
	public State<E, IN, C> removeState(E token){
		return _states.remove(token).setMaster(null);
	}
	
	public State<E, IN, C> getState(E token){
		return getState(token, true);
	}
	
	public State<E, IN, C> getState(E token, boolean autoCreate){
		State<E, IN, C> result = _states.get(token);
		if(result == null && autoCreate) 
			_states.put(token, result = new State<E, IN, C>().setMaster(this));
		return result;
	}
	
	protected void init(){}
	protected abstract E getLandingPoint(IN input, C inputClass);
	protected abstract C getClass(IN input);
	
	protected void onStop(boolean endOfInput){}
	
	protected void onReject(IN input){
		throw new StateMachineException("Rejected at step " + _step + 
				" with current state: " + _current + ", input: " + input);
	}
	
	protected final void doReject(IN input){
		_run = false;
		onReject(input);
	}
	
	
	
	// operation //
	public void reset(){
		_current = null;
		_run = false;
		_step = 0;
	}
	
	public void run(Iterator<IN> inputs){
		_run = true;
		while(_run && inputs.hasNext())
			run(inputs.next());
		onStop(!inputs.hasNext());
	}
	
	public void run(IN input){
		C inputClass = getClass(input);
		if(inputClass == null) {
			doReject(input);
			return;
		}
		if(_current == null){
			_current = getLandingPoint(input, inputClass);
			if(_current == null) {
				doReject(input);
				return;
			}
			State<E, IN, C> state = _states.get(_current);
			if(state != null) state.onStart(input, inputClass);
			else throw new IllegalStateException();
		} else {
			State<E, IN, C> state = _states.get(_current);
			if(state == null) throw new IllegalStateException();
			
			if(state.isReturning(inputClass)) {
				state.onReturn(input, inputClass);
				
			} else if(state.isLeaving(inputClass)) {
				E dest = state.getDestination(inputClass);
				if(dest == null) {
					doReject(input);
					return;
				}
				state.onLeave(input, inputClass, dest);
				state = _states.get(dest);
				if(state != null) state.onLand(input, inputClass, _current);
				else throw new IllegalStateException();
				_current = dest;
				
			} else { // rejected by state
				state.onReject(input, inputClass);
				doReject(input);
				return;
			}
		}
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
	
	public void terminate(IN input){
		_states.get(_current).onTerminate(input);
		reset();
	}
	
	
	
	// status query //
	public boolean isTerminated(){
		return !_run && _current == null;
	}
	
	public boolean isSuspended(){
		return !_run && _current != null;
	}
	
	
	
	// exception //
	public static class StateMachineException extends RuntimeException {
		private static final long serialVersionUID = -6580348498729948101L;
		
		public StateMachineException(String msg) {
			super(msg);
		}
	}
	

}
