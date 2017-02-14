package com.tisawesomeness.onewordbot;

import java.util.List;

import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
	
	private Config config;
	
	public Listener(Config config) {
		this.config = config;
	}

	//When the bot has been set up
	@Override
	public void onReady(ReadyEvent e) {
        System.out.println("Bot started.");
    }
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		Message m = e.getMessage();
		MessageChannel c = m.getChannel();
		User u = m.getAuthor();
		
		//If the message was sent in the right channel
		if (c.getId().equals(config.getChannel())) {
			
			//If sender is human
			if (!m.getAuthor().isBot()) {
				
				//Get previous messages
				MessageHistory mh = new MessageHistory(c);
				mh.retrievePast(this.config.getHistoryLength()).complete();
				
				List<Message> past = mh.getCachedHistory();
				Message prev = past.get(0);
				
				//Find the first human message
				for (Message i : past) {
					if (i == prev) {continue;}
					if (!i.getAuthor().isBot()) {
						prev = i;
						break;
					}
				}
				
				TextChannel tc = m.getTextChannel();
				
				//If the sender sent two messages in a row
				if (prev.getAuthor() == u && !prev.equals(m)) {
					m.deleteMessage().queue();
					notify(config.getDoubleMessage(), tc);
					return;
				}
				
				//If there are no mentions or mentions are not checked
				if (m.getMentionedUsers().size() == 0 || config.getCheckMentions()) {
					check(m);
				} else {
					User user = m.getMentionedUsers().get(0);
					String msg = new StringBuilder(m.getRawContent()).deleteCharAt(2).toString();
						
					//If the message contains more than just the mention
					if (!user.getAsMention().equals(msg)) {
						m.deleteMessage().queue();
						notify(config.getInvalidWord(), tc);
					}
				}
			}
			
		}
	}
	
	@Override
	public void onMessageUpdate(MessageUpdateEvent e) {
		if (config.getCheckEdits() && e.getChannel().getId().equals(config.getChannel())) {
			check(e.getMessage());
		}
	}
	
	public boolean check(Message m) {
		
		//Tests the message against the word regex
		//Default regex:
		//^([A-z0-9'"\-_]+[.,!?;:]?|\$?[0-9.]+[$%]?)$
		//Escaped regex for config file:
		//^([A-z0-9'\"\\-_]+[.,!?;:]?|\\$?[0-9.]+[$%]?)$
		
		if (m.getRawContent().matches(config.getWordRegex())) {
			return false;
		} else {
			m.deleteMessage().queue();
			notify(config.getInvalidWord(), m.getTextChannel());
			return true;
		}
		
	}

	public void notify(String m, TextChannel c) {
		Message notification = c.sendMessage(m).complete();
		
		new java.util.Timer().schedule( 
			new java.util.TimerTask() {
				@Override
				public void run() {
					notification.deleteMessage().queue();
				}
			}, 
			config.getNotificationTime()
		);
	}
	
}
