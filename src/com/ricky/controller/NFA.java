package com.ricky.controller;
import java.util.ArrayList;
import java.util.LinkedList;

import com.ricky.entity.Rule;
import com.ricky.entity.State;

/**
 *   描述NFA的类
 *   @author Ricky Liang   
 *   @time   2012-11-5 下午6:39:50   
 */
public class NFA {
	/**
	 * 开始NFA状态
	 * 结束NFA状态
	 * 所有NFA状态
	 * 所有NFA状态转换规则
	 * 所有终结符
	 */
	private State beginState;
	private State finalState;
	private ArrayList<State> states;
	private ArrayList<Rule> rules;
	private ArrayList<Character> terminals;
	
	public NFA(State beginState,State finalState){
		this.beginState = beginState;
		this.finalState = finalState;
		states = new ArrayList<State>();
		rules = new ArrayList<Rule>();
		terminals = new ArrayList<Character>();
	}
	
	/**
	 * 根据终结符生成的NFA
	 * @param key
	 */
	public NFA(char key){
		this.beginState = new State(TransHelper.stateNums++);
		this.finalState = new State(TransHelper.stateNums++);
		states = new ArrayList<State>();
		states.add(beginState);
		states.add(finalState);
		Rule newRule = new Rule(beginState, key, finalState);
		rules = new ArrayList<Rule>();
		rules.add(newRule);
		terminals = new ArrayList<Character>();
		if(key!=' '){
			terminals.add(key);
		}		
	}
	
	/**
	 * 进行|操作后的NFA
	 * @param other
	 * @return
	 */
	public NFA orNFA(NFA other){
		State resultBeginState = new State(TransHelper.stateNums++);
		State resultFinalState = new State(TransHelper.stateNums++);
		NFA resultNFA = new NFA(resultBeginState, resultFinalState);
		resultNFA.addState(this.states);
		resultNFA.addState(other.states);
		resultNFA.addState(resultBeginState);
		resultNFA.addState(resultFinalState);
		resultNFA.addRule(this.rules);
		resultNFA.addRule(other.getRules());
		Rule newRule1 = new Rule(resultBeginState, ' ', this.beginState);
		Rule newRule2 = new Rule(resultBeginState, ' ', other.beginState);
		Rule newRule3 = new Rule(this.finalState, ' ', resultFinalState);
		Rule newRule4 = new Rule(other.finalState, ' ', resultFinalState);
		resultNFA.addRule(newRule1);
		resultNFA.addRule(newRule2);
		resultNFA.addRule(newRule3);
		resultNFA.addRule(newRule4);	
		resultNFA.addTerminal(this.terminals);
		resultNFA.addTerminal(other.terminals);
		return resultNFA;
	}
	
	/**
	 * 进行·操作后的NFA
	 * @param other
	 * @return
	 */
	public NFA joinNFA(NFA other){
		State resultBeginState = this.beginState;
		State resultFinalState = other.finalState;
		NFA resultNFA = new NFA(resultBeginState, resultFinalState);
		resultNFA.addState(this.states);
		resultNFA.addState(other.states);
		resultNFA.addRule(this.rules);
		resultNFA.addRule(other.rules);
		Rule newRule = new Rule(this.finalState, ' ', other.beginState);
		resultNFA.addRule(newRule);	
		resultNFA.addTerminal(this.terminals);
		resultNFA.addTerminal(other.terminals);
		return resultNFA;
	}
	
	/**
	 * 进行*操作后的NFA
	 * @return
	 */
	public NFA repeatNFA(){
		State resultBeginState = new State(TransHelper.stateNums++);
		State resultFinalState = new State(TransHelper.stateNums++);
		NFA resultNFA = new NFA(resultBeginState, resultFinalState);
		resultNFA.addState(this.states);
		resultNFA.addState(resultBeginState);
		resultNFA.addState(resultFinalState);		
		resultNFA.addRule(this.rules);
		Rule newRule1 = new Rule(resultBeginState, ' ', this.beginState);
		Rule newRule2 = new Rule(this.finalState, ' ', resultFinalState);
		Rule newRule3 = new Rule(this.finalState, ' ', this.beginState);
		Rule newRule4 = new Rule(resultBeginState, ' ', resultFinalState);
		resultNFA.addRule(newRule1);
		resultNFA.addRule(newRule2);
		resultNFA.addRule(newRule3);
		resultNFA.addRule(newRule4);
		resultNFA.addTerminal(this.terminals);
		return resultNFA;
	}
	
	/**
	 * 过期的生成 yigequnao 闭包的方法，用了递归，因为效率和栈溢出问题，已经用下面的方法改进
	public ArrayList<State> getBlankClosure(State state){
		ArrayList<State> helpClosure = new ArrayList<State>();
		return getBlankClosure(state, helpClosure);
	}
	
	private ArrayList<State> getBlankClosure(State state, ArrayList<State> blankClosure){
		blankClosure.add(state);
		for(int i=0; i<rules.size(); i++){
			Rule currentRule = rules.get(i);
			if(currentRule.getCurrentState() == state && currentRule.getKey() == ' '){
				State nextState = currentRule.getNextState();
				if(!blankClosure.contains(nextState)){
					blankClosure.addAll(getBlankClosure(nextState,blankClosure));
				}				
			}
		}
		return blankClosure;
	}
	*/
	
	/**
	 * 生成某状态 yigequnao 闭包的方法
	 */
	public ArrayList<State> getBlankClosure(State state){
		ArrayList<State> blankClosure = new ArrayList<State>();
		LinkedList<State> queue = new LinkedList<State>();
		blankClosure.add(state);
		queue.offer(state);	
		while(!queue.isEmpty()){
			State currentState = queue.poll();
			for(int i=0; i<rules.size(); i++){
				Rule currentRule = rules.get(i);
				if(currentRule.getCurrentState() == currentState && currentRule.getKey() == ' '){
					State nextState = currentRule.getNextState();
					if(!blankClosure.contains(nextState)){
						blankClosure.add(nextState);
						queue.offer(nextState);
					}
				}
			}
		}
		return blankClosure;
	}
	
	/**
	 * 获得某NFA状态经过某个终结符后到达的状态
	 * @param state
	 * @param key
	 * @return
	 */
	public State getKeyState(State state, char key){
		State resultState = null;
		for(int i=0; i<rules.size(); i++){
			Rule currentRule = rules.get(i);
			if(currentRule.getCurrentState() == state && currentRule.getKey() == key){
				resultState = currentRule.getNextState();
			}
		}
		return resultState;
	}
	
	/**
	 * 打印NFA，该方法用于写代码过程中转换DFA后的正确性检测 
	public void printNFA(){
		System.out.println("下面是NFA中的内容：");
		System.out.println("开始状态："+beginState.getStateNo());
		System.out.println("结束状态："+finalState.getStateNo());
		System.out.println("所有状态如下：");
		for(int i=0; i<states.size(); i++){
			System.out.print(states.get(i).getStateNo()+";");
		}
		System.out.println();
		System.out.println("所有非终结符如下：");
		for(int i=0; i<terminals.size(); i++){
			System.out.print(terminals.get(i)+";");
		}
		System.out.println();
		System.out.println("所有规则如下：");
		for(int i=0; i<rules.size(); i++){
			Rule rule = rules.get(i);
			System.out.print("{"+rule.getCurrentState().getStateNo()+","+rule.getKey()+","+rule.getNextState().getStateNo()+"};");
		}
		System.out.println();
	}
	*/
	
	private void addState(State newState){
		states.add(newState);
	}
	
	private void addState(ArrayList<State> newStates){
		for(int i=0; i<newStates.size(); i++){
			states.add(newStates.get(i));
		}
	}
	
	private void addRule(Rule newRule){
		rules.add(newRule);
	}
	
	private void addRule(ArrayList<Rule> newRules){
		for(int i=0; i<newRules.size(); i++){
			rules.add(newRules.get(i));
		}
	}
	
	private void addTerminal(ArrayList<Character> newTerminals){
		for(int i=0; i<newTerminals.size(); i++){
			if(!terminals.contains(newTerminals.get(i))){
				terminals.add(newTerminals.get(i));
			}		
		}
	}
	
	public State getBeginState() {
		return beginState;
	}

	public void setBeginState(State beginState) {
		this.beginState = beginState;
	}

	public State getFinalState() {
		return finalState;
	}

	public void setFinalState(State finalState) {
		this.finalState = finalState;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}
	
	public ArrayList<Character> getTerminals(){
		return terminals;
	}

}


