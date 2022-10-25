package de.joshizockt.scw;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;

public class DiscordWebhook {

    private static HashMap<String, WebhookClient> cache;

    public static void send(String webhook, String message, Color color, String desc, String footer) {

        if(cache == null) cache = new HashMap<>();

        WebhookClient client;
        if(cache.containsKey(webhook)) {
            client = cache.get(webhook);
        } else {
            client = WebhookClient.withUrl(webhook);
        }

        WebhookEmbed embed = new WebhookEmbedBuilder()
                .setColor(color.getRGB())
                .setTitle(new WebhookEmbed.EmbedTitle(message, null))
                .setDescription(desc)
                .setFooter(new WebhookEmbed.EmbedFooter(footer, null))
                .setTimestamp(new Date().toInstant())
                .build();

        client.send(embed);

    }

}
