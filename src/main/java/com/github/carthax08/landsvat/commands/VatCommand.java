package com.github.carthax08.landsvat.commands;

import com.github.carthax08.landsvat.Landsvat;
import com.github.carthax08.landsvat.files.LandsConfig;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use that!");
            return true;
        }

        LandsIntegration lands = Landsvat.lands;
        Player player = (Player) sender;

        if (!lands.isClaimed(player.getLocation())) {
            player.sendMessage("You must be in a claimed area to use this command!");
            return true;
        }

        Land land = lands.getLand(player.getLocation());

        if (args.length == 0) {
            player.sendMessage("The vat rate for the nation you are in is " + LandsConfig.getVatRateForLandRaw(land) + "%");
            return true;
        }
        assert land != null;
        if (!land.getOwnerUID().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You must be the owner of this nation to run this command!");
            return true;
        }
        if (args[0].equalsIgnoreCase("set") && args.length == 1) {
            player.sendMessage(ChatColor.RED + "You must provide a new VAT rate!");
            return true;
        }

        double newRate = Double.parseDouble(args[1]);

        if (newRate != LandsConfig.getVatRateForLandRaw(land)) {
            LandsConfig.setVatRateForLand(land, newRate);
        }

        player.sendMessage(ChatColor.GREEN + "VAT rate updated to " + newRate);

        return true;

    }
}
