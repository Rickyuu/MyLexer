package com.ricky.entity;
import java.util.ArrayList;

/**
 * 	  等价状态的划分集合
 *   @author Ricky Liang   
 *   @time   2012-11-7 下午9:11:50    
 */
public class CollectionSet {
	/**
	 * 当前划分集合编号
	 * 某状态在上次的划分中所在集合编号
	 * 某状态经过某非终结符后所得状态在上次划分中所在集合编号
	 * 集合中所包含的DFA状态
	 * 集合的代表状态
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
	 * 判断该集合是否包含某DFA状态
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
	 * 判断是否已经包含相应的转化
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
	 * 增加集合中的状态
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
