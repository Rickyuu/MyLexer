package com.ricky.main;
import java.util.ArrayList;
import java.util.Scanner;

import com.ricky.controller.DFA;
import com.ricky.controller.TransHelper;
import com.ricky.entity.Token;

/**
 *   测试类，进行初始化工作和测试
 *   @author Ricky Liang   
 *   @time   2012-11-10 下午3:06:42   
 */

public class TestLexer {
	/**
	 * 主方法，获得输入，并输出判断结果
	 * @param args
	 */
	public static void main(String args[]){
		System.out.println("Please wait for seconds, the system is loading......");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);		
		TestLexer testLexer = new TestLexer();
		ArrayList<DFA> dfa0s = testLexer.initDFA0s();
		System.out.println("You will have 20 chances to test......");
		for(int testCount=0; testCount<20; testCount++){
			System.out.println("Test"+(testCount+1));
			System.out.print("Input : ");
			String input = scanner.nextLine();
			boolean isAccepted = false;
			String isSpecial = testLexer.isSpecial(input);
			if(isSpecial != null){
				isAccepted = true;
				System.out.println("Token : "+isSpecial);
			}else{
				for(int i=0; i<dfa0s.size(); i++){
					DFA dfa0 = dfa0s.get(i);
					if(dfa0.accept(input)){
						isAccepted = true;
						System.out.println("Token : "+dfa0.getTokenName());
						break;
					}
				}
				if(isAccepted == false){
					System.out.println("Input is not accepted!");
				}
			}			
			System.out.println("-----------------------------------------");
		}		
	}
	
	/**
	 * 初始化Token
	 * @return
	 */
	private ArrayList<Token> initTokens(){
		ArrayList<Token> tokens = new ArrayList<Token>();
		tokens.add(new Token("(\\s)|(\\t)|(\\r)|(\\n)", "SKIP"));
		tokens.add(new Token("(if)|(else)|(while)|(do)|(break)|(switch)|(for)|(case)|(struct)|(typedef)", "KEYWORD"));
		tokens.add(new Token("(int)|(float)|(double)|(long)|(char)", "TYPE"));
		tokens.add(new Token("(unsigned)|(signed)", "TYPE_MODIFIER"));
		tokens.add(new Token("#((include)|(define)|(undef)|(ifdef)|(ifndef)|(if)|(endif)|(elif)|(else))", "PREPROC_DERIV"));
		tokens.add(new Token("(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|" +
				"A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|_)" +
				"(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|" +
				"A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|_" +
				"|0|1|2|3|4|5|6|7|8|9)*", "IDENTIFIER"));
		tokens.add(new Token("((1|2|3|4|5|6|7|8|9)" +
				"(0|1|2|3|4|5|6|7|8|9)*)|0", "INTEGER"));
		tokens.add(new Token("+|-|/|%", "ARITHMETIC_OP"));
		tokens.add(new Token("(==)|(&&)|!|<|>|(<=)|(>=)", "LOGICAL_OP"));
		tokens.add(new Token("^|&|~| ", "BIT_OP"));
		tokens.add(new Token("->", "ACCESS_OP"));
		tokens.add(new Token("=", "ASSIGN_OP"));
		tokens.add(new Token(";", "SEMICOLON"));
		tokens.add(new Token(".", "DOT"));
		tokens.add(new Token(",", "COMMA"));
		tokens.add(new Token("[", "LEFT_SQBRAC"));
		tokens.add(new Token("]", "RIGHT_SQBRAC"));
		tokens.add(new Token("{", "BLOCK_START"));
		tokens.add(new Token("}", "BLOCK_END"));
		return tokens;
	}
	
	/**
	 * 针对在正则表达式中已经出现的内容进行判断
	 * @param input
	 * @return
	 */
	private String isSpecial(String input){
		if(input.equals("*")){
			return "ARITHMETIC_OP";
		}else if(input.equals("||")){
			return "LOGICAL_OP";
		}else if(input.equals("|")){
			return "BIT_OP";
		}else if(input.equals("(")){
			return "LEFT_PAREN";
		}else if(input.equals(")")){
			return "RIGHT_PAREN";
		}
		return null;
	}
	
	/**
	 * 初始化DFA0
	 * @return
	 */
	private ArrayList<DFA> initDFA0s(){
		ArrayList<DFA> dfa0s = new ArrayList<DFA>();
		ArrayList<Token> tokens = initTokens();
		for(int i=0; i<tokens.size(); i++){
			Token token = tokens.get(i);
			String regularExpression = token.getRegularExpression();
			String tokenName = token.getTokenName();
			TransHelper transHelper = new TransHelper();
			DFA dfa0 = transHelper.getDFA0(regularExpression);
			dfa0.setTokenName(tokenName);
			dfa0s.add(dfa0);
		}
		return dfa0s;
	}
	
}
