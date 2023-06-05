package TPEntrega1;

import java.util.*;

public abstract class NodoHuff implements Comparable<NodoHuff> {
	
	private NodoHuff hijo1= null;
	private NodoHuff hijo2= null;
	private float p=0;
	private int color=0;
	
	public int compareTo(NodoHuff nodo) {
        int  comp= Float.compare(this.p, nodo.p);
        return comp;
    }
	
	public void setHijo1(NodoHuff nodo){
		hijo1=nodo;
	}
	
	public void setHijo2(NodoHuff nodo){
		hijo2=nodo;
	}
	
	public void setP(float p){
		this.p=p;
	}
	
	public void setColor(int color){
		this.color=color;
	}
	
	public float getP(){
		return p;
	}
	
	public int getColor(){
		return color;
	}
	
	public NodoHuff getHijo1(){
		return hijo1;
	}
	
	public NodoHuff getHijo2(){
		return hijo2;
	}
}
