package com.ricky.controller;
import java.util.ArrayList;

import com.ricky.entity.CollectionRule;
import com.ricky.entity.CollectionSet;
import com.ricky.entity.CollectionState;
import com.ricky.entity.State;


/**
 *   描述DFA的类
 *   @author Ricky Liang   
 *   @time   2012-11-5 下午10:50:23   
 */
public class DFA {
	/**
	 * 生成该DFA的NFA
	 * 开始DFA状态
	 * 中止DFA状态
	 * 所有DFA状态
	 * 所有的DFA转换规则
	 * 该DFA所对应的Token的名称
	 */
	private NFA nfa;
	private CollectionState startCollectionState;
	private ArrayList<CollectionState> finalCollectionStates;
	private ArrayList<CollectionState> collectionStates;
	private ArrayList<CollectionRule> collectionRules;
	private String tokenName;
	
	public DFA(NFA nfa){
		this.nfa = nfa;
		finalCollectionStates = new ArrayList<CollectionState>();
		collectionStates = new ArrayList<CollectionState>();
		collectionRules = new ArrayList<CollectionRule>();
		tokenName = null;
		getStartCollection();
	}
	
	public DFA(ArrayList<CollectionState> collectionStates, ArrayList<CollectionRule> collectionRules, CollectionState startCollectionState, ArrayList<CollectionState> finalCollectionStates){
		this.nfa = null;
		this.collectionStates = collectionStates;
		this.collectionRules = collectionRules;
		this.startCollectionState = startCollectionState;
		this.finalCollectionStates = finalCollectionStates;
		tokenName = null;
	}
	
	/**
	 * 获得DFA的开始状态
	 * @return
	 */
	private CollectionState getStartCollection(){
		ArrayList<State> resultStates = nfa.getBlankClosure(nfa.getBeginState());
		CollectionState startCollectionState = new CollectionState(TransHelper.collectionStateNums++, resultStates);
		startCollectionState.setIsTerminalState(isTerminalState(startCollectionState));
		if(startCollectionState.getIsTerminalState()){
			finalCollectionStates.add(startCollectionState);
		}
		collectionStates.add(startCollectionState);
		this.startCollectionState = startCollectionState;
		return startCollectionState;
	}
	
	/**
	 * 找出从一个DFA状态，经过终结符所到达的DFA状态，并把DFA规则加入DFA
	 * @param collectionState
	 * @param key
	 * @return
	 */
	public CollectionState getCollectionState(CollectionState collectionState, char key){
		ArrayList<State> resultStates = new ArrayList<State>();
		ArrayList<State> currentStates = collectionState.getStatesInCollection();
		ArrayList<State> keyStates = new ArrayList<State>();
		for(int i=0; i<currentStates.size(); i++){
			State state = nfa.getKeyState(currentStates.get(i), key);
			if(state != null){
				keyStates.add(state);
			}
		}
		if(keyStates.size() == 0){
			return null;
		}
		for(int i=0; i<keyStates.size(); i++){
			ArrayList<State> blankClosure = nfa.getBlankClosure(keyStates.get(i));
			for(int j=0; j<blankClosure.size(); j++){
				if(!resultStates.contains(blankClosure.get(j))){
					resultStates.add(blankClosure.get(j));
				}
			}
		}
		for(int i=0; i<collectionStates.size(); i++){
			if(collectionStates.get(i).isEqual(resultStates)){
				CollectionRule newCollectionRule = new CollectionRule(collectionState, key, collectionStates.get(i));
				collectionRules.add(newCollectionRule);
				return collectionStates.get(i);
			}
		}
		CollectionState newCollectionState = new CollectionState(TransHelper.collectionStateNums++, resultStates);
		CollectionRule newCollectionRule = new CollectionRule(collectionState, key, newCollectionState);
		collectionRules.add(newCollectionRule);
		return newCollectionState;
	}
	
	/**
	 * 判断某状态是否为终止状态
	 * @param collectionState
	 * @return
	 */
	public boolean isTerminalState(CollectionState collectionState){
		if(collectionState.getStatesInCollection().contains(nfa.getFinalState())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断某个DFA状态是否已经存在
	 * @param collectionState
	 * @return
	 */
	public boolean hasThisCollection(CollectionState collectionState){
		if(collectionStates.contains(collectionState)){
			return true;			
		}
		collectionState.setIsTerminalState(isTerminalState(collectionState));
		if(collectionState.getIsTerminalState()){
			finalCollectionStates.add(collectionState);
		}
		collectionStates.add(collectionState);
		return false;
	}
	
	/**
	 * 判断该DFA是否接受一个输入串
	 * @param input
	 * @return
	 */
	public boolean accept(String input){
		char[] inputChars = input.toCharArray();
		CollectionState currentState = startCollectionState;
		for(int inputCount=0; inputCount<inputChars.length; inputCount++){
			char key = inputChars[inputCount];
			CollectionState nextState = getNextState(currentState, key);
			if(nextState == null){
				return false;
			}else{
				currentState = nextState;
			}
		}
		if(finalCollectionStates.contains(currentState)){
			return true;
		}
		return false;
	}
	
	/**
	 * 从一个DFA状态，经过终结符到达的另一个DFA状态，不添加规则
	 * @param currentState
	 * @param key
	 * @return
	 */
	private CollectionState getNextState(CollectionState currentState, char key){
		for(int i=0; i<collectionRules.size(); i++){
			CollectionRule currentRule = collectionRules.get(i);
			if(currentRule.getCurrentCollectionState() == currentState && currentRule.getKey() == key){
				return currentRule.getNextCollectionState();
			}
		}
		return null;
	}
	
	/**
	 * 获得最小化的DFA
	 * @return
	 */
	public DFA getMinDFA(){
		/*
		 * 找出等价类划分后的有用状态
		 * 找出等价类划分后的有用规则
		 * 新建一个DFA
		 * */
		ArrayList<CollectionState> usefulCollectionStates = getMinCollectionStates();
		ArrayList<CollectionRule> usefulCollectionRules = getMinRules();
		ArrayList<CollectionState> usefulFinalCollectionStates = new ArrayList<CollectionState>();
		for(int i=0; i<usefulCollectionStates.size(); i++){
			CollectionState currentCollectionState = usefulCollectionStates.get(i);
			if(finalCollectionStates.contains(currentCollectionState)){
				usefulFinalCollectionStates.add(currentCollectionState);
			}
		}
		DFA minDFA = new DFA(usefulCollectionStates, usefulCollectionRules, startCollectionState, usefulFinalCollectionStates);
		return minDFA;
	}
	
	/**
	 * 获得等价类划分后的有用的规则
	 * @return
	 */
	private ArrayList<CollectionRule> getMinRules(){
		
		ArrayList<CollectionState> usefulCollectionStates = new ArrayList<CollectionState>();
		ArrayList<CollectionSet> collectionSets = getMinDFACollectionSets();
		for(int i=0; i<collectionSets.size(); i++){
			CollectionSet currentCollectionSet = collectionSets.get(i);
			ArrayList<CollectionState> currentCollectionStates = currentCollectionSet.getCollectionStates();
			currentCollectionSet.setRepresentCollectionState(currentCollectionStates.get(0));
			usefulCollectionStates.add(currentCollectionSet.getRepresentCollectionState());
		}
		
		ArrayList<CollectionRule> usefulCollectionRules = new ArrayList<CollectionRule>();
		for(int i=0; i<collectionRules.size(); i++){
			CollectionRule currentCollectionRule = collectionRules.get(i);
			CollectionState currentCollectionState = currentCollectionRule.getCurrentCollectionState();
			if(usefulCollectionStates.contains(currentCollectionState)){
				usefulCollectionRules.add(currentCollectionRule);
			}
		}
		
		for(int i=0; i<usefulCollectionRules.size(); i++){
			CollectionRule currentCollectionRule = usefulCollectionRules.get(i);
			CollectionState currentNextState = currentCollectionRule.getNextCollectionState();
			if(!usefulCollectionStates.contains(currentNextState)){
				for(int j=0; j<collectionSets.size(); j++){
					CollectionSet currentCollectionSet = collectionSets.get(j);
					if(currentCollectionSet.containsState(currentNextState)){
						CollectionState representCollectionState = currentCollectionSet.getRepresentCollectionState();
						currentCollectionRule.setNextCollectionState(representCollectionState);
						break;
					}
				}
			}
		}
		return usefulCollectionRules;
	}
	
	/**
	 * 获得等价类划分后的有用的状态
	 * @return
	 */
	private ArrayList<CollectionState> getMinCollectionStates(){
		ArrayList<CollectionState> usefulCollectionStates = new ArrayList<CollectionState>();
		ArrayList<CollectionSet> collectionSets = getMinDFACollectionSets();
		for(int i=0; i<collectionSets.size(); i++){
			CollectionSet currentCollectionSet = collectionSets.get(i);
			ArrayList<CollectionState> currentCollectionStates = currentCollectionSet.getCollectionStates();
			currentCollectionSet.setRepresentCollectionState(currentCollectionStates.get(0));
			usefulCollectionStates.add(currentCollectionSet.getRepresentCollectionState());
		}
		return usefulCollectionStates;
	}
	
	/**
	 * 等价类划分
	 * @return
	 */
	private ArrayList<CollectionSet> getMinDFACollectionSets(){
		CollectionSet notTerminalCollectionSet = new CollectionSet(TransHelper.collectionSetNums++);
		for(int i=0; i<collectionStates.size(); i++){
			if(!collectionStates.get(i).getIsTerminalState()){
				notTerminalCollectionSet.addCollectionState(collectionStates.get(i));
			}
		}	
		CollectionSet terminalCollectionSet = new CollectionSet(TransHelper.collectionSetNums++);
		terminalCollectionSet.setCollectionStates(finalCollectionStates);
		ArrayList<CollectionSet> startCollectionSets = new ArrayList<CollectionSet>();
		startCollectionSets.add(notTerminalCollectionSet);
		startCollectionSets.add(terminalCollectionSet);
		
		ArrayList<Character> terminals = nfa.getTerminals();
		
		int[] setsNum = new int[terminals.size()];
		for(int i=0; i<setsNum.length; i++){
			setsNum[i] = 0;
		}
		
		while(true){
			for(int terminalCount=0; terminalCount<terminals.size(); terminalCount++){
				char terminal = terminals.get(terminalCount);
				ArrayList<CollectionSet> newCollectionSets = new ArrayList<CollectionSet>();
				for(int setCount=0; setCount<startCollectionSets.size(); setCount++){
					CollectionSet collectionSet = startCollectionSets.get(setCount);
					ArrayList<CollectionState> collectionStates = collectionSet.getCollectionStates();
					for(int stateInSetCount=0; stateInSetCount<collectionStates.size(); stateInSetCount++){
						CollectionState srcCollectionState = collectionStates.get(stateInSetCount);
						CollectionState desCollectionState = getNextState(srcCollectionState, terminal);
						
						// 判断是否有这样一条路
						if(desCollectionState == null){
							// 判断这样的一个SET是否已经存在，如果存在，则加入；
							// 如果不存在，新建一个，加入
							boolean alreadyHave = false;
							for(int newCollectionSetCount=0; newCollectionSetCount<newCollectionSets.size(); newCollectionSetCount++){
								CollectionSet currentCollectionSet = newCollectionSets.get(newCollectionSetCount);
								if(currentCollectionSet.alreadyHave(collectionSet.getSetKey(), -1)){
									currentCollectionSet.addCollectionState(srcCollectionState);
									alreadyHave = true;
									break;
								}
							}
							if(alreadyHave == false){
								CollectionSet newCollectionSet = new CollectionSet(TransHelper.collectionSetNums++,collectionSet.getSetKey(),-1);
								newCollectionSet.addCollectionState(srcCollectionState);
								newCollectionSets.add(newCollectionSet);
							}					
						}
						
						
						else{
							for(int inSet=0; inSet<startCollectionSets.size(); inSet++){
								CollectionSet currentStartCollectionSet = startCollectionSets.get(inSet);
								
								// 如果当前的set包含目的状态，进行相应处理
								if(currentStartCollectionSet.containsState(desCollectionState)){
									// 判断这样的一个SET是否已经存在，如果存在，则加入；
									// 如果不存在，新建一个，加入
									boolean alreadyHave = false;
									for(int newCollectionSetCount=0; newCollectionSetCount<newCollectionSets.size(); newCollectionSetCount++){
										CollectionSet currentCollectionSet = newCollectionSets.get(newCollectionSetCount);
										if(currentCollectionSet.alreadyHave(collectionSet.getSetKey(), currentStartCollectionSet.getSetKey())){
											currentCollectionSet.addCollectionState(srcCollectionState);
											alreadyHave = true;
											break;
										}
									}
									if(alreadyHave == false){
										CollectionSet newCollectionSet = new CollectionSet(TransHelper.collectionSetNums++,collectionSet.getSetKey(),currentStartCollectionSet.getSetKey());
										newCollectionSet.addCollectionState(srcCollectionState);
										newCollectionSets.add(newCollectionSet);
									}
									break;
								}
								
								
							}
						}
						
					}// 判断某个set中的某个状态的情况
				}// 判断每个set中的情况
				if(newCollectionSets.size() == setsNum[terminalCount]){
					return startCollectionSets;
				}else{
					setsNum[terminalCount] = newCollectionSets.size();
					startCollectionSets = newCollectionSets;
				}
			}// 判断每个终止符的情况
		}
		
	}
	
	/**
	 * 打印DFA，该方法用于写代码过程中转换DFA后的正确性检测
	public void printDFA(){
		System.out.println("下面是DFA中的内容：");
		System.out.println("开始状态："+startCollectionState.getCollectionStateNo());
		System.out.println("结束状态如下：");
		for(int i=0; i<finalCollectionStates.size(); i++){
			System.out.print(finalCollectionStates.get(i).getCollectionStateNo()+";");
		}
		System.out.println();
		System.out.println("所有状态如下：");
		for(int i=0; i<collectionStates.size(); i++){
			System.out.print(collectionStates.get(i).getCollectionStateNo()+";");
		}
		System.out.println();
		System.out.println("所有规则如下：");
		for(int i=0; i<collectionRules.size(); i++){
			CollectionRule collectionRule = collectionRules.get(i);
			System.out.print("{"+collectionRule.getCurrentCollectionState().getCollectionStateNo()+","+collectionRule.getKey()+","+collectionRule.getNextCollectionState().getCollectionStateNo()+"};");
		}
		System.out.println();
	}
	*/
	
	public CollectionState getStartCollectionState() {
		return startCollectionState;
	}

	public void setStartCollectionState(CollectionState startCollectionState) {
		this.startCollectionState = startCollectionState;
	}

	public ArrayList<CollectionState> getFinalCollectionStates() {
		return finalCollectionStates;
	}

	public void setFinalCollectionStates(
			ArrayList<CollectionState> finalCollectionStates) {
		this.finalCollectionStates = finalCollectionStates;
	}

	public ArrayList<CollectionState> getCollectionStates() {
		return collectionStates;
	}

	public void setCollectionStates(ArrayList<CollectionState> collectionStates) {
		this.collectionStates = collectionStates;
	}

	public ArrayList<CollectionRule> getCollectionRules() {
		return collectionRules;
	}

	public void setCollectionRules(ArrayList<CollectionRule> collectionRules) {
		this.collectionRules = collectionRules;
	}
	
	public String getTokenName(){
		return tokenName;
	}
	
	public void setTokenName(String tokeName){
		this.tokenName = tokeName;
	}
	
}
