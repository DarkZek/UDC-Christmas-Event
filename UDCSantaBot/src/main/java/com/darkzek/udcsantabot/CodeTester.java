package com.darkzek.udcsantabot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class CodeTester {

    public final String PREFAB_FOLDER = "/home/darkzek/example_folder/";
    public static final String KEY = Settings.STAGE_1_KEY;

    public String TestCode(String code) {

        String codeId = System.currentTimeMillis() + "";
        int testNumber = new Random().nextInt(14) + 10;

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("cp -R " + PREFAB_FOLDER + " /home/darkzek/" + codeId).waitFor();

            //Write the code file
            PrintWriter writer = new PrintWriter("/home/darkzek/" + codeId + "/Program.cs", "UTF-8");
            writer.println(code);
            writer.close();

            //Test the code
            String result = new CodeRunner().run(codeId, testNumber);

            //Delete the folder
            rt.exec("rm -R /home/darkzek/" + codeId).waitFor();

            //If there was an error just return
            if (result.startsWith("ERROR")) {
                return result;
            } else {
                return new CodeResultsTester().CompareResults(testNumber, KEY, result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "An error occurred when running your code.";
    }
}
