package me.rojo8399.titlemotd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import com.google.inject.Inject;

import me.rojo8399.titlemotd.config.Config;

@Plugin(id = TitleMOTD.PLUGIN_ID, name = TitleMOTD.PLUGIN_NAME, version = TitleMOTD.PLUGIN_VERSION, authors = "rojo8399")
public class TitleMOTD {

	public static final String PLUGIN_ID = "tmotd";
	public static final String PLUGIN_NAME = "TitleMOTD";
	public static final String PLUGIN_VERSION = "1.0";

	@Inject
	private static TitleMOTD instance;

	@Inject
	private static Logger logger;

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;

	@Listener
	public void onGamePreInitializationEvent(GamePreInitializationEvent e) {
		instance = this;

		// Create Configuration Directory for CustomPlayerCount
		if (!Files.exists(configDir)) {
			try {
				Files.createDirectories(configDir);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		// Setup Config File
		Config.getConfig().setup();
	}

	@Listener
	public void onReload(GameReloadEvent e) {
		Config.getConfig().load();
	}

	@Listener
	public void onClientConnectionEventJoin(ClientConnectionEvent.Join e) {
		Config c = Config.getConfig();
		String Ftitle = c.getTitleFirst();
		String FsubTitle = c.getSubTitleFirst();
		int FfadeIn = c.getFadeInFirst();
		int FstayTime = c.getStayTimeFirst();
		int FfadeOut = c.getFadeOutFirst();
		String title = c.getTitle();
		String subTitle = c.getSubTitle();
		int fadeIn = c.getFadeIn();
		int stayTime = c.getStayTime();
		int fadeOut = c.getFadeOut();

		Player p = e.getTargetEntity();

		if (p.hasPermission("titlemotd.show")) {
			if (p.hasPlayedBefore()) {
				// ReJoin
				Title built = Title.builder().title(format(title, p)).subtitle(format(subTitle, p)).fadeIn(fadeIn)
						.stay(stayTime).fadeOut(fadeOut).build();
				p.sendTitle(built);
			} else {
				// First Join
				Title built = Title.builder().title(format(Ftitle, p)).subtitle(format(FsubTitle, p)).fadeIn(FfadeIn)
						.stay(FstayTime).fadeOut(FfadeOut).build();
				p.sendTitle(built);
			}
		}
	}

	private Text format(String s, Player p) {
		return TextSerializers.FORMATTING_CODE.deserialize(s
						.replace("{player_name}", p.getName())
						.replace("{player_displayname}", p.getDisplayNameData().displayName().toString())
						.replace("{online}", String.valueOf(Sponge.getServer().getOnlinePlayers().size()))
						.replace("{max_players}", String.valueOf(Sponge.getServer().getMaxPlayers()))
						.replace("{xp}", p.get(Keys.EXPERIENCE_LEVEL).toString())
						.replace("{isflying}", p.get(Keys.IS_FLYING).toString())
						.replace("{isinwater}", p.get(Keys.IS_WET).toString())
						.replace("{playeruuid}", p.getUniqueId().toString())
					);
	}

	public static TitleMOTD getInstance() {
		return instance;
	}

	public Logger getLogger() {
		return logger;
	}

	public Path getConfigDir() {
		return configDir;
	}

}
