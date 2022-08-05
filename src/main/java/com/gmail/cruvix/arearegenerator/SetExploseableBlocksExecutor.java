package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetExploseableBlocksExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        if (arg0.isOp()) {
            AreaInformation areaInformation = AreaVerificator.verifyAreas(arg0);
            if (areaInformation != null) {
                new ArrayList();
                ArrayList<Material> materialsList = MaterialsTranslator.translateIntoMaterials(arg3);
                if (materialsList != null) {
                    areaInformation.addNonExplosiveMaterials(materialsList);
                } else {
                    arg0.sendMessage(ChatColor.RED + "Materials list not valid!");
                }

                AreaRegister.getInstance().saveAreaInformationJSON();
            } else {
                arg0.sendMessage(ChatColor.RED + "You are not in a registered Area!");
            }
        } else {
            arg0.sendMessage(ChatColor.RED + "You are not allowed to perform this command!");
        }

        return false;
    }
}