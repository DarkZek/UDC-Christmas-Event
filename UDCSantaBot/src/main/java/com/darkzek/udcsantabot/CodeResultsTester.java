package com.darkzek.udcsantabot;

public class CodeResultsTester {

    public String CompareResults(int targetNumber, String key, String userInput) {
        String correctAnswer = GenerateCorrectAnswer(targetNumber, key);

        //Check if they got it right
        if (userInput.trim().equalsIgnoreCase(correctAnswer.trim())) {
            return "Success! Your program worked. The password is `" + Settings.SSH_PASSWORD + "`";
        } else {
            return "Sorry, your program didn't work! I used the number `" + targetNumber + "` and your program returned `" + userInput + "`";
        }
    }

    public String GenerateCorrectAnswer(int offset, String key) {

        String newMessage = "";

        for (int i = 0; i < key.length(); i++) {
            int characterCode = (((int)(key.charAt(i))) + offset);

            if (characterCode > 122) {
                characterCode -= 25;
            }

            newMessage += (char)characterCode;
        }

        return newMessage;
    }
}
