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
	
	
	
	// event handler (State) //
	@Override
	protected void onLand(IN input, C inputClass, E origin) {
		super.onLand(input, inputClass, origin);
		//beforeMachineStart(input, inputClass, origin);
		_submachine.start(input);
	}
	@Override
	protected void onReturn(IN input, C inputClass) {
		super.onReturn(input, inputClass);
		//beforeMachineRun(input, inputClass);
		_submachine.run(input);
	}
	@Override
	protected void onLeave(IN input, C inputClass, E destination) {
		_submachine.terminateAt(input);
		//afterMachineStop(input, inputClass, destination);
		super.onLeave(input, inputClass, destination);
	}
	
	
	
	// event handler (MacroState) //
	//protected void beforeMachineStart(IN input, C inputClass, E origin){}
	//protected void beforeMachineRun(IN input, C inputClass){}
	//protected void afterMachineStop(IN input, C inputClass, E destination){}
}
