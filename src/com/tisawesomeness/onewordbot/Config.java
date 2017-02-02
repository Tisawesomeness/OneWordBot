package com.tisawesomeness.onewordbot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

	private String clientToken;
	private int historyLength;
	private int notificationTime;
	private String wordRegex;
	private String game;
	private String channel;
	private boolean checkEdits;
	private boolean checkMentions;
	
	private String doubleMessage;
	private String invalidWord;

	public Config(File configFile) {
		try {
			JSONObject config = new JSONObject(FileUtils.readFileToString(configFile, "UTF-8"));
			
			clientToken = config.optString("clientToken");
			historyLength = config.optInt("historyLength", 5);
			if (historyLength < 1) {historyLength = 1;}
			notificationTime = config.optInt("notificationTime", 4000);
			if (notificationTime < 1) {notificationTime = 1;}
			wordRegex = config.optString("wordRegex");
			game = config.optString("game");
			channel = config.optString("channel", null);
			checkEdits = config.optBoolean("checkEdits", false);
			checkMentions = config.optBoolean("checkMentions", true);
			
			JSONObject lang = config.optJSONObject("lang");
			doubleMessage = lang.optString("doubleMessage");
			invalidWord = lang.optString("invalidWord");
			
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public String getClientToken() {
		return clientToken;
	}
	public void setClientToken(String clientToken) {
		this.clientToken = clientToken;
	}
	public int getHistoryLength() {
		return historyLength;
	}
	public int getNotificationTime() {
		return notificationTime;
	}
	public String getWordRegex() {
		return wordRegex;
	}
	public String getGame() {
		return game;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public boolean getCheckEdits() {
		return checkEdits;
	}
	public boolean getCheckMentions() {
		return checkMentions;
	}
	
	public String getDoubleMessage() {
		return doubleMessage;
	}
	public String getInvalidWord() {
		return invalidWord;
	}
	
}
