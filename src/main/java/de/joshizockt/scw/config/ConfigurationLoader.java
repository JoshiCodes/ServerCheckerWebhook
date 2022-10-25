package de.joshizockt.scw.config;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigurationLoader {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Configuration> loadConfigurations(String filename) {
        List<Configuration> list = new ArrayList<>();

        File file = new File(filename);
        if(!file.exists()) {
            try {
                file.createNewFile();
                writeToFile(file, exampleConfig());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String content = null;
        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNext()) {
                if(content == null) content = "";
                content += "\n" + scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JsonElement e = JsonParser.parseString(content);
        if(e.isJsonArray()) {
            for (JsonElement element : e.getAsJsonArray()) {
                if(element.isJsonObject()) {
                    JsonObject o = element.getAsJsonObject();

                    String name = o.has("name") ? o.get("name").getAsString() : null;
                    String host = o.get("host").getAsString();
                    int port = o.has("port") ? o.get("port").getAsInt() : Configuration.DEFAULT_PORT;
                    String webhook = o.get("webhook").getAsString();
                    int pingInterval = o.has("pingInterval") ? o.get("pingInterval").getAsInt() : Configuration.DEFAULT_PING_INTERVAL;
                    String pingedMessage = o.has("pingedMessage") ? o.get("pingedMessage").getAsString() : Configuration.DEFAULT_PINGED_MESSAGE;
                    String timeoutMessage = o.has("timeoutMessage") ? o.get("timeoutMessage").getAsString() : Configuration.DEFAULT_TIMEOUT_MESSAGE;

                    JsonObject lastPing = o.has("lastPing") ? o.get("lastPing").getAsJsonObject() : null;

                    list.add(new Configuration() {

                        @Override
                        public @Nullable String name() {
                            if(name != null) return name;
                            return Configuration.super.name();
                        }

                        @Override
                        public String host() {
                            return host;
                        }

                        @Override
                        public int port() {
                            return port;
                        }

                        @Override
                        public String webhook() {
                            return webhook;
                        }

                        @Override
                        public int pingInterval() {
                            return pingInterval;
                        }

                        @Override
                        public String pingedMessage() {
                            return pingedMessage;
                        }

                        @Override
                        public String timeoutMessage() {
                            return timeoutMessage;
                        }

                        @Override
                        public JsonObject lastPing() {
                            return lastPing;
                        }

                        @Override
                        public void setLastPing(JsonObject ping) {
                            o.add("lastPing", ping);
                            writeToFile(file, gson.toJson(e));
                        }

                    });
                }
            }
        }

        System.out.println("Loaded a ConfigurationList with " + list.size() + " Configurations.");

        return list;
    }

    private static String exampleConfig() {
        JsonObject o = new JsonObject();
        o.addProperty("name", "someName");
        o.addProperty("host", "somehost.net");
        o.addProperty("port", 25565);
        o.addProperty("_port", "# Port can be not set, default is 25565");
        o.addProperty("webhook", "https://discord.com/api/webhooks/...");
        o.addProperty("pingedMessage", "Server pinged successfully!");
        o.addProperty("timeoutMessage", "Server timed out! Probably offline!");
        o.addProperty("_messages", "The pingedMessage and timeoutMessage do not have to be set. The default messages will be used if no one is found.");
        o.addProperty("pingInterval", 60);
        o.addProperty("_pingInterval", "The pingInterval is in seconds. Default is 60 (1 minute).");
        //o.add("lastPing", new JsonObject());  -  Is set automatically
        JsonArray array = new JsonArray();
        array.add(o);
        return gson.toJson(array);
    }

    private static void writeToFile(File file, String str) {
        try {
            Files.write(file.toPath(), str.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
