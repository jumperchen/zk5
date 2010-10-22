/**
 * 
 */
package org.zkoss.selector.demo;

import java.util.List;

import org.zkoss.selector.lang.Token;
import org.zkoss.selector.lang.Tokenizer;

/**
 * @author simonpai
 *
 */
public class TokenizerTest {
	
	public static void main(String[] args){
		
		//String selector = "div#id.class span.class2 > #id3 ~ intbox";
		String selector = "[attr$=\"value\"]    ~   div:pseudo(2)[attr2*=596]  ";
		
		Tokenizer t = new Tokenizer();
		t.setDebugMode(true);
		List<Token> tokens = t.tokenize(selector);
		
		System.out.println("");
		System.out.println("==== Tokens ====");
		int n = 1;
		for(Token tk : tokens) 
			System.out.println("" + n++ + ": " + tk + " (" + tk.source(selector) + ")");
	}
	
}
