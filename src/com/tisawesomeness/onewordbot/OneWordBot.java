package com.tisawesomeness.onewordbot;

import java.io.File;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class OneWordBot {

	private static JDA jda;
	private static Config config;

	public static void main(String[] args) {
		
		//Initialize config
		config = new Config(new File("./config.json"));
		
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
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			e.printStackTrace();
		}

	}

}
