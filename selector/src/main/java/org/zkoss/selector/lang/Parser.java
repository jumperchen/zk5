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
 * 
 * @author simonpai
 */
public class Parser extends StateMachine<Parser.ParseState, Parser.CharClass, Token>{
	
	private String _source;
	private Selector _selector;
	private SimpleSelectorSequence _currentSeq;
	
	public Parser(Tokenizer tokenizer){
		super();
		//_source = tokenizer.getSourceString();
		//_selector = new Selector();
		//run(tokenizer);
	}
	
	public Selector getSelector(){
		return _selector;
	}
	
	
	
	@Override
	protected void init() {
		
		getState(ParseState.PRE_SELECTOR)
			.addTransition(CharClass.SELECTOR_LITERAL, ParseState.IN_SELECTOR);
		
		setState(ParseState.IN_SELECTOR,
				new MacroState<ParseState, CharClass, Token, SubParseState, Token.Type>(
						new StateMachine<SubParseState, Token.Type, Token>() {
							
							@Override
							protected Token.Type getClass(Token input) {
								return input.getType();
							}
							
							@Override
							protected SubParseState getLandingPoint(Token input,
									Token.Type inputClass) {
								// TODO
								switch(inputClass){
								case IDENTIFIER:
									
								case UNIVERSAL:
									
								case NTN_ID:
									
								case NTN_CLASS:
									
								case NTN_PSEUDO_CLASS:
									
								case OPEN_BRACKET:
									
								default:
									return null;
								}
							}
							
							@Override
							protected void init() {
								// TODO
								// lots of states
							}
							
							@Override
							protected void onStart(Token input, Token.Type inputClass,
									SubParseState landing){
								Parser.this._currentSeq = new SimpleSelectorSequence();
							}
							
							@Override
							protected void onStop(boolean endOfInput){
								if(!endOfInput) return;
								// flush sequence if valid
								// TODO: check
								_selector.add(_currentSeq);
								_currentSeq = null;
							}
							
						}) {
					
					// macro state methods //
					@Override
					protected void init() {
						addReturningClasses(CharClass.SELECTOR_LITERAL);
						addTransition(CharClass.WHITESPACE, ParseState.PRE_COMBINATOR);
					}
					
				});
		
		setState(ParseState.PRE_COMBINATOR, 
				new State<ParseState, CharClass, Token>(){
					
					@Override
					protected void init() {
						addTransition(CharClass.COMBINATOR, ParseState.POST_COMBINATOR);
						addTransition(CharClass.SELECTOR_LITERAL, ParseState.IN_SELECTOR);
					}
					
					@Override
					protected void onLand(Token input, CharClass inputClass, 
							ParseState origin) {
						
						if(_currentSeq == null) throw new IllegalStateException();
						_currentSeq.setCombinator(getCombinator(input));
					}
					
					@Override
					protected void onLeave(Token input, CharClass inputClass,
							ParseState destination) {
						
						// flush sequence
						_selector.add(_currentSeq);
						_currentSeq = null;
					}
					
					
		});
		
		getState(ParseState.POST_COMBINATOR)
			.addTransition(CharClass.WHITESPACE, ParseState.PRE_SELECTOR);
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
			
		case CBN_CHILD:
		case CBN_ADJACENT_SIBLING:
		case CBN_GENERAL_SIBLING:
			return CharClass.COMBINATOR;
			
		default:
			return CharClass.SELECTOR_LITERAL;
		}
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
		PRE_SELECTOR, IN_SELECTOR, PRE_COMBINATOR, POST_COMBINATOR;
	}
	
	public enum CharClass {
		SELECTOR_LITERAL, WHITESPACE, COMBINATOR;
	}
	
	public enum SubParseState {
		// TODO
	}
	
	
	
	// helper //
	private Combinator getCombinator(Token token){
		switch(token.getType()){
		case CBN_CHILD:
			return Combinator.CHILD;
		case CBN_ADJACENT_SIBLING:
			return Combinator.ADJACENT_SIBLING;
		case CBN_GENERAL_SIBLING:
			return Combinator.GENERAL_SIBLING;
		}
		throw new IllegalStateException();
	}
	
}
