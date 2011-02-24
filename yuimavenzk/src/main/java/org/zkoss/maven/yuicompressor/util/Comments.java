package org.zkoss.maven.yuicompressor.util;


public class Comments {
//
//	public static String readFile(String file) {
//		try {
//			Scanner scanner = new Scanner(new FileReader(new File(file)));
//			StringBuffer sb = new StringBuffer();
//			while (scanner.hasNextLine()) {
//				sb.append(scanner.nextLine()+"\r\n");
//				
//			}
//			return sb.toString();
//		} catch (Exception e) {
//			return "";
//		}
//	}
//
//	public static void main(String[] args) {
//
//		System.out.println(removeComment(readFile("skin.js")));
//		
//	}

	public static String removeComment(String input) {

		StringBuffer sb = new StringBuffer(input);
		char NQ = ' ', quote = NQ;
		int len = sb.length();
		for (int j = 0, lineno = 1; j < len; j++) {
			if (sb.charAt(j) == '\n')
				++lineno;

			if (quote != NQ) {
				if (sb.charAt(j) == quote){
					quote = NQ;
				}else if (sb.charAt(j) == '\\') {
					j++;
					//fix for  "123\\\r\n123" 
					if(sb.charAt(j)=='\r') j++;
					// if(sb.charAt(j) == '\n') j++;
				} else if (sb.charAt(j) == '\n') {
					throw new IllegalStateException("Unterminated string at line " + lineno);
				}
			} else if (sb.charAt(j) == '/' && j + 1 < len && (sb.charAt(j + 1) == '*' || sb.charAt(j + 1) == '/')) {
				int l = j;
				boolean eol = sb.charAt(++j) == '/';
				while (++j < len) {
					if (sb.charAt(j) == '\n')
						++lineno;

					if (eol) {
						if (sb.charAt(j) == '\n') {
							sb.delete(l, sb.charAt(j - 1) == '\r' ? j - 1 : j);
							len = sb.length();
							j = l;
							break;
						}
					} else if (sb.charAt(j) == '*' && j + 1 < len && sb.charAt(j + 1) == '/') {
						sb.delete(l, j + 2);
						len = sb.length();
						j = l;
						break;
					}
				}
			} else if (sb.charAt(j) == '\'' || sb.charAt(j) == '"') {
				quote = sb.charAt(j);
			} else if (sb.charAt(j) == '/') { // regex
				boolean regex = false;
				for (int k = j;;) {
					if (--k < 0) {
						regex = true;
						break;
					}

					char ck = sb.charAt(k);
					if (!Character.isWhitespace(ck)) {
						regex = ck == '(' || ck == ',' || ck == '=' || ck == ':' || ck == '?' || ck == '{' || ck == '['
								|| ck == ';' || ck == '!' || ck == '&' || ck == '|' || ck == '^'
								|| (ck == 'n' && k > 4 && "return".equals(sb.substring(k - 5, k + 1)))
								|| (ck == 'e' && k > 2 && "case".equals(sb.substring(k - 3, k + 1)));
						break;
					}
				}
				if (regex) {
					while (++j < len && sb.charAt(j) != '/') {
						if (sb.charAt(j) == '\\')
							j++;
						else if (sb.charAt(j) == '\n') {
							throw new IllegalStateException("Unterminated regex at line " + lineno);
						}
					}
				}
			}
		}
		return sb.toString();
	}

}
