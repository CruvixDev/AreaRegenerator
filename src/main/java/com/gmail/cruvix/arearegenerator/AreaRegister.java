package com.gmail.cruvix.arearegenerator;

import org.bukkit.Bukkit;
import org.json.simple.parser.ParseException;
import java.io.Reader;
import org.json.simple.parser.JSONParser;
import org.bukkit.Material;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.ArrayList;
import java.io.File;

/**
 * Class which allows to register an AreaInfomations and write and save him into a file.
 * @author Julien Cruvieux
 */
public final class AreaRegister {
	private File registeredAreasJSON;
	private ArrayList<AreaInformation> areasList;
	private FileWriter fileWriter;
	private FileReader fileReader;
	private static AreaRegister areaRegister = null;

	/**
	 * Private constructor which initialize the file (if it does not exist) and the list
	 * of AreaInformations.
	 */
	private AreaRegister() {
		String path = Bukkit.getPluginManager().getPlugin("AreaRegenerator").getDataFolder().getAbsolutePath() + "/web";
		registeredAreasJSON = new File(path + "/registeredAreas.json");
		try {
			if (!this.registeredAreasJSON.exists()) {
				this.registeredAreasJSON.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.areasList = new ArrayList();
	}

	/**
	 * Get the unique instance of the AreaRegister.
	 * @return the unique instance of this class.
	 */
	public static AreaRegister getInstance() {
		if (areaRegister == null) {
			areaRegister = new AreaRegister();
		}
		return areaRegister;
	}

	/**
	 * Register a new AreaInformations.
	 * @param areaInformation
	 */
	public void addAreaInformations(AreaInformation areaInformation) {
		this.areasList.add(areaInformation);
	}

	/**
	 * Unregister the specified AreaInformations.
	 * @param areaInformation the AreaInformations to unregister.
	 * @return true if the specified AreaInformations is unregistered, false otherwise
	 */
	public boolean removeAreaInformations(AreaInformation areaInformation) {
		return this.areasList.remove(areaInformation);
	}

	/**
	 * Verify that the specified AreaInformations is registered.
	 * @param areaName the name of an AreaInformations.
	 * @return true if it exists, false otherwise.
	 */
	public boolean containsAreaName(String areaName) {
		for (final AreaInformation ai : this.areasList) {
			if (ai.getAreaName().equals(areaName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify that two areas are not riding.
	 * @param point1 the first point of the area.
	 * @param point2 the second point of the area.
	 * @return true if the two points are not in another area registered, false otherwise.
	 */
	public boolean isRiding(Coordinate point1, Coordinate point2) {
		for (AreaInformation ai : areasList){
			if (Coordinate.isInSurface(point1,ai.getPoint1(),ai.getPoint2()) || Coordinate.isInSurface(point2,ai.getPoint1(),ai.getPoint2())){
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the AreaInformations according to a given point.
	 * @param point a specific point.
	 * @return the AreaInformations concerned by this point (the point is in this Area), null otherwise.
	 */
	public AreaInformation isInArea(Coordinate point) {
		for (final AreaInformation ai : this.areasList) {
			if (Coordinate.isInSurface(point, ai.getPoint1(), ai.getPoint2())) {
				return ai;
			}
		}
		return null;
	}

	/**
	 * Save the file where the AreaInformation are written, but in JSON format.
	 * @return true if the save is correctly done false otherwise.
	 */
	public boolean saveAreaInformationJSON() {
		final JSONArray areaRegister = new JSONArray();
		for (final AreaInformation ai : this.areasList) {
			final JSONObject area = new JSONObject();
			final JSONObject areaInformationJSON = new JSONObject();
			final JSONObject point1 = new JSONObject();
			final JSONObject point2 = new JSONObject();
			final JSONArray placeableBlocksList = new JSONArray();
			final JSONArray nonExplosiveBlocksList = new JSONArray();
			areaInformationJSON.put("areaName",ai.getAreaName());
			areaInformationJSON.put("worldName",ai.getWorldName());
			point1.put("x",ai.getPoint1().getX());
			point1.put("y",ai.getPoint1().getY());
			point2.put("x",ai.getPoint2().getX());
			point2.put("y",ai.getPoint2().getY());
			areaInformationJSON.put("point1",point1);
			areaInformationJSON.put("point2",point2);
			for (final Material m : ai.getPlaceableBlocks()) {
				placeableBlocksList.add(m.toString());
			}
			for (final Material m : ai.getNonExplosiveBlocks()) {
				nonExplosiveBlocksList.add(m.toString());
			}
			areaInformationJSON.put("materialsList",placeableBlocksList);
			areaInformationJSON.put("nonExplosiveBlocksList",nonExplosiveBlocksList);
			area.put("areaInformation",areaInformationJSON);
			areaRegister.add(area);
		}
		try {
			this.fileWriter = new FileWriter(this.registeredAreasJSON);
			fileWriter.write(areaRegister.toJSONString());
			this.fileWriter.flush();
			this.fileWriter.close();
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read the file and put all the AreaInformation in a list.
	 * @return true if the reading is successfully completed.
	 */
	public boolean readAreaInformationJSON() {
		try {
			this.fileReader = new FileReader(this.registeredAreasJSON);
			final JSONParser jsonParser = new JSONParser();
			final Object jsonFile = jsonParser.parse((Reader)this.fileReader);
			this.fileReader.close();
			final JSONArray areaRegister = (JSONArray)jsonFile;
			for (final Object o : areaRegister) {
				final JSONObject areaInformationJSON = (JSONObject)((JSONObject)o).get("areaInformation");
				final String areaName = (String)areaInformationJSON.get("areaName");
				final String worldName = (String)areaInformationJSON.get("worldName");
				final long point1X = (long)((JSONObject)areaInformationJSON.get("point1")).get("x");
				final long point1Y = (long)((JSONObject)areaInformationJSON.get("point1")).get("y");
				final long point2X = (long)((JSONObject)areaInformationJSON.get("point2")).get("x");
				final long point2Y = (long)((JSONObject)areaInformationJSON.get("point2")).get("y");
				final Coordinate point1 = new Coordinate((int)point1X, (int)point1Y);
				final Coordinate point2 = new Coordinate((int)point2X, (int)point2Y);
				final JSONArray placeableBlocksListJSON = (JSONArray)areaInformationJSON.get("materialsList");
				final ArrayList<Material> placeableBlocksList = new ArrayList<Material>();
				final JSONArray nonExplosiveBlocksListJSON = (JSONArray)areaInformationJSON.get("nonExplosiveBlocksList");
				final ArrayList<Material> nonExplosiveBlocksList = new ArrayList<Material>();
				if (nonExplosiveBlocksListJSON != null) {
					for (final Object obj : nonExplosiveBlocksListJSON) {
						final Material material = MaterialsTranslator.translateIntoMaterial((String)obj);
						nonExplosiveBlocksList.add(material);
					}
				}
				for (final Object obj : placeableBlocksListJSON) {
					final Material material = MaterialsTranslator.translateIntoMaterial((String)obj);
					placeableBlocksList.add(material);
				}
				final AreaInformation areaInformation = new AreaInformation(areaName, worldName, point1, point2);
				areaInformation.setEventHandler();
				areaInformation.setPlaceableBlocks((ArrayList)placeableBlocksList);
				areaInformation.setNonExplosiveBlocks((ArrayList)nonExplosiveBlocksList);
				this.areasList.add(areaInformation);
			}
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		String toString = "";
		for (AreaInformation ai : areasList){
			toString += " [" + ai.getAreaName() + " at " + ai.getPoint1() + " and " + ai.getPoint2() + "]";
		}
		return toString;
	}
}