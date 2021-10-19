package com.josemarcellio.playercosmetics.manager;

import com.josemarcellio.playercosmetics.PlayerCosmetics;
import com.josemarcellio.playercosmetics.enums.CosmeticsEnum;
import com.josemarcellio.playercosmetics.utils.Cosmetics;
import com.josemarcellio.playercosmetics.utils.PlayerCosmeticsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerCosmeticsManager {
    private final ArrayList<String> cosmeticNames;
    private Integer cosmeticCount;

    public PlayerCosmeticsManager() {
        cosmeticNames = new ArrayList<>();
        cosmeticCount = 0;
    }

    /**
     * Returns a non-cached List of Cosmetic's by reading the config.
     * Uses default Material.PAPER if none is specified. Returns empty ArrayList for lore if none is specified.
     *
     * @return List of non-cached Cosmetic's from config
     */
    public @NotNull List<Cosmetics> getCosmeticsFromConfig() {
        List<Cosmetics> cosmeticsList = new ArrayList<>();
        PlayerCosmetics.refreshCosmetics();
        FileConfiguration config = PlayerCosmetics.getCosmetics();

        for (String internalname : Objects.requireNonNull ( config.getConfigurationSection ( "PlayerCosmetics" ) ).getKeys(false)) {
            Component name;
            Integer modelID;
            Material material;
            List<String> lore;
            List<Component> formattedLore;
            CosmeticsEnum type;
            Map<String,Object> valuesmap;

            cosmeticCount++;
            cosmeticNames.add(internalname);

            lore = new ArrayList<>();
            formattedLore = new ArrayList<>();
            valuesmap = Objects.requireNonNull ( config.getConfigurationSection ( "PlayerCosmetics." + internalname ) ).getValues(false);
            name = Component.text((String) valuesmap.get("name"));
            modelID = (Integer) valuesmap.get("modeldata");
            type = CosmeticsEnum.valueOf((String)valuesmap.get("canequip"));

            if(valuesmap.get("material") != null) material = Material.matchMaterial(valuesmap.get("material").toString());
            else material = Material.PAPER;

            if (valuesmap.get("lore") instanceof List) lore = (List<String>) valuesmap.get("lore");

            lore.forEach((String stg) -> formattedLore.add(
                    Component.text(stg)
                            .color(TextColor.color(170, 170, 170))
                            .decoration(TextDecoration.ITALIC,false)
            ));

            Cosmetics cosmetic = new Cosmetics(internalname,name,modelID,material,formattedLore,type);
            cosmeticsList.add(cosmetic);
        }
        PlayerCosmetics.setCachedCosmeticList(cosmeticsList);
        return cosmeticsList;
    }

    public Integer getCosmeticCount() {
        return cosmeticCount;
    }
    public List<String> getInternalCosmeticNames() {
        return cosmeticNames;
    }

    public void equipCosmetic(Player player, ItemStack item, EquipmentSlot slot) {
        if(PlayerCosmeticsUtils.isCosmetic(item)) {
            Cosmetics cosmetic = Cosmetics.getCosmeticFromItemStack(item);
            CosmeticsEnum cosmeticType = Objects.requireNonNull ( cosmetic ).getType();
            PlayerInventory inv = player.getInventory();

            if (inv.getItem(36 + cosmeticType.getID()) == null && cosmeticType != CosmeticsEnum.CANNOT) { //dont switch inventory items
                inv.setItem(36 + cosmeticType.getID(), item);
                inv.setItem(slot,null);
            }
            //Could *re-*make something that switches two cosmetics, but thats not vanilla behaviour ;)
        }
    }

    /**
     * Equip a cosmetic from slot click event, if an item is already present, switch items on cursor and slot
     * ONLY CALL WHEN PLAYER'S INVENTORY IS OPEN!
     *
     * @param player  HumanEntity who clicked
     * @param inv     Inventory that was clicked
     * @param inSlot    ItemStack already present in slot
     * @param cursor  ItemStack present on player cursor
     * @param slotInt Int of the slot that was clicked
     */

    public void equipCosmeticFromSlotClick(HumanEntity player, Inventory inv, ItemStack inSlot, ItemStack cursor, int slotInt) {
        if(PlayerCosmeticsUtils.isCosmetic(cursor)) {
            Cosmetics cosmetic = Cosmetics.getCosmeticFromItemStack(cursor);
            CosmeticsEnum cosmeticType = Objects.requireNonNull ( cosmetic ).getType();

            if (slotInt == 36 + cosmeticType.getID()) {
                /*verify that the cosmetic-type matches armor slot & slot type*/
                if (inSlot == null || inSlot.equals(new ItemStack(Material.AIR))) {
                    player.setItemOnCursor(null);
                    Bukkit.getScheduler().runTask(PlayerCosmetics.getInstance(), () -> inv.setItem(slotInt, cursor));
                } else { // switcher
                    Bukkit.getScheduler().runTask(PlayerCosmetics.getInstance(), () -> inv.setItem(slotInt, cursor));
                    Bukkit.getScheduler().runTaskLater(PlayerCosmetics.getInstance(), () -> player.setItemOnCursor(inSlot), 1);
                }
            }
        }
    }

    /**
     * Called when player shift-clicks a Cosmetic into its slot
     *
     * @param shiftClickedItem Item that was shift-clicked
     * @param inv              Inventory where Item was shift-clicked in
     * @param slot             Slot where shiftClickedItem was shift-clicked from
     */

   // public void equipCosmeticFromShiftClick(ItemStack shiftClickedItem, Inventory inv, int slot) {
     //   if(PlayerCosmeticsUtils.isCosmetic(shiftClickedItem)) {
       //     CosmeticsEnum cosmeticType = Objects.requireNonNull ( Cosmetics.getCosmeticFromItemStack ( shiftClickedItem ) ).getType();

          //  ItemStack itemInSlot = inv.getItem(36 + cosmeticType.getID());
          //  int slotInt = 36 + cosmeticType.getID();

        //    if (itemInSlot == null || itemInSlot.equals(new ItemStack(Material.AIR))) {
          //      inv.setItem(slotInt, shiftClickedItem);
        //        inv.setItem(slot, null);
      //      }
     //   }
   // }

}
