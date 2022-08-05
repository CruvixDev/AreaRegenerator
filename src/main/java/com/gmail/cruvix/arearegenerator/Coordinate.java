package com.gmail.cruvix.arearegenerator;

import java.io.Serializable;

/**
 * Represents a couple of coordinates and perform geometric operations.
 * @author Julien Cruvieux
 *
 */
public class Coordinate implements Serializable{

	private static final long serialVersionUID = 8969253717320431963L;
	private int x;
	private int y;
	
	public Coordinate(){
		this.x = 0;
		this.y = 0;
	}
	
	public Coordinate(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * Verify if a point is in the surface defined by two other points.
	 * @param point the point to verify.
	 * @param point1 the first point defining the surface.
	 * @param point2 the second point defining the surface.
	 * @return true if point is the surface defined by point1 and point2, false otherwise.
	 */
	public static boolean isInSurface(Coordinate point, Coordinate point1, Coordinate point2){
		boolean isInSurface = false;
		
		int x = point.getX();
		int y = point.getY();
		int xmax = 0;
		int xmin = 0;
		int ymax = 0;
		int ymin = 0;

		if (point1.getX() >= point2.getX()){
			xmax = point1.getX();
			xmin = point2.getX();
		}
		else{
			xmax = point2.getX();
			xmin = point1.getX();
		}
		
		if (point1.getY() >= point2.getY()){
			ymax = point1.getY();
			ymin = point2.getY();
		}
		else{
			ymax = point2.getY();
			ymin = point1.getY();
		}
		
		if ((xmin <= x && x <= xmax) && (ymin <= y && y <= ymax)){
			isInSurface = true;
		}
		return isInSurface;
	}
	
	@Override
	public String toString(){
		return "(" + this.x + ", " + this.y + ")";
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj instanceof Coordinate){
			Coordinate coord = (Coordinate)obj;
			if (coord.getX() == this.x && coord.getY() == this.y){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
}