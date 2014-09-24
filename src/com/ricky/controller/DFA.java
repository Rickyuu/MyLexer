package com.ricky.controller;
import java.util.ArrayList;

import com.ricky.entity.CollectionRule;
import com.ricky.entity.CollectionSet;
import com.ricky.entity.CollectionState;
import com.ricky.entity.State;


/**
 *   ����DFA����
 *   @author Ricky Liang   
 *   @time   2012-11-5 ����10:50:23   
 */
public class DFA {
	/**
	 * ���ɸ�DFA��NFA
	 * ��ʼDFA״̬
	 * ��ֹDFA״̬
	 * ����DFA״̬
	 * ���е�DFAת������
	 * ��DFA����Ӧ��Token������
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
	 * ���DFA�Ŀ�ʼ״̬
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
	 * �ҳ���һ��DFA״̬�������ս���������DFA״̬������DFA�������DFA
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
	 * �ж�ĳ״̬�Ƿ�Ϊ��ֹ״̬
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
	 * �ж�ĳ��DFA״̬�Ƿ��Ѿ�����
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
	 * �жϸ�DFA�Ƿ����һ�����봮
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
	 * ��һ��DFA״̬�������ս���������һ��DFA״̬������ӹ���
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
	 * �����С����DFA
	 * @return
	 */
	public DFA getMinDFA(){
		/*
		 * �ҳ��ȼ��໮�ֺ������״̬
		 * �ҳ��ȼ��໮�ֺ�����ù���
		 * �½�һ��DFA
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
	 * ��õȼ��໮�ֺ�����õĹ���
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
	 * ��õȼ��໮�ֺ�����õ�״̬
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
	 * �ȼ��໮��
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
						
						// �ж��Ƿ�������һ��·
						if(desCollectionState == null){
							// �ж�������һ��SET�Ƿ��Ѿ����ڣ�������ڣ�����룻
							// ��������ڣ��½�һ��������
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
								
								// �����ǰ��set����Ŀ��״̬��������Ӧ����
								if(currentStartCollectionSet.containsState(desCollectionState)){
									// �ж�������һ��SET�Ƿ��Ѿ����ڣ�������ڣ�����룻
									// ��������ڣ��½�һ��������
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
						
					}// �ж�ĳ��set�е�ĳ��״̬�����
				}// �ж�ÿ��set�е����
				if(newCollectionSets.size() == setsNum[terminalCount]){
					return startCollectionSets;
				}else{
					setsNum[terminalCount] = newCollectionSets.size();
					startCollectionSets = newCollectionSets;
				}
			}// �ж�ÿ����ֹ�������
		}
		
	}
	
	/**
	 * ��ӡDFA���÷�������д���������ת��DFA�����ȷ�Լ��
	public void printDFA(){
		System.out.println("������DFA�е����ݣ�");
		System.out.println("��ʼ״̬��"+startCollectionState.getCollectionStateNo());
		System.out.println("����״̬���£�");
		for(int i=0; i<finalCollectionStates.size(); i++){
			System.out.print(finalCollectionStates.get(i).getCollectionStateNo()+";");
		}
		System.out.println();
		System.out.println("����״̬���£�");
		for(int i=0; i<collectionStates.size(); i++){
			System.out.print(collectionStates.get(i).getCollectionStateNo()+";");
		}
		System.out.println();
		System.out.println("���й������£�");
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
