/**
 * 
 */
package org.zkoss.selector.lang;

/**
 * 
 * @author simonpai
 */
public class Token {
	
	private int _begin;
	private int _end;
	private Type _type;
	
	public Token(Type group, int begin, int end){
		_type = group;
		_begin = begin;
		_end = end;
	}
	
	public Type getType(){
		return _type;
	}
	
	public int getBeginIndex(){
		return _begin;
	}
	
	public int getEndIndex(){
		return _end;
	}
	
	public static enum Type {
		
		// noun body //
		IDENTIFIER,
		UNIVERSAL(1),
		WHITESPACE,
		
		// combinator //
		CHILD_CBN(1),
		ADJACENT_SIBLING_CBN(1),
		GENERAL_SIBLING_CBN(1),
		
		// selector notation //
		ID_NTN(1),
		CLASS_NTN(1),
		PSEUDO_CLASS_NTN(1),
		
		// pairwise //
		DOUBLE_QUOTE(1),
		OPEN_BRACKET(1),
		CLOSE_BRACKET(1),
		OPEN_PARAM(1),
		CLOSE_PARAM(1),
		
		// category (pseudo tokens) //
		SYMBOL(2);
		
		
		
		// structure //
		private int _limit;
		
		Type(){
			this(-1);
		}
		
		Type(int limit){
			_limit = limit;
		}
		
		public int getLimit(){
			return _limit;
		}
		
		
		
		// query //
		public static Type getGroup(char c, boolean escaped){
			
			if(escaped)
				return Character.isWhitespace(c)? WHITESPACE : IDENTIFIER;
			
			if(Character.isLetter(c) || Character.isDigit(c) 
					|| c=='_' || c=='-' || c=='%')
				return IDENTIFIER;
			if(Character.isWhitespace(c))
				return WHITESPACE;
			
			switch(c){
			case '*':
				return UNIVERSAL;
			case '>':
			case '+':
			case '~':
			case '#':
			case '.':
			case ':':
			case '$':
				return SYMBOL;
			case '"':
				return DOUBLE_QUOTE;
			case '[':
				return OPEN_BRACKET;
			case ']':
				return CLOSE_BRACKET;
			case '(':
				return OPEN_PARAM;
			case ')':
				return CLOSE_PARAM;
			}
			
			return null;
		}
		
		public static Type getSymbolType(char c){
			switch(c){
			case '>':
				return CHILD_CBN;
			case '+':
				return ADJACENT_SIBLING_CBN;
			case '~':
				return GENERAL_SIBLING_CBN;
				
			case '#':
				return ID_NTN;
			case '.':
				return CLASS_NTN;
			case ':':
				return PSEUDO_CLASS_NTN;
			}
			return null;
		}
		
		public static Type getSymbolType(char c1, char c2){
			// TODO
			return null;
		}
		
	}
	
}
