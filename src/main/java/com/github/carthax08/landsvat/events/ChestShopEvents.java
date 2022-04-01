package com.github.carthax08.landsvat.events;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.ShopCreatedEvent;
import com.github.carthax08.landsvat.Landsvat;
import com.github.carthax08.landsvat.files.LandsConfig;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class ChestShopEvents implements Listener {

    private final HashMap<Sign, Double> vatAmountMap = new HashMap<>();

    @EventHandler
    public void preShopCreationEvent(PreShopCreationEvent event){
        if(Landsvat.lands.isClaimed(event.getSign().getLocation())){
            double vat_rate = LandsConfig.getVatRateForLand(Landsvat.lands.getLand(event.getSign().getLocation()));
            double vatAmount;
            String priceLine = event.getSign().getLine(2);
            String[] priceParts = priceLine.split(":");
            for (String part : priceParts){
                if(!part.contains("B")){
                    continue;
                }
                part = part.replace(" ", "");
                part = part.replace("B", "");
                double price = Double.parseDouble(part);
                vatAmount = price * vat_rate;
                vatAmount = Math.round(vatAmount);
                vatAmountMap.put(event.getSign(), vatAmount);
                priceLine = priceLine.replaceFirst(String.valueOf(price), String.valueOf(price + vatAmount));
            }
            event.getSign().setLine(2, priceLine);

        }
    }
    @EventHandler
    public void PostShopCreationEvent(ShopCreatedEvent event) {
        if(vatAmountMap.containsKey(event.getSign())) {
            String line = event.getSign().getLine(2);
            String[] lineParts = line.split(":");
            for (String part : lineParts){
                if(!part.contains("B")){
                    continue;
                }
                part = part.replace(" ", "");
                part = part.replace("B", "");
                double price = Double.parseDouble(part);
                double adjustedPrice = price - vatAmountMap.get(event.getSign());
                line = line.replaceFirst(String.valueOf(price), adjustedPrice + "(+" + (price - adjustedPrice) + ")");
            }
            event.getSign().setLine(2, line);
        }
    }
    @EventHandler
    public void PreTransactionEvent(PreTransactionEvent event){
        System.out.println(event.getExactPrice());
    }
}
