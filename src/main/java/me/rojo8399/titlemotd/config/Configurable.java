package me.rojo8399.titlemotd.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public interface Configurable
{
	void setup();

	void load();

	void save();

	void populate();

	CommentedConfigurationNode get();
}
