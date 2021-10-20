package com.josemarcellio.playercosmetics.listeners;

import com.josemarcellio.playercosmetics.manager.PlayerCosmeticsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Listens to RightClick events and equips cosmetic if player is holding one.
 *
 */

public class ClickCosmetics implements Listener {
   private final PlayerCosmeticsManager equipper;
   
   public ClickCosmetics() {
      equipper = new PlayerCosmeticsManager();
   }
   
   //@EventHandler
   //public void onRightClick(PlayerInteractEvent e) {
    //  if(e.getItem() != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
     //    if(PlayerCosmeticsUtils.isCosmetic(e.getItem())) {
      //      equipper.equipCosmetic(e.getPlayer(),e.getItem(),e.getHand());
       //  }
      //}
   //}
   @EventHandler
   public void onInventoryClick(InventoryClickEvent e) {
      Inventory inv = e.getClickedInventory();

      if(e.getSlotType() == InventoryType.SlotType.ARMOR && inv != null && inv.getType() == InventoryType.PLAYER) { //only apply to armor slots
         equipper.equipCosmeticFromSlotClick(e.getWhoClicked(), e.getClickedInventory(), e.getCurrentItem(), e.getCursor(), e.getSlot());
      }
     // if(e.isShiftClick() && inv != null && inv.getType() == InventoryType.PLAYER && e.getInventory().getType() == InventoryType.CRAFTING) {
       //  if(e.getSlotType() != InventoryType.SlotType.ARMOR) {
         //   equipper.equipCosmeticFromShiftClick(e.getCurrentItem(), e.getClickedInventory(), e.getSlot());
         //}
      //}

      //https://hub.spigotmc.org/jira/browse/SPIGOT-6701
      //Inventory click event does not fire for non-armor pieces when in creative. Not possible to fix unless mojang fixes it.
   }
}
