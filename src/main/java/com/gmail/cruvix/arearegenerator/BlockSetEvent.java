package com.gmail.cruvix.arearegenerator;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockSetEvent extends Event implements Cancellable {
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;
	private Location blockSetLocation;

	public BlockSetEvent() {
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public void setBlockSetLocation(Location blockSetLocation) {
		this.blockSetLocation = blockSetLocation;
	}

	public Location getBlockSetLocation() {
		return this.blockSetLocation;
	}
}