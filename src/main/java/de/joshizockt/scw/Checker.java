package de.joshizockt.scw;

import com.google.gson.JsonObject;
import de.joshizockt.scw.config.Configuration;

import java.awt.*;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class Checker {

    private final Configuration configuration;
    private Timer timer;
    private JsonObject last = null;

    public Checker(Configuration config) {
        this.configuration = config;
    }

    public void ping() {

        System.out.println("********************");
        System.out.println("Pinging " + configuration.name() + "...");
        JsonObject response = Ping.ping(configuration.host(), configuration.port());

        JsonObject last;
        if(this.last != null) last = this.last;
        else last = configuration.lastPing();
        // update the config with the last ping
        configuration.setLastPing(response);
        this.last = response;

        System.out.println("Response: " + response.toString());

        if(last.get("success").getAsBoolean() != response.get("success").getAsBoolean()) {
            System.out.println("Detected a change! Sending message..");

            String desc = "**Server:** " + configuration.name() + "\n" +
                    "**Host:** " + configuration.host() + "\n" +
                    "**Port:** " + configuration.port() + "\n" +
                    "**Status:** " + (response.get("success").getAsBoolean() ? "Online" : "Offline") + "\n" +
                    "**Ping:** " + response.get("ping").getAsLong() + "ms";
            String footer = "pinged with " + response.get("ping").getAsInt() + "ms";
            if(response.get("success").getAsBoolean()) {
                // server is online
                DiscordWebhook.send(configuration.webhook(), configuration.pingedMessage(), Color.GREEN, desc, footer);
                System.out.println("Sent (online)!");
            } else {
                // server is offline
                DiscordWebhook.send(configuration.webhook(), configuration.timeoutMessage(), Color.RED, desc, footer);
                System.out.println("Sent (offline)!");
            }

        }

        System.out.println("********************");
        System.out.println(" ");

    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        }, 1000, configuration.pingInterval() * 1000L);
    }

    public void stop() {

    }

}
