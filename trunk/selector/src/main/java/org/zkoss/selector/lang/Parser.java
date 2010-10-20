/**
 * 
 */
package org.zkoss.selector.lang;

import org.zkoss.selector.model.Combinator;
import org.zkoss.selector.model.Selector;
import org.zkoss.selector.model.SimpleSelectorSequence;
import org.zkoss.selector.util.MacroState;
import org.zkoss.selector.util.State;
import org.zkoss.selector.util.StateMachine;

/**
 * @author simonpai
 *
 */
public class Parser extends StateMachine<Parser.ParseState, Token, Parser.CharClass>{
	
	private final Selector _selector;
	private SimpleSelectorSequence _currentSeq;
	
	public Parser(Tokenizer tokenizer){
		super();
		_selector = new Selector();
		run(tokenizer);
	}
	
	public Selector getSelector(){
		return _selector;
	}
	
	
	
	@Override
	protected void init() {
		// TODO: check with 5-states
		
		getState(ParseState.PRE_SELECTOR)
			.addReturningClasses(CharClass.WHITESPACE)
			.addTransition(CharClass.SELECTOR_LITERAL, ParseState.IN_SELECTOR);
		
		setState(ParseState.IN_SELECTOR,
				new MacroState<ParseState, Token, CharClass, SubState, SubCharClass>(
						new StateMachine<SubState, Token, SubCharClass>() {
							
							@Override
							protected SubCharClass getClass(Token input) {
								return null; // TODO
							}
							
							@Override
							protected SubState getLandingPoint(Token input,
									SubCharClass inputClass) {
								return null; // TODO
							}
							
							@Override
							protected void init() {
								// TODO
							}
							
							@Override
							protected void onStop(boolean endOfInput){
								// TODO
								
							}
							
						}) {
					
					// macro state methods //
					@Override
					protected void init() {
						addReturningClasses(CharClass.SELECTOR_LITERAL);
						addTransition(CharClass.WHITESPACE, ParseState.PRE_COMBINATOR);
					}
					
					@Override
					protected void onStart(Token input, CharClass inputClass) {
						startNewSequence();
						super.onStart(input, inputClass);
					}
					
					@Override
					protected void onLand(Token input, CharClass inputClass, 
							ParseState origin) {
						startNewSequence();
						super.onLand(input, inputClass, origin);
					}
					
					private void startNewSequence(){
						Parser.this._currentSeq = new SimpleSelectorSequence();
					}
				});
		
		setState(ParseState.PRE_COMBINATOR, 
				new State<ParseState, Token, CharClass>(){
					
					@Override
					protected void init() {
						addReturningClasses(CharClass.WHITESPACE);
						addTransition(CharClass.COMBINATOR, ParseState.PRE_SELECTOR);
						addTransition(CharClass.SELECTOR_LITERAL, ParseState.IN_SELECTOR);
					}
					
					@Override
					protected void onLand(Token input, CharClass inputClass, 
							ParseState origin) {
						
						// TODO: set combinator
					}
					
					@Override
					protected void onLeave(Token input, CharClass inputClass,
							ParseState destination) {
						
						// TODO: flush
					}
					
					
		});
	}
	
	@Override
	protected void onStop(boolean endOfInput) {
		// TODO: flush the last sequence
	}

	@Override
	protected CharClass getClass(Token input) {
		switch(input.getType()){
		case WHITESPACE:
			return CharClass.WHITESPACE;
			
		case CHILD_CBN:
		case ADJACENT_SIBLING_CBN:
		case GENERAL_SIBLING_CBN:
			return CharClass.COMBINATOR;
			
		case IDENTIFIER:
		case UNIVERSAL:
		case ID_NTN:
		case CLASS_NTN:
		case PSEUDO_CLASS_NTN:
		case DOUBLE_QUOTE:
		case OPEN_BRACKET:
		case CLOSE_BRACKET:
		case OPEN_PARAM:
		case CLOSE_PARAM:
			return CharClass.SELECTOR_LITERAL;
		}
		return null;
	}

	@Override
	protected ParseState getLandingPoint(Token input, CharClass inputClass) {
		switch(inputClass){
		case WHITESPACE:
			return ParseState.PRE_SELECTOR;
		case SELECTOR_LITERAL:
			return ParseState.IN_SELECTOR;
		}
		return null;
	}
	
	
	
	// state //
	public enum ParseState {
		PRE_SELECTOR, POST_SELECTOR, IN_SELECTOR, PRE_COMBINATOR, POST_COMBINATOR;
	}
	
	public enum CharClass {
		SELECTOR_LITERAL, WHITESPACE, COMBINATOR;
	}
	
	public enum SubState {
		// TODO
	}
	
	public enum SubCharClass {
		// TODO
	}
	
	
	
	// helper //
	private Combinator getCombinator(Token token){
		switch(token.getType()){
		case CHILD_CBN:
			return Combinator.CHILD;
		case ADJACENT_SIBLING_CBN:
			return Combinator.ADJACENT_SIBLING;
		case GENERAL_SIBLING_CBN:
			return Combinator.GENERAL_SIBLING;
		}
		throw new IllegalStateException();
	}
	
}
