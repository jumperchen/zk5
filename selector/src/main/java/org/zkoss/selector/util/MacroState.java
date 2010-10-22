/**
 * 
 */
package org.zkoss.selector.util;

/**
 * 
 * @author simonpai
 */
public class MacroState<E, C, IN, E2, C2> extends State<E, C, IN> {
	
	// sub machine //
	protected StateMachine<E2, C2, IN> _submachine;
	
	public MacroState(StateMachine<E2, C2, IN> submachine) {
		super();
		_submachine = submachine;
	}
	
	
	
	// event handler //
	@Override
	protected void onLand(IN input, C inputClass, E origin) {
		_submachine.start(input);
	}
	@Override
	protected void onReturn(IN input, C inputClass) {
		_submachine.run(input);
	}
	@Override
	protected void onLeave(IN input, C inputClass, E destination) {
		_submachine.terminateAt(input);
	}
	
}
