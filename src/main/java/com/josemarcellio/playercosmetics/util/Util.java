package com.josemarcellio.playercosmetics.util;

import com.josemarcellio.playercosmetics.Cosmetics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class Util {
   
   /**
    * Refreshes cosmetics list. Used by RefreshCosmeticsList command
    *
    * @return User-friendly text Component with list of new cosmetics found
    */
   public static Component getUserFriendlyRefreshedCosmeticsList() {
      List<String> oldList;
      List<String> newList;
      List<String> differences;
      Component differencesNames;
      Component differencesMessage;
      Component refreshedMessage;
      
      oldList = new ArrayList<>();
      newList = new ArrayList<>();
      Cosmetics.getCachedCosmeticList().forEach((Cosmetic csm) -> oldList.add( String.valueOf ( csm.getCosmeticName() ) ));
      Cosmetics.getCosmeticFactory().getCosmeticsFromConfig().forEach((Cosmetic csm) -> newList.add( String.valueOf ( csm.getCosmeticName() ) ));
      //Horrible.
      
      if(oldList.toArray().length < newList.toArray().length) {
         differences = new ArrayList<>(newList);
         differences.removeAll(oldList);
         differencesMessage = Component.text("Cosmetics Reloaded!")
                 .color(TextColor.color(YELLOW));
      } else {
         differencesMessage = Component.text("");
         differences = new ArrayList<>();
      }
      
      differencesNames = Component.text("").color(TextColor.color(66,133,255));
      for (String dif : differences) {
         differencesNames = differencesNames.append(Component.text(dif+", "));
      }
      
      refreshedMessage = Component.text("Cosmetics Reloaded!")
              .color(TextColor.color(YELLOW));
      
      return refreshedMessage.append(differencesMessage).append(differencesNames);
   }
   
   /**
    * Returns whether a provided ItemStack is a Cosmetic or not.
    *
    * @param item ItemStack to verify
    * @return     True if Cosmetic, False if not
    */
   public static boolean isCosmetic(ItemStack item) {
      return getCosmeticString(item) != null && getCosmeticByte(item) != null;
   }
   
   /**
    * Gets the Cosmetic-type Byte from provided ItemStack
    *
    * @param item ItemStack to get Byte from
    * @return     Null if Byte does not exist, otherwise returns Byte
    */
   public static @Nullable Byte getCosmeticByte(ItemStack item) {
      NamespacedKey typeKey = new NamespacedKey(Cosmetics.getInstance(), "cosmetic-type");
      PersistentDataContainer metaContainer;
      
      if(item != null && item.hasItemMeta()) {
         metaContainer = item.getItemMeta().getPersistentDataContainer();
      } else return null;
      
      return metaContainer.get(typeKey, PersistentDataType.BYTE);
   }
   
   /**
    * Gets the Cosmetic-name String from provided ItemStack
    *
    * @param item ItemStack to get InternalName String from
    * @return     Null if String does not exist, otherwise returns String
    */
   public static @Nullable String getCosmeticString(ItemStack item) {
      NamespacedKey nameKey = new NamespacedKey(Cosmetics.getInstance(), "cosmetic-name");
      PersistentDataContainer metaContainer;
   
      if(item != null && item.hasItemMeta()) {
         metaContainer = item.getItemMeta().getPersistentDataContainer();
      } else return null;
      
      return metaContainer.get(nameKey, PersistentDataType.STRING);
   }
   
   /**
    * Get a Cosmetic from its InternalName
    *
    * @param name InternalName, always one "word"
    * @return     Cosmetic from InternalName, null if none found.
    */
   
   public @Nullable static Cosmetic getCosmeticFromName(String name) {
      for (Cosmetic cosmetic : Cosmetics.getCachedCosmeticList()) {
         if(cosmetic.getInternalName().equals(name)) { //probably inefficient for large amounts of cosmetics?
            return cosmetic;
         }
      }
      return null;
   }
   
   public static boolean isSpigot() {
      try {
         Class.forName("io.papermc.lib.PaperLib");
         return false;
      } catch (ClassNotFoundException e) {
         return true;
      }
   }
}
