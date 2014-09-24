package com.ricky.controller;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import com.ricky.entity.CollectionState;

/**
 *   ת�������ĸ�����
 *   @author Ricky Liang   
 *   @time   2012-11-2 ����3:51:02   
 */
public class TransHelper {
	/**
	 * NFA״̬����
	 * DFA״̬����
	 * �ȼ�״̬���ϼ���
	 */
	public static int stateNums = 0;
	public static int collectionStateNums = 0;
	public static int collectionSetNums = 0;

	/**
	 * ���ڴ�����ɹ����У���������NFA��DFA����ȷ��
	public static void main(String args[]){
		TransHelper transHelper = new TransHelper();
		Scanner scanner = new Scanner(System.in);
		while(true){
			String infix = "((1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*)|0";
			String extendInfix = transHelper.getExtendInfix(infix);
			String postfix = transHelper.transToPost(extendInfix);
			System.out.println(postfix);
			NFA resultNFA = transHelper.getNFA(postfix);
			resultNFA.printNFA();
			DFA resultDFA = transHelper.getDFA(resultNFA);
			resultDFA.printDFA();
			DFA minDFA = resultDFA.getMinDFA();
			minDFA.printDFA();
		}
	}
	*/
	
	/**
	 * ����������ʽ�����DFA0
	 */
	public DFA getDFA0(String regularExpression){
		String extendExpression = getExtendInfix(regularExpression);
		String postfixExpression = transToPost(extendExpression);
		NFA nfa = getNFA(postfixExpression);
		DFA dfa = getDFA(nfa);
		DFA dfa0 = dfa.getMinDFA();
		return dfa0;
	}
	
	/**
	 * ����׺���ʽ��ӡ�����
	 * @param infix
	 * @return
	 */
	private String getExtendInfix(String infix){
		StringBuilder stringBuilder = new StringBuilder();
		char[] infixChars = infix.toCharArray();
		for(int i=0; i<infixChars.length; i++){
			stringBuilder.append(infixChars[i]);
			if(!isSymbol(infixChars[i]) || infixChars[i] == ')' || infixChars[i] == '*'){
				if((i+1)<infixChars.length){
					if(!isSymbol(infixChars[i+1])){
						stringBuilder.append('��');
					}else if(infixChars[i+1] == '('){
						stringBuilder.append('��');
					}
				}
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * ����׺���ʽת���ɺ�׺���ʽ
	 * @param infix
	 * @return
	 */
	private String transToPost(String infix){
		StringBuilder transResult = new StringBuilder();
		Stack<Character> symbolStack = new Stack<Character>();
		symbolStack.push('#');
		char[] infixChars = infix.toCharArray();
		for(int i=0; i<infixChars.length; i++){
			char currentChar = infixChars[i];
			char currentStackTop = symbolStack.peek();
			if(currentChar == '|' || currentChar == '��'){
				if(currentStackTop == '|' || currentStackTop == '��'){
					transResult.append(symbolStack.pop());					
				}
				symbolStack.push(currentChar);
			}else if(currentChar == '('){
				symbolStack.push(currentChar);
			}else if(currentChar == ')'){
				while(symbolStack.peek() != '('){
					transResult.append(symbolStack.pop());
				}
				symbolStack.pop();
			}else if(currentChar == '*'){
				transResult.append(currentChar);
			}else{
				transResult.append(currentChar);
			}			
		}
		while(symbolStack.peek() != '#') {
			transResult.append(symbolStack.pop());
		}
		symbolStack.pop();
		return transResult.toString();
	} 
	
	/**
	 * ���ݺ�׺���ʽ����NFA
	 * @param postfix
	 * @return
	 */
	private NFA getNFA(String postfix){
		Stack<NFA> stack = new Stack<NFA>();
		char[] postfixChars = postfix.toCharArray();
		for(int i=0; i<postfixChars.length; i++){
			if(postfixChars[i]=='|'){
				NFA originNFA2 = stack.pop();
				NFA originNFA1 = stack.pop();
				NFA resultNFA = originNFA1.orNFA(originNFA2);
				stack.push(resultNFA);
			}else if(postfixChars[i]=='��'){
				NFA originNFA2 = stack.pop();
				NFA originNFA1 = stack.pop();
				NFA resultNFA = originNFA1.joinNFA(originNFA2);
				stack.push(resultNFA);
			}else if(postfixChars[i]=='*'){
				NFA orginNFA = stack.pop();
				NFA resultNFA = orginNFA.repeatNFA();
				stack.push(resultNFA);
			}else{
				NFA resultNFA = new NFA(postfixChars[i]);
				stack.push(resultNFA);
			}
		}
		return stack.pop();
	}
	
	/**
	 * ����NFA����û����С��DFA
	 * @param nfa
	 * @return
	 */
	private DFA getDFA(NFA nfa){
		DFA dfa = new DFA(nfa);
		ArrayList<Character> terminals = nfa.getTerminals();
		LinkedList<CollectionState> queue = new LinkedList<CollectionState>();
		queue.offer(dfa.getStartCollectionState());
		while(!queue.isEmpty()){
			CollectionState collectionState = queue.poll();
			for(int i=0; i<terminals.size(); i++){
				char key = terminals.get(i);
				CollectionState newCollectionState = dfa.getCollectionState(collectionState, key);
				if(newCollectionState!=null && !dfa.hasThisCollection(newCollectionState)){
					queue.offer(newCollectionState);
				}
			}
		}
		return dfa;
	}
	
	/**
	 * �ж�ĳ���ַ��Ƿ�Ϊ�����ַ�
	 * @param charToJudge
	 * @return
	 */
	private boolean isSymbol(char charToJudge){
		if(charToJudge == '*' || charToJudge == '(' || charToJudge == ')' || charToJudge == '|' || charToJudge == '��'){
			return true;
		}
		return false;
	}
	
}


