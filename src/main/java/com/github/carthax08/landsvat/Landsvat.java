package com.github.carthax08.landsvat;

import me.angeschossen.lands.api.integration.LandsIntegration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Landsvat extends JavaPlugin {

    public static LandsIntegration lands;
    public static Landsvat instance;


    @Override
    public void onEnable() {
        // Plugin startup logic
        String logPrefix = ChatColor.translateAlternateColorCodes('&',getConfig().getString("log-prefix"));
        Logger logger = getServer().getLogger();
        instance = this;
        logger.info(logPrefix + " Initializing plugin, please wait...");
        if(getServer().getPluginManager().isPluginEnabled("Lands")) {
            logger.info(logPrefix + " Initializing Lands integration...");
            lands = new LandsIntegration(this);
        }else{
            logger.info(logPrefix + " Lands not detected, skipping integration.");
        }
        logger.info(logPrefix + " Initializing event handlers, please wait.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
