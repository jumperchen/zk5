/**
 * 
 */
package org.zkoss.selector.util;

/**
 * @author simonpai
 *
 */
public abstract class MacroState<E extends Enum<E>, IN, C extends Enum<C>, 
	E2 extends Enum<E2>, C2 extends Enum<C2>> extends State<E, IN, C> {
	
	// sub machine //
	protected StateMachine<E2, IN, C2> _submachine;
	
	public MacroState(StateMachine<E2, IN, C2> submachine) {
		super();
		_submachine = submachine;
	}
	
	
	
	// TODO: may support additional interface for different timing
	
	@Override
	protected void onStart(IN input, C inputClass) {
		super.onStart(input, inputClass);
		_submachine.start(input);
	}
	@Override
	protected void onLand(IN input, C inputClass, E origin) {
		super.onLand(input, inputClass, origin);
		_submachine.start(input);
	}
	@Override
	protected void onReturn(IN input, C inputClass) {
		super.onReturn(input, inputClass);
		_submachine.run(input);
	}
	@Override
	protected void onLeave(IN input, C inputClass, E destination) {
		_submachine.terminate(input);
		super.onLeave(input, inputClass, destination);
	}
	@Override
	protected void onTerminate(IN input) {
		_submachine.terminate(input);
		super.onTerminate(input);
	}
}
