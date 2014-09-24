package com.ricky.entity;
/**
 *   ����NFA�е�ת������
 *   @author Ricky Liang   
 *   @time   2012-11-5 ����7:08:36   
 */
public class Rule{
	/**
	 * ��ǰNFA״̬
	 * �ս��
	 * ��һ��NFA״̬
	 */
	private State currentState;
    private char key;
    private State nextState;
   
    public Rule(State currentState,char key,State nextState){
    	this.currentState = currentState;
	    this.key = key;
	    this.nextState = nextState;
    } 
    
    public State getCurrentState(){
    	return currentState;
    } 
    public char getKey(){
	    return key;
    }
    public State getNextState(){
	    return nextState;
    }
    public void setCurrentState(State currentState){
    	this.currentState = currentState;
    }
    public void setKey(char key){
	    this.key = key;
    }
    public void setNextState(State nextState){
	    this.nextState = nextState;
    }
}  


