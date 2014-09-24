package com.ricky.entity;
import java.util.ArrayList;

/**
 *   ����DFA��״̬����
 *   @author Ricky Liang   
 *   @time   2012-11-5 ����10:43:23   
 */
public class CollectionState {
	/**
	 * ����NFA�е�״̬����
	 * DFA״̬���
	 * �Ƿ�����ֹ״̬
	 */
	private ArrayList<State> statesInCollection;
	private int collectionStateNo;
	private boolean isTerminalState;
	
	public CollectionState(int collectionStateNo, ArrayList<State> statesInCollection){
		this.collectionStateNo = collectionStateNo;
		this.statesInCollection = statesInCollection;
		this.isTerminalState = false;
	}
	
	/**
	 * �ж�״̬NFA�����Ƿ����
	 * @param states
	 * @return
	 */
	public boolean isEqual(ArrayList<State> states){
		if(statesInCollection.size() != states.size()){
			return false;
		}
		for(int i=0; i<states.size(); i++){
			if(!statesInCollection.contains(states.get(i))){
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<State> getStatesInCollection() {
		return statesInCollection;
	}

	public void setStatesInCollection(ArrayList<State> statesInCollection) {
		this.statesInCollection = statesInCollection;
	}

	public int getCollectionStateNo() {
		return collectionStateNo;
	}

	public void setCollectionStateNo(int collectionStateNo) {
		this.collectionStateNo = collectionStateNo;
	}	
	
	public boolean getIsTerminalState(){
		return isTerminalState;
	}
	
	public void setIsTerminalState(boolean isTerminalState){
		this.isTerminalState = isTerminalState;
	}
	
}
