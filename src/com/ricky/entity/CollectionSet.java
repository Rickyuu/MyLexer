package com.ricky.entity;
import java.util.ArrayList;

/**
 * 	  �ȼ�״̬�Ļ��ּ���
 *   @author Ricky Liang   
 *   @time   2012-11-7 ����9:11:50    
 */
public class CollectionSet {
	/**
	 * ��ǰ���ּ��ϱ��
	 * ĳ״̬���ϴεĻ��������ڼ��ϱ��
	 * ĳ״̬����ĳ���ս��������״̬���ϴλ��������ڼ��ϱ��
	 * ��������������DFA״̬
	 * ���ϵĴ���״̬
	 */
	private int setKey;
	private int srcKey;
	private int desKey;
	ArrayList<CollectionState> collectionStates;
	private CollectionState representCollectionState;
	
	public CollectionSet(int setKey, int srcKey, int desKey){
		this.setKey = setKey;
		this.srcKey = srcKey;
		this.desKey = desKey;
		this.representCollectionState = null;
		collectionStates = new ArrayList<CollectionState>();
	}
	
	public CollectionSet(int setKey){
		this.setKey = setKey;
		collectionStates = new ArrayList<CollectionState>();
	}
	
	/**
	 * �жϸü����Ƿ����ĳDFA״̬
	 * @param collectionState
	 * @return
	 */
	public boolean containsState(CollectionState collectionState){
		if(collectionStates.contains(collectionState)){
			return true;
		}
		return false;
	}
	
	/**
	 * �ж��Ƿ��Ѿ�������Ӧ��ת��
	 * @param srcKey
	 * @param desKey
	 * @return
	 */
	public boolean alreadyHave(int srcKey, int desKey){
		if(this.srcKey == srcKey && this.desKey == desKey)
			return true;
		return false;
	}
	
	/**
	 * ���Ӽ����е�״̬
	 * @param collectionState
	 */
	public void addCollectionState(CollectionState collectionState){
		collectionStates.add(collectionState);
	}
	
	public int getSetKey(){
		return setKey;
	}
	
	public void setSetKey(int setKey){
		this.setKey = setKey;
	}
	
	public int getSrcKey() {
		return srcKey;
	}

	public void setSrcKey(int srcKey) {
		this.srcKey = srcKey;
	}

	public int getDesKey() {
		return desKey;
	}

	public void setDesKey(int desKey) {
		this.desKey = desKey;
	}
		
	public ArrayList<CollectionState> getCollectionStates() {
		return collectionStates;
	}

	public void setCollectionStates(
			ArrayList<CollectionState> collectionStates) {
		this.collectionStates = collectionStates;
	}
	
	public CollectionState getRepresentCollectionState(){
		return representCollectionState;
	}
	
	public void setRepresentCollectionState(CollectionState representCollectionState){
		this.representCollectionState = representCollectionState;
	}
	
}
