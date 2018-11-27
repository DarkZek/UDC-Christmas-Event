package com.darkzek.udcsantabot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

import javax.security.auth.login.LoginException;

public class UDCSantaBot {

    public static JDA jda;

    public static void main(String[] args)
    {
        //Load settings
        Settings.getInstance();

        //Setup account
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Settings.BOT_TOKEN);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);

        //Setup the command manager so it can listen to events
        builder.addEventListener(new PrivateMessageListener());

        //Connect
        try {
            jda = builder.buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Properly shut down connection to discord
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            jda.shutdown();
        }));

        System.out.println("Started bot!");
    }
}
