package com.github.carthax08.landsvat.files;

import com.github.carthax08.landsvat.Landsvat;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LandsConfig {
    private static final String pathSeperator = File.separator;
    private static HashMap<Land, YamlConfiguration> landConfigs = new HashMap<>();
    public static YamlConfiguration loadConfigForLand(Land land) throws IOException {
        File file = new File(Landsvat.instance.getDataFolder() + pathSeperator + "lands" + pathSeperator + land.getName() + ":" + land.getNation() + ":" + land.getId() + ".yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        landConfigs.put(land, YamlConfiguration.loadConfiguration(file));
        YamlConfiguration config =  YamlConfiguration.loadConfiguration(file);
        if(!config.isSet("vat-rate")){
            config.set("vat-rate", Landsvat.instance.getConfig().getDouble("default-vat-rate"));
        }
        return config;
    }

    public static double getVatRateForLand(Land land) {
        if(land == null){
            return 0;
        }
        if(landConfigs.containsKey(land)){
            return landConfigs.get(land).getDouble("vat-rate")/100;
        }else{
            try {
                return loadConfigForLand(land).getDouble("vat-rate")/100;
            }catch (IOException e){
                e.printStackTrace();
                return 0.00;
            }
        }
    }
    public static double getVatRateForLandRaw(Land land){
        if(landConfigs.containsKey(land)){
            return landConfigs.get(land).getDouble("vat-rate");
        }else{
            try {
                return loadConfigForLand(land).getDouble("vat-rate");
            }catch (IOException e){
                e.printStackTrace();
                return 0.00;
            }
        }
    }
    public static boolean setVatRateForLand(Land land, Double rate){
        if(landConfigs.containsKey(land)){
            landConfigs.get(land).set("vat-rate", rate);
            return true;
        }else{
            try {
                loadConfigForLand(land).set("vat-rate", rate);
                return true;
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
