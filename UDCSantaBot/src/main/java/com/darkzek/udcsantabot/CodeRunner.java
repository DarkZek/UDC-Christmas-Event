package com.darkzek.udcsantabot;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class CodeRunner {

    //
    // I am disgraced with this class
    //

    public final String COMMAND = "/home/darkzek/run.sh ";

    public String run(String projectDirectory, int number) {
        Runtime rt = Runtime.getRuntime();

        try {
            //Command to start the docker container which runs the code
            List<String> lines = Arrays.asList("#!/bin/bash", "docker run --stop-timeout 10 --rm -v  /home/darkzek/" + projectDirectory + "/:/mnt sixeyed/coreclr-hello-world /bin/bash -c 'cd /mnt/ && timeout 20 dnx run " + number + "\'");

            //Script to run the code
            Path file = Paths.get("/home/darkzek/run.sh");

            //Write the command into the script (it wont work if we just execute the command directly)
            Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.CREATE);

            //Run the command
            Process pr = rt.exec(COMMAND);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String message = "";
            String line = null;

            pr.waitFor();

            //Read the output
            try {
                while ((line = input.readLine()) != null)
                    message += line;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //If there is no output read the error log
            if (message == "") {
                //Read errors then
                message += "ERROR: ```";
                input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

                try {
                    while ((line = input.readLine()) != null)
                        message += line + '\n';
                } catch (IOException e) {
                    e.printStackTrace();
                }
                message += "```";
            }

            return message;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "An error has occurred";
    }
}
