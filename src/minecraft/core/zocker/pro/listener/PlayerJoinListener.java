package minecraft.core.zocker.pro.listener;

import minecraft.core.zocker.pro.Zocker;
import minecraft.core.zocker.pro.event.ZockerDataInitializeEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Zocker zocker = Zocker.getZocker(e.getPlayer().getUniqueId());
		if (zocker != null) return;

		zocker = new Zocker(e.getPlayer());
		Zocker finalZocker = zocker;

		finalZocker.hasValueAsync("player", "uuid", "uuid", finalZocker.getPlayer().getUniqueId().toString()).thenApplyAsync(aBoolean -> {
			if (aBoolean) {
				finalZocker.set("player", "name", finalZocker.getPlayer().getName());
				Bukkit.getPluginManager().callEvent(new ZockerDataInitializeEvent(finalZocker));
				return true;
			}

			finalZocker.insert("player", new String[]{"uuid", "name"}, new Object[]{finalZocker.getPlayer().getUniqueId().toString(), finalZocker.getPlayer().getName()});
			Bukkit.getPluginManager().callEvent(new ZockerDataInitializeEvent(finalZocker));

			return aBoolean;
		});
	}
}
