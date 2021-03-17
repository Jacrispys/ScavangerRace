package com.Jacrispys.ScavangerRace.Listeners;

import com.Jacrispys.ScavangerRace.ScavangerRaceMain;
import com.Jacrispys.ScavangerRace.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.Jacrispys.ScavangerRace.utils.chat.chat;

public class UpdateListener implements Listener {

    private final ScavangerRaceMain plugin;

    public UpdateListener(ScavangerRaceMain plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void UpdateChecker(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.hasPermission("scav.admin")) {
            UpdateChecker.init(plugin, 12038).requestUpdateCheck().whenComplete((result, exception) -> {
                if (result.requiresUpdate()) {
                    p.sendMessage(chat(String.format("&3An update is available! ScavengerRace&b %s &3may be downloaded on SpigotMC", result.getNewestVersion())));
                    return;
                }

                UpdateChecker.UpdateReason reason = result.getReason();
                if (reason == UpdateChecker.UpdateReason.UP_TO_DATE) {
                    p.sendMessage(chat(String.format("&3Your version of ScavengerRace&b (%s) &3is up to date!&b", result.getNewestVersion())));
                } else if (reason == UpdateChecker.UpdateReason.UNRELEASED_VERSION) {
                    p.sendMessage(chat(String.format("&3Your version of ScavengerRace&b (%s) &3is more recent than the one publicly available. Are you on a development build?&b", result.getNewestVersion())));
                } else {
                    p.sendMessage(chat("&cCould not check for a new version of ScavengerRace. Reason: &4" + reason));
                }
            });
        }
    }
}
