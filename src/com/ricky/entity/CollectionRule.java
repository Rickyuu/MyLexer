package com.ricky.entity;
/**
 *   描述DFA中的转化规则类
 *   @author Ricky Liang   
 *   @time   2012-11-5 下午10:52:23 
 */
public class CollectionRule {
	/**
	 * 当前状态
	 * 接受的终结符
	 * 下一个状态
	 */
	private CollectionState currentCollectionState;
	private char key;
	private CollectionState nextCollectionState;
	
	public CollectionRule(CollectionState currentCollectionState, char key, CollectionState nextCollectionState){
		this.currentCollectionState = currentCollectionState;
		this.key = key;
		this.nextCollectionState = nextCollectionState;
	}
	
	public CollectionState getCurrentCollectionState() {
		return currentCollectionState;
	}

	public void setCurrentCollectionState(CollectionState currentCollectionState) {
		this.currentCollectionState = currentCollectionState;
	}

	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public CollectionState getNextCollectionState() {
		return nextCollectionState;
	}

	public void setNextCollectionState(CollectionState nextCollectionState) {
		this.nextCollectionState = nextCollectionState;
	}
	
}
