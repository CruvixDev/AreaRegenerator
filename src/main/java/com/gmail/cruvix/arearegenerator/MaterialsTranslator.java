package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import org.bukkit.Material;

public class MaterialsTranslator {
	public MaterialsTranslator() {
	}

	private static boolean isMaterialsValid(String[] materialsList) {
		for (String s : materialsList){
			if (Material.matchMaterial(s) == null){
				return false;
			}
		}
		return true;
	}

	public static ArrayList<Material> translateIntoMaterials(String[] materialsList) {
		ArrayList<Material> materials = new ArrayList();
		if (!isMaterialsValid(materialsList)) {
			return null;
		} else {
			for(String s : materialsList) {
				materials.add(Material.matchMaterial(s));
			}
			return materials;
		}
	}

	public static Material translateIntoMaterial(String materialName) {
		Material material = Material.matchMaterial(materialName);
		return material != null ? material : null;
	}
}