package com.gmail.cruvix.arearegenerator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Bed.Part;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

/**
 * Class handling events for areas.
 * @author Julien Cruvieux
 */
public class EventHandler implements Listener {
	private transient Hashtable<Block, BlockData> placedBlocks;
	private transient ArrayList<Location> placedBlocksLocation;
	private AreaInformation areaInformation;
	private static transient BlockFace[] adjacentBlocksOrientation;

	static {
		adjacentBlocksOrientation = new BlockFace[]{BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST};
	}

	public EventHandler(AreaInformation areaInformation) {
		this.areaInformation = areaInformation;
		this.placedBlocks = new Hashtable();
		this.placedBlocksLocation = new ArrayList();
	}

	public void clearPlacedBlocks(){
		for (Map.Entry<Block,BlockData> me : placedBlocks.entrySet()){
			Block block = me.getKey();
			Material material = me.getValue().getMaterial();
			block.setType(material);
			block.setBlockData(me.getValue());

			if (me.getValue() instanceof Bisected) {
				Bisected bisected = (Bisected) me.getValue();
				if (bisected.getHalf() == Half.BOTTOM) {
					bisected.setHalf(Half.TOP);
					block = block.getRelative(BlockFace.UP);
				}
				else {
					bisected.setHalf(Half.BOTTOM);
					block = block.getRelative(BlockFace.DOWN);
				}
				block.setBlockData(bisected);
			}
			if(me.getValue() instanceof Bed) {
				Bed bed = (Bed)me.getValue();
				Block blockFacing = block.getRelative(bed.getFacing());
				if (bed.getPart() == Part.FOOT) {
					bed.setPart(Part.HEAD);
				}
				else {
					bed.setPart(Part.FOOT);
					blockFacing = block.getRelative(bed.getFacing().getOppositeFace());
				}
				blockFacing.setBlockData(bed);
			}
		}
		placedBlocks.clear();
		placedBlocksLocation.clear();
	}

	@org.bukkit.event.EventHandler
	public void onBlockPlacedEvent(BlockPlaceEvent e) {
		Block blockInvolves = e.getBlock();
		Coordinate blockCoordinate = new Coordinate(blockInvolves.getX(), blockInvolves.getY(), blockInvolves.getZ());
		Coordinate point1 = this.areaInformation.getPoint1();
		Coordinate point2 = this.areaInformation.getPoint2();
		if (Coordinate.isInSurface(blockCoordinate, point1, point2)) {
			if (this.areaInformation.getPlaceableBlocks().contains(blockInvolves.getType())) {
				if (!this.placedBlocksLocation.contains(blockInvolves.getLocation()) || this.placedBlocks.size() == 0) {
					this.placedBlocks.put(e.getBlockPlaced(), e.getBlockReplacedState().getBlockData());
					this.placedBlocksLocation.add(blockInvolves.getLocation());
				}
			} else {
				e.setCancelled(true);
			}
		}
	}

	@org.bukkit.event.EventHandler
	public void onBlockBrokenEvent(BlockBreakEvent e) {
		Block blockInvolves = e.getBlock();
		Coordinate blockCoordinate = new Coordinate(blockInvolves.getX(), blockInvolves.getY(), blockInvolves.getZ());
		Coordinate point1 = this.areaInformation.getPoint1();
		Coordinate point2 = this.areaInformation.getPoint2();
		if (Coordinate.isInSurface(blockCoordinate, point1, point2)) {
			if (!this.areaInformation.getPlaceableBlocks().contains(blockInvolves.getType())) {
				e.setCancelled(true);
			} else if (!this.placedBlocksLocation.contains(blockInvolves.getLocation()) || this.placedBlocks.size() == 0) {
				this.placedBlocks.put(blockInvolves, blockInvolves.getBlockData());
				this.placedBlocksLocation.add(blockInvolves.getLocation());
			}
		}

	}

	@org.bukkit.event.EventHandler
	public void onBucketPlacedEvent(PlayerBucketEmptyEvent e) {
		Block blockInvolves = e.getBlock();
		Coordinate blockCoordinate = new Coordinate(blockInvolves.getX(), blockInvolves.getY(), blockInvolves.getZ());
		Coordinate point1 = this.areaInformation.getPoint1();
		Coordinate point2 = this.areaInformation.getPoint2();
		if (Coordinate.isInSurface(blockCoordinate, point1, point2)) {
			if ((this.areaInformation.getPlaceableBlocks().contains(Material.WATER) || this.areaInformation.getPlaceableBlocks().contains(Material.LAVA)) && !blockInvolves.getType().equals(Material.WATER) && !this.adjacentBlockInWater(blockInvolves)) {
				if (!this.placedBlocksLocation.contains(blockInvolves.getLocation()) || this.placedBlocks.size() == 0) {
					this.placedBlocks.put(blockInvolves, blockInvolves.getBlockData());
					this.placedBlocksLocation.add(blockInvolves.getLocation());
				}
			} else {
				e.setCancelled(true);
			}
		}

	}

	@org.bukkit.event.EventHandler
	public void waterBrokenBlocksEvent(BlockFromToEvent e) {
		Block blockInvolves = e.getToBlock();
		if (!this.placedBlocksLocation.contains(blockInvolves.getLocation()) || this.placedBlocks.size() == 0) {
			this.placedBlocks.put(blockInvolves, blockInvolves.getBlockData());
			this.placedBlocksLocation.add(blockInvolves.getLocation());
		}

	}

	@org.bukkit.event.EventHandler
	public void blockExploded(EntityExplodeEvent e) {
		Coordinate point1 = this.areaInformation.getPoint1();
		Coordinate point2 = this.areaInformation.getPoint2();
		for (Block block : new ArrayList<Block>(e.blockList())) {
			Coordinate coordinate = new Coordinate(block.getX(), block.getY(), block.getZ());
			if (Coordinate.isInSurface(coordinate, point1, point2) && (!this.areaInformation.getPlaceableBlocks().contains(block.getType()) || this.areaInformation.getNonExplosiveBlocks().contains(block.getType()) || !this.placedBlocksLocation.contains(block.getLocation()))) {
				e.blockList().remove(block);
			}
		}
	}

	@org.bukkit.event.EventHandler
	public void notifyBlockSet(BlockSetEvent e) {
		Block block = e.getBlockSetLocation().getBlock();
		this.placedBlocks.put(block, block.getBlockData());
		this.placedBlocksLocation.add(e.getBlockSetLocation());
	}

	/*@org.bukkit.event.EventHandler
	public void onPlayerHeldTool(PlayerItemHeldEvent e) {
		Player player = e.getPlayer();
		Coordinate playerLocation = new Coordinate(player.getLocation().getBlockX(),player.getLocation().getBlockZ());
		Coordinate point1 = this.areaInformation.getPoint1();
		Coordinate point2 = this.areaInformation.getPoint2();

		if (player.getInventory().getItem(e.getNewSlot()) == null) {
			return;
		}
		if (player.getInventory().getItem(e.getNewSlot()).getType() == ToolManager.getInstance().getTool() && Coordinate.isInSurface(playerLocation,point1,point2)) {
			Location location1 = player.getLocation();
			Location location2 = player.getLocation();

			location1.setX(point1.getX());
			location1.setZ(point1.getY());

			location2.setX(point2.getX());
			location2.setZ(point2.getY());

			player.sendBlockChange(location1,Material.GOLD_BLOCK.createBlockData());
			player.sendBlockChange(location2,Material.GOLD_BLOCK.createBlockData());
		}
	}*/

	/*@org.bukkit.event.EventHandler
	public void onEntityExplode(ExplosionPrimeEvent e) {
		int radius = (int) e.getRadius();
		Location center  = e.getEntity().getLocation();
		ArrayList<Block> blockArrayList = new ArrayList<>();

		for (int i = center.getBlockX() - radius; i <= center.getBlockX() + radius; i++) {
			for (int j = center.getBlockY() - radius; j <= center.getBlockY() + radius; j++) {
				for (int k = center.getBlockZ() - radius; k <= center.getBlockZ() + radius; k++) {
					if (norm(i,j,k,center.getBlockX(),center.getBlockY(),center.getBlockZ()) <= radius) {
						blockArrayList.add(e.getEntity().getWorld().getBlockAt(i,j,k));
					}
				}
			}
		}

		for (Block block : blockArrayList) {
			if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				block.setType(Material.AIR);
			}
		}
	}*/

	public boolean adjacentBlockInWater(Block block) {
		for(BlockFace bf : adjacentBlocksOrientation) {
			if (block.getRelative(bf).getType().equals(Material.WATER) || block.getRelative(bf,2).getType().equals(Material.WATER)) {
				return true;
			}
		}
		return false;
	}

	/*public int norm(int x, int y, int z, int xc, int yc, int zc) {
		return (int) (Math.pow(x-xc,2) + Math.pow(y-yc,2) + Math.pow(z-zc,2));
	}*/
}