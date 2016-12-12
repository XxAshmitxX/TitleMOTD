package me.rojo8399.titlemotd.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.rojo8399.titlemotd.TitleMOTD;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Config implements Configurable {
	private static Config config = new Config();

	private Config() {
		
	}

	public static Config getConfig() {
		return config;
	}

	private Path configFile = Paths.get(TitleMOTD.getInstance().getConfigDir() + "/config.json");
	private ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder()
			.setPath(configFile).build();
	private CommentedConfigurationNode configNode;

	@Override
	public void setup() {
		if (!Files.exists(configFile)) {
			try {
				Files.createFile(configFile);
				load();
				populate();
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			load();
		}
	}

	@Override
	public void load() {
		try {
			configNode = configLoader.load();

			/*
			 * Config Updates
			 */
			switch (get().getNode("ConfigVersion").getInt()) {
			case 1: {
				// Alls Good :)
			}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save() {
		try {
			configLoader.save(configNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void populate() {
		get().getNode("ConfigVersion").setValue(1);
		get().getNode("firstjoin").setComment("Message on first join");
		get().getNode("firstjoin", "broadcast").setValue(false).setComment("Broadcast the message to all players");
		get().getNode("firstjoin", "title").setValue("&eROJO Software").setComment("Title message");
		get().getNode("firstjoin", "subtitle").setValue("&aWelcome {player_name}!").setComment("Subtitle message");
		get().getNode("firstjoin", "fadein").setValue(15).setComment("Fade in time in TICKS (20 ticks = 1 sec)");
		get().getNode("firstjoin", "staytime").setValue(50).setComment("Stay on screen time in TICKS");
		get().getNode("firstjoin", "fadeout").setValue(15).setComment("Fade out time in TICKS");
		
		get().getNode("rejoin").setComment("Message on rejoin");
		get().getNode("rejoin", "broadcast").setValue(false).setComment("Broadcast the message to all players");
		get().getNode("rejoin", "title").setValue("&eTitleMOTD").setComment("Title message");
		get().getNode("rejoin", "subtitle").setValue("&aWelcome back {player_name}!").setComment("Subtitle message");
		get().getNode("rejoin", "fadein").setValue(15).setComment("Fade in time in TICKS (20 ticks = 1 sec)");
		get().getNode("rejoin", "staytime").setValue(50).setComment("Stay on screen time in TICKS");
		get().getNode("rejoin", "fadeout").setValue(15).setComment("Fade out time in TICKS");
	}

	@Override
	public CommentedConfigurationNode get() {
		return configNode;
	}
	
	public String getTitleFirst() {
		return get().getNode("firstjoin", "title").getString();
	}
	
	public String getTitle() {
		return get().getNode("rejoin", "title").getString();
	}
	
	public String getSubTitleFirst() {
		return get().getNode("firstjoin", "subtitle").getString();
	}
	
	public String getSubTitle() {
		return get().getNode("rejoin", "subtitle").getString();
	}
	
	public int getFadeInFirst() {
		return get().getNode("firstjoin", "fadein").getInt();
	}
	
	public int getFadeIn() {
		return get().getNode("rejoin", "fadein").getInt();
	}
	
	public int getStayTimeFirst() {
		return get().getNode("firstjoin", "staytime").getInt();
	}
	
	public int getStayTime() {
		return get().getNode("rejoin", "staytime").getInt();
	}
	
	public int getFadeOutFirst() {
		return get().getNode("firstjoin", "fadeout").getInt();
	}
	
	public int getFadeOut() {
		return get().getNode("rejoin", "fadeout").getInt();
	}

}
