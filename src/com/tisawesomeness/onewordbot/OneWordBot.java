package com.tisawesomeness.onewordbot;

import java.io.File;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

public class OneWordBot {

	private static JDA jda;
	private static Config config;

	public static void main(String[] args) {
		
		//Initialize config
		config = new Config(new File("./config.json"));
		
		//Override config values
		if (args.length > 0) {
			config.setClientToken(args[0]);
			if (args.length > 1) {
				config.setChannel(args[1]);
			}
		}
		
		//Initialize JDA
		System.out.println("Booting...");
		JDABuilder builder = new JDABuilder(AccountType.BOT)
			.setToken(config.getClientToken())
			.setAudioEnabled(false)
			.setAutoReconnect(true)
			.setGame(Game.of(config.getGame()))
			.addListener(new Listener(config));
		try {
			jda = builder.buildBlocking();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
