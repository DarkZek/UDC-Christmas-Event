package com.darkzek.udcsantabot;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Settings {

    private static Settings settings = new Settings( );
    public static String COMPLETED_MESSAGE;
    public static String BOT_TOKEN;
    public static String SSH_PASSWORD;
    public static String STAGE_1_KEY;

    //Load settings
    private Settings() {
        BOT_TOKEN = getFile("token.txt");
        COMPLETED_MESSAGE = getFile("completed_message.txt");
        SSH_PASSWORD = getFile("ssh_password.txt");
        STAGE_1_KEY = getFile("stage_1_key.txt");
    }

    /* Static 'instance' method */

    public static Settings getInstance( ) {
        return settings;
    }

    public String getFile(String fileName) {
        try {
            File file = new File("./private/" + fileName);

            Scanner scanner = new Scanner(file);
            String token = scanner.nextLine();
            return token;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return "";
    }
}
