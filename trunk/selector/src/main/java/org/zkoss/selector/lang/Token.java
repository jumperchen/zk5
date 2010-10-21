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
		// selector body //
		IDENTIFIER, UNIVERSAL,
		
		// white space //
		WHITESPACE,
		
		// combinator //
		CBN_CHILD, CBN_ADJACENT_SIBLING, CBN_GENERAL_SIBLING,
		
		// selector notation //
		NTN_ID, NTN_CLASS, NTN_PSEUDO_CLASS,
		
		// attribute boolean operator //
		OP_EQUAL, OP_BEGIN_WITH, OP_END_WITH, OP_CONTAINS,
		
		// pairwise //
		DOUBLE_QUOTE, OPEN_BRACKET, CLOSE_BRACKET, OPEN_PARAM, CLOSE_PARAM;
		
		// query //
		public static Type getSymbolType(char c){
			switch(c){
			case '>':
				return CBN_CHILD;
			case '+':
				return CBN_ADJACENT_SIBLING;
			case '~':
				return CBN_GENERAL_SIBLING;
				
			case '#':
				return NTN_ID;
			case '.':
				return NTN_CLASS;
			case ':':
				return NTN_PSEUDO_CLASS;
			}
			return null;
		}
		
		public static Type getSymbolType(char c1, char c2){
			// TODO
			return null;
		}
		
	}
	
	public enum CharType {
		// selector body //
		LITERAL,
		UNIVERSAL(1),
		SYMBOL(2),
		
		// white space //
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
		CLOSE_PARAM(1);
		
		// structure //
		private int _limit;
		
		CharType(){
			this(-1);
		}
		
		CharType(int limit){
			_limit = limit;
		}
		
		public int getLimit(){
			return _limit;
		}
		
		public static CharType getGroup(char c, boolean escaped){
			
			if(escaped)
				return Character.isWhitespace(c)? WHITESPACE : LITERAL;
			
			if(Character.isLetter(c) || Character.isDigit(c) 
					|| c=='_' || c=='-' || c=='%')
				return LITERAL;
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

	}
	
}
