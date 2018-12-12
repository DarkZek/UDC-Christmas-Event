package com.darkzek.udcsantabot;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class PrivateMessageListener extends ListenerAdapter {

    private CodeTester tester = new CodeTester();
    public final String UDC_SERVER_ID = "493510779866316801";
    public final String HELP_MESSAGE = "UDC Santa is a bot to assist with the UDC Christmas Event found at https://christmas.unitydeveloperhub.com/\n" +
            "To enter code for stage 2 just reply to this message with three grave symbols (\\`\\`\\`) surrounding your code ```as such```";

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

        if (event.getChannelType() != ChannelType.PRIVATE || event.getAuthor().isBot()) {
            return;
        }

        //The whole no activator character may confuse some so lets just get all instances of help
        String message = event.getMessage().getContentRaw();

        int length = message.length();
        if (length > 10) {
            length = 10;
        }

        //Don't want them to have to guess random activator characters
        if (message.substring(0, length).contains("help")) {
            showHelp(event.getPrivateChannel());
            return;
        }

        //They're trying to run code
        if (message.startsWith("```")) {
            testCode(event);
            return;
        }

        //They just completed the jam
        if (message.equalsIgnoreCase(Settings.COMPLETED_MESSAGE)) {
            completedChallenge(event);
        }
    }

    private void showHelp(PrivateChannel channel) {
        channel.sendMessage(HELP_MESSAGE).queue();
    }

    private void testCode(MessageReceivedEvent event) {

        event.getPrivateChannel().sendMessage("Processing code now...").queue();

        final String finalCode = extractCode(event.getMessage().getContentDisplay());

        //Run code
        try {
            new Thread(() -> {
                String result = "";

                if (!finalCode.contains(CodeTester.KEY)) {
                    result += "***Note: Your code does not contain the correct key. Remember to remove the spaces from your key**\n";
                }

                //Get the result from the code
                result += tester.TestCode(finalCode);

                if (result.length() > 2000) {
                    result = result.substring(0, 1996) + "...";
                }


                event.getPrivateChannel().sendMessage(result).queue();
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            event.getPrivateChannel().sendMessage("An error has occurred running your code, please notify DarkZek#2276").queue();
        }
    }

    private String extractCode(String code) {
        //Remove grave symbols
        code = code.substring(3, code.length() - 3);

        if (code.length() > 7) {
            //Get the few beginning characters
            String beginning = code.substring(0, 6).toLowerCase();

            //Remove code tags
            if (beginning.startsWith("cs")) {
                code = code.substring(2);
            } else if (beginning.startsWith("csharp")) {
                code = code.substring(6);
            }
        }

        return code;
    }

    private void completedChallenge(MessageReceivedEvent e) {

        //TODO: Send message to 2BDroid to give rank & karma
        final Guild UDC = e.getJDA().getGuildById(UDC_SERVER_ID);
        final Role christmasRole = UDC.getRoleById(521454263969513482L);

        if (UDC.getMember(e.getAuthor()).getRoles().contains(christmasRole)) {
            e.getPrivateChannel().sendMessage("You already have completed this event!").queue();
            return;
        }

        if (UDC.getMember(e.getAuthor()) == null) {
            e.getPrivateChannel().sendMessage("You must be in the discord server `Unity Developer Hub` to claim your prize. Join here:  https://discord.gg/bu3bbby").queue();
            return;
        }

        int peopleCompleted = UDC.getMembersWithRoles(christmasRole).size();

        e.getPrivateChannel().sendMessage("Congratulations and thank you for participating! Your rank has been added.").queue();

        UDC.getTextChannelById(493512044973260811L).sendMessage("!completedchristmasevent " + e.getAuthor().getId()).queue();
        UDC.getTextChannelById(521775461987254273L).sendMessage(e.getAuthor().getName() + " just completed the UDC Christmas Event 2018! So far `" + peopleCompleted + "` have completed the christmas event").queue();



    }
}
