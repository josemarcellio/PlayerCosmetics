package com.josemarcellio.playercosmetics;

import com.josemarcellio.playercosmetics.commands.CosmeticsCommand;
import com.josemarcellio.playercosmetics.commands.CosmeticsReload;
import com.josemarcellio.playercosmetics.listeners.ClickCosmetics;
import com.josemarcellio.playercosmetics.manager.PlayerCosmeticsManager;
import com.josemarcellio.playercosmetics.metrics.Metrics;
import com.josemarcellio.playercosmetics.utils.Cosmetics;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PlayerCosmetics extends JavaPlugin {
   private static PlayerCosmetics instance;
   private static PlayerCosmeticsManager cosmeticFactory;
   private static List<Cosmetics> cosmeticList;
   
   private static FileConfiguration cosmetics;
   private static File cosmeticsFile;
   
   // Static abuse? No, Static abusage.
   
   @Override
   public void onEnable() {
      instance = this;
      this.createCosmetics();
      
      if(cosmeticFactory == null) {
         cosmeticFactory = new PlayerCosmeticsManager();
         cosmeticList = cosmeticFactory.getCosmeticsFromConfig();
      }

      new Metrics (this, 13060);
      this.getLogger ().info ( "---------------------------------------------" );
      this.getLogger ().info ( "" );
      this.getLogger ().info ( "   PlayerCosmetics by JoseMarcellio" );
      this.getLogger ().info ( "" );
      this.getLogger ().info ( "---------------------------------------------" );
      // probably a good idea to eventually move these elsewhere to work as a method
      Objects.requireNonNull ( this.getCommand ( "playercosmeticsgive" ) ).setExecutor(new CosmeticsCommand ());
      Objects.requireNonNull ( this.getCommand ( "playercosmeticsreload" ) ).setExecutor(new CosmeticsReload ());
      getServer().getPluginManager().registerEvents(new ClickCosmetics(), this);
   }
   
   @Override
   public void onDisable() {
      this.getLogger ().info ( "Disabling PlayerCosmetics" );
   }
   
   public static JavaPlugin getInstance() { return instance; }
   public static PlayerCosmeticsManager getCosmeticFactory() { return cosmeticFactory; }
   
   /**
    * Get cached List of Cosmetic's. Use CosmeticFactory#getCosmeticsFromConfig() for a non-cached list!
    * @return List of cached (read from config at load) Cosmetic's
    */
   public static List<Cosmetics> getCachedCosmeticList() { return cosmeticList; }
   
   /**
    * Called from CosmeticFactory#getCosmeticsFromConfig(), refreshes cache and updates cosmetic list if new ones are found
    * in the config!
    * @param cList List of Cosmetic's from CosmeticFactory#getCosmeticsFromConfig()
    */
   public static void setCachedCosmeticList(List<Cosmetics> cList) { cosmeticList = cList;}
   
   /**
    * Return cosmetics.yml FileConfiguration.
    *
    * @return FileConfiguration of cosmetics.yml
    */
   public static FileConfiguration getCosmetics() {
      return cosmetics;
   }
   
   /**
    * Reloads cosmetics file for use in CosmeticFactory.
    */
   public static void refreshCosmetics() {
      try {
         cosmetics.load(cosmeticsFile);
      } catch (IOException | InvalidConfigurationException e) {
         e.printStackTrace();
      }
   }
   
   // https://www.spigotmc.org/wiki/config-files/
   private void createCosmetics() {
      cosmeticsFile = new File(getDataFolder(), "config.yml");
      if (!cosmeticsFile.exists()) {
         cosmeticsFile.getParentFile().mkdirs();
         saveResource("config.yml", false);
      }
      
      cosmetics = new YamlConfiguration();
      try {
         cosmetics.load(cosmeticsFile);
      } catch (IOException | InvalidConfigurationException e) {
         e.printStackTrace();
      }
   }
   
}
