package com.josemarcellio.playercosmetics.util;

import com.josemarcellio.playercosmetics.Cosmetics;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main Cosmetic class.
 *
 */
public class Cosmetic {
   private final String internalCosmeticName;
   private final Component cosmeticName;
   private final Integer modelID;
   private final Material material;
   private final List<Component> lore;
   private final CosmeticType type;

   public Cosmetic(String internalCosmeticName, Component cosmeticName, Integer modelID, Material material, List<Component> lore, CosmeticType type) {
      this.internalCosmeticName = internalCosmeticName;
      this.cosmeticName = cosmeticName
              .decoration(TextDecoration.ITALIC,false);
      this.modelID = modelID;
      this.material = material;
      this.type = type;
      this.lore = lore;
   }

   public @NotNull Integer getModelID() {
      return modelID;
   }
   public @NotNull Component getCosmeticName() {
      return cosmeticName;
   }
   public @NotNull String getCosmeticNameOnSpigot() {
      return LegacyComponentSerializer.builder().build().serialize(cosmeticName);
   }
   public @NotNull Material getMaterial() {
      return material;
   }
   public @NotNull List<Component> getLore() {
      return lore;
   }
   public @NotNull List<String> getLoreOnSpigot() {
      List<String> lore = new ArrayList<>();
      this.lore.forEach((l) -> lore.add(LegacyComponentSerializer.builder().build().serialize(l).replaceAll("&", "§")));
      return lore;
   }
   public @NotNull CosmeticType getType() {
      return type;
   }
   public @NotNull String getInternalName() {
      return internalCosmeticName;
   }

   /**
    * Creates a ItemStack with the properties of the provided Cosmetic
    *
    * @return         ItemStack that was created
    */
   public @NotNull ItemStack getCosmeticItemStack() {
      ItemStack stack = new ItemStack(this.getMaterial());
      ItemMeta meta = stack.getItemMeta();
      NamespacedKey typekey = new NamespacedKey(Cosmetics.getInstance(),"cosmetic-type");
      NamespacedKey namekey = new NamespacedKey(Cosmetics.getInstance(),"cosmetic-name".replaceAll("&", "§"));
      PersistentDataContainer metaContainer = meta.getPersistentDataContainer();
      if(Util.isSpigot()) {
         List<String> lore = new ArrayList<>();
         LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
         this.lore.forEach((l) -> lore.add(serializer.serialize(l).replaceAll("&", "§")));
         meta.setDisplayName(serializer.serialize(cosmeticName).replaceAll("&", "§"));
         meta.setLore(lore);
      } else {
         meta.lore(this.getLore());
         meta.displayName(this.getCosmeticName());
      }
      meta.setCustomModelData(this.getModelID());
      metaContainer.set(typekey,PersistentDataType.BYTE,this.getType().getID());
      metaContainer.set(namekey,PersistentDataType.STRING,this.getInternalName());

      stack.setItemMeta(meta);

      return stack;
   }

   /**
    * Fetch a Cosmetic from a provided ItemStack
    *
    * @param itemStack    ItemStack to get Cosmetic from
    * @return             Cosmetic if ItemStack is a valid cosmetic, Null if ItemStack is not a valid cosmetic
    */
   public static @Nullable Cosmetic getCosmeticFromItemStack(@NotNull ItemStack itemStack) {
      ItemMeta meta = itemStack.getItemMeta();
      NamespacedKey typekey = new NamespacedKey(Cosmetics.getInstance(),"cosmetic-type");
      NamespacedKey namekey = new NamespacedKey(Cosmetics.getInstance(),"cosmetic-name");
      NamespacedKey raritykey = new NamespacedKey(Cosmetics.getInstance(), "rarity");
      PersistentDataContainer metaContainer = meta.getPersistentDataContainer();
      Optional<CosmeticType> type;
      String internalname;

      if(Util.isCosmetic(itemStack)) {
         type = CosmeticType.getFromID(metaContainer.get(typekey, PersistentDataType.BYTE));
         internalname = metaContainer.get(namekey,PersistentDataType.STRING);
      } else {
         return null;
      }
      if(itemStack.hasItemMeta() & meta.hasDisplayName() & meta.hasCustomModelData() & meta.hasLore() & type.isPresent()) {
         if(Util.isSpigot()) {
            List<Component> lore = new ArrayList<>();
            LegacyComponentSerializer serializer = LegacyComponentSerializer.builder().build();
            meta.getLore().forEach((l) -> lore.add(serializer.deserialize(l)));
            return new Cosmetic(internalname,Component.text(meta.getDisplayName()),meta.getCustomModelData(),itemStack.getType(),lore,type.get());
         }
         return new Cosmetic(internalname,meta.displayName(),meta.getCustomModelData(),itemStack.getType(),meta.lore(),type.get());
      } else {
         return null;
      }
   }
}