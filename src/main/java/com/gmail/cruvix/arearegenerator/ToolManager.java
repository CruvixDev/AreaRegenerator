package com.gmail.cruvix.arearegenerator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public final class ToolManager implements Listener {
	private Material tool;
	private static ToolManager toolManager;
	private Coordinate point1;
	private Coordinate point2;

	public ToolManager() {
		this.tool = Material.DIAMOND_SHOVEL;
		this.point1 = new Coordinate();
		this.point2 = new Coordinate();
	}

	public static ToolManager getInstance() {
		if (toolManager == null) {
			toolManager = new ToolManager();
		}

		return toolManager;
	}

	public Material getTool() {
		return this.tool;
	}

	public void setTool(Material m) {
		if (Material.matchMaterial(m.toString()) != null) {
			this.tool = Material.matchMaterial(m.toString());
		}

	}

	public Coordinate getPoint1() {
		return this.point1;
	}

	public Coordinate getPoint2() {
		return this.point2;
	}

	public void setPoint1(Coordinate point1) {
		this.point1 = point1;
	}

	public void setPoint2(Coordinate point2) {
		this.point2 = point2;
	}

	@EventHandler
	public void onPlayerUseTool(PlayerInteractEvent e) {
		Block clickedBlock = e.getClickedBlock();
		Player player = e.getPlayer();
		if (e.getItem() != null && e.getItem().getType().equals(this.tool)) {
			if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				this.point1.setX(clickedBlock.getX());
				this.point1.setY(clickedBlock.getZ());
				player.sendMessage(ChatColor.AQUA + "First point seting at : " + this.point1.toString());
			} else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				this.point2.setX(clickedBlock.getX());
				this.point2.setY(clickedBlock.getZ());
				player.sendMessage(ChatColor.AQUA + "Second point seting at : " + this.point2.toString());
			}
			e.setCancelled(true);
		}
	}
}