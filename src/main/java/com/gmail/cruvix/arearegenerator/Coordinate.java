package com.gmail.cruvix.arearegenerator;

import java.io.Serializable;

/**
 * Represents a couple of coordinates and perform geometric operations.
 * @author Julien Cruvieux
 *
 */
public class Coordinate {
	private int x;
	private int y;
	private int z;
	
	public Coordinate(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Coordinate(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}

	public int getZ() {
		return this.z;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
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
		int z = point.getZ();
		int xmax = 0;
		int xmin = 0;
		int zmax = 0;
		int zmin = 0;

		if (point1.getX() >= point2.getX()){
			xmax = point1.getX();
			xmin = point2.getX();
		}
		else{
			xmax = point2.getX();
			xmin = point1.getX();
		}
		
		if (point1.getZ() >= point2.getZ()){
			zmax = point1.getZ();
			zmin = point2.getZ();
		}
		else{
			zmax = point2.getZ();
			zmin = point1.getZ();
		}
		
		if ((xmin <= x && x <= xmax) && (zmin <= z && z <= zmax)){
			isInSurface = true;
		}
		return isInSurface;
	}
	
	@Override
	public String toString(){
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj instanceof Coordinate){
			Coordinate coord = (Coordinate)obj;
			if (coord.getX() == this.x && coord.getY() == this.y && coord.getZ() == this.z){
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