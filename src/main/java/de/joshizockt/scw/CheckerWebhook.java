package de.joshizockt.scw;

import de.joshizockt.scw.config.Configuration;
import de.joshizockt.scw.config.ConfigurationLoader;

import java.util.HashMap;
import java.util.List;

public class CheckerWebhook {

    private static HashMap<String, Checker> checkerThreads;

    public static void main(String[] args) {

        // Load Filename from args or set to "config.json" as default
        String filename = getArgument("config", args);
        if(filename == null) filename = "config.json";
        else if(!filename.contains(".")) filename = filename + ".json";

        // Load every Configuration in the File
        List<Configuration> configurations = ConfigurationLoader.loadConfigurations(filename);
        if(configurations.size() < 1) {
            System.err.println("Cannot start without any Configuration(s)! Abort.");
            System.exit(1);
            return;
        }

        checkerThreads = new HashMap<>();

        for (Configuration config : configurations) {
            Checker checker = new Checker(config);
            checker.start();
            checkerThreads.put(config.name(), checker);
        }

    }

    private static String getArgument(String target, String[] args) {
        if(!target.startsWith("--")) target = "--" + target + "=";
        for(String arg : args) {
            if(arg.startsWith(target)) {
                return arg.replaceFirst(target, "");
            }
        }
        return null;
    }

}
