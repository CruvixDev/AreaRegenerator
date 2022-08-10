package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import org.bukkit.Material;

/**
 * Class encapsulating the information of an area, like opposites coordinates, the name of the area
 * and the material that we can place in the area.
 * @author Julien Cruvieux
 */
public class AreaInformation {
	private String worldName;
	private String areaName;
	private Coordinate point1;
	private Coordinate point2;
	private ArrayList<Material> placeableMaterials;
	private ArrayList<Material> nonExplosiveMaterials;
	private EventHandler eventHandler;

	/**
	 * Public constructor to encapsulate the name and the opposites coordinates of the area.
	 * @param areaName the name of the area.
	 * @param point1 the first point of the area.
	 * @param point2 the second point of the area.
	 */
	public AreaInformation(String areaName, String worldName, Coordinate point1, Coordinate point2) {
		this.areaName = areaName;
		this.worldName = worldName;
		this.point1 = point1;
		this.point2 = point2;
		this.placeableMaterials = new ArrayList();
		this.nonExplosiveMaterials = new ArrayList();
		this.eventHandler = new EventHandler(this);
	}

	/**
	 * Add new placeable materials in the existing list.
	 * @param materialsList the list of materials to add.
	 */
	public void addPlaceableMaterials(ArrayList<Material> materialsList) {
		for (Material m : materialsList) {
			if (!this.placeableMaterials.contains(m)) {
				this.placeableMaterials.add(m);
			}
		}
	}

	public void addPlaceableMaterial(Material material) {
		this.placeableMaterials.add(material);
	}

	/**
	 * Clear the list of placeable materials.
	 */
	public void clearPlaceableMaterials() {
		this.placeableMaterials.clear();
	}

	/**
	 * Clear specified placeable materials in the list.
	 * @param materialsList the list of materials to remove.
	 */
	public void clearPlaceableMaterials(ArrayList<Material> materialsList) {
		this.placeableMaterials.removeAll(materialsList);
	}

	public void clearPlaceableMaterial(Material material) {
		this.placeableMaterials.remove(material);
	}

	/**
	 * Add new non exploseable materials in the existing list.
	 * @param materialsList the list of materials to add.
	 */
	public void addNonExplosiveMaterials(ArrayList<Material> materialsList) {
		for (Material m : materialsList) {
			if (!this.nonExplosiveMaterials.contains(m)) {
				this.nonExplosiveMaterials.add(m);
			}
		}
	}

	public void addNonExplosiveMaterial(Material material) {
		this.nonExplosiveMaterials.add(material);
	}

	/**
	 * Clear the list of non exploseable materials.
	 */
	public void clearNonExplosiveMaterials() {
		this.nonExplosiveMaterials.clear();
	}

	/**
	 * Clear specified non exploseable materials in the list.
	 * @param materialsList the list of materials to remove.
	 */
	public void clearNonExplosiveMaterials(ArrayList<Material> materialsList) {
		this.nonExplosiveMaterials.removeAll(materialsList);
	}

	public void clearNonExplosiveMaterial(Material material) {
		this.nonExplosiveMaterials.remove(material);
	}

	/**
	 * Show the materials present in the placeable blocks list.
	 * @return the String representation of the materials in the list.
	 */
	public String showMaterials() {
		return this.placeableMaterials.toString();
	}

	/**
	 * Show the materials present in the non exploseable blocks list.
	 * @return the String representation of the materials in the list.
	 */
	public String showNonExplosiveBlocks() {
		return this.nonExplosiveMaterials.toString();
	}

	public String getAreaName() {
		return this.areaName;
	}

	public String getWorldName() {
		return this.worldName;
	}

	public Coordinate getPoint1() {
		return this.point1;
	}

	public Coordinate getPoint2() {
		return this.point2;
	}

	public ArrayList<Material> getPlaceableBlocks() {
		return this.placeableMaterials;
	}

	public ArrayList<Material> getNonExplosiveBlocks() {
		return this.nonExplosiveMaterials;
	}

	public void setPlaceableBlocks(ArrayList<Material> materials) {
		this.placeableMaterials = materials;
	}

	public void setNonExplosiveBlocks(ArrayList<Material> materials) {
		this.nonExplosiveMaterials = materials;
	}

	/**
	 * Each AreaInformations contains a specific EventHandler to handle the different event
	 * which are involved in an area.
	 * @return
	 */
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}

	public void setEventHandler() {
		this.eventHandler = new EventHandler(this);
	}
}