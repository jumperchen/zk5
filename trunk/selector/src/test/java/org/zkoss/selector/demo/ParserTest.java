/**
 * 
 */
package org.zkoss.selector.demo;

import java.util.List;

import org.zkoss.selector.lang.Parser;
import org.zkoss.selector.lang.Token;
import org.zkoss.selector.lang.Tokenizer;
import org.zkoss.selector.model.Selector;

/**
 * @author simonpai
 *
 */
public class ParserTest {
	
	public static void main(String[] args){
		
		//String selector = "div#id.class span.class2 > #id3 ~ intbox";
		String selector = "[attr$=\"value\"]    +   div:pseudo(2)[attr2*=596]  tab#kerker";
		
		List<Token> tokens = new Tokenizer().tokenize(selector);
		Parser p = new Parser();
		p.setDebugMode(true);
		
		Selector model = p.parse(tokens, selector);
		
		System.out.println("\n\n==== Selector ====\n\n");
		System.out.println(model.toDebugString());
		System.out.println("\n\n==== Selector repacked ====\n\n");
		System.out.println(model);
	}
	
}
