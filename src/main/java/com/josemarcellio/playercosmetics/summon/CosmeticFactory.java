package com.josemarcellio.playercosmetics.summon;

import com.josemarcellio.playercosmetics.Cosmetics;
import com.josemarcellio.playercosmetics.util.Cosmetic;
import com.josemarcellio.playercosmetics.util.CosmeticType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CosmeticFactory {
   private ArrayList<String> cosmeticNames;
   private Integer cosmeticCount;
   
   public CosmeticFactory() {
      cosmeticNames = new ArrayList<>();
      cosmeticCount = 0;
   }
   
   /**
    * Returns a non-cached List of Cosmetic's by reading the config.
    * Uses default Material.PAPER if none is specified. Returns empty ArrayList for lore if none is specified.
    *
    * @return List of non-cached Cosmetic's from config
    */
   public @NotNull List<Cosmetic> getCosmeticsFromConfig() {
      List<Cosmetic> cosmeticsList = new ArrayList<>();
      Cosmetics.refreshCosmetics();
      FileConfiguration config = Cosmetics.getCosmetics();
      
      for (String internalname : config.getConfigurationSection("PlayerCosmetics").getKeys(false)) {
         Component name;
         Integer modelID;
         Material material;
         List<String> lore;
         List<Component> formattedLore;
         CosmeticType type;
         Map<String,Object> valuesmap;
         
         cosmeticCount++;
         cosmeticNames.add(internalname);
         
         lore = new ArrayList<>();
         formattedLore = new ArrayList<>();
         valuesmap = config.getConfigurationSection("PlayerCosmetics."+internalname).getValues(false);
         name = Component.text((String) valuesmap.get("name"));
         modelID = (Integer) valuesmap.get("modeldata");
         type = CosmeticType.HAT;
         
         if(valuesmap.get("material") != null) material = Material.matchMaterial(valuesmap.get("material").toString());
         else material = Material.PAPER;
         
         if (valuesmap.get("lore") instanceof List) lore = (List<String>) valuesmap.get("lore");
         
         lore.forEach((String stg) -> formattedLore.add(
                 Component.text(stg)
                 .color(TextColor.color(170, 170, 170))
                 .decoration(TextDecoration.ITALIC,false)
         ));
         
         Cosmetic cosmetic = new Cosmetic(internalname,name,modelID,material,formattedLore,type);
         cosmeticsList.add(cosmetic);
      }
      Cosmetics.setCachedCosmeticList(cosmeticsList);
      return cosmeticsList;
   }
   
   public Integer getCosmeticCount() {
      return cosmeticCount;
   }
   public List<String> getInternalCosmeticNames() {
      return cosmeticNames;
   }
   
}
