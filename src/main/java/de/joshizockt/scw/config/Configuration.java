package de.joshizockt.scw.config;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public interface Configuration {

    String DEFAULT_PINGED_MESSAGE = "Server pinged successfully!";
    String DEFAULT_TIMEOUT_MESSAGE = "Server timed out! Probably offline!";
    int DEFAULT_PING_INTERVAL = 60;
    int DEFAULT_PORT = 25565;

    /**
     * The Name of the Server which is used to identify it. It is also used in the Webhook Message.
     * @return The Name of the Server or the host with port if none is set.
     */
    @Nullable
    default String name() {
        return host() + ":" + port();
    }

    /**
     * The Host of the Server which is used to ping it.
     * Can be an IP Address or a Domain.
     * @return The Host of the Server
     */
    String host();

    /**
     * The Port of the Server which is used to ping it.
     * Default is 25565.
     * @return The Port of the Server
     */
    default int port() {
        return DEFAULT_PORT;
    }

    /**
     * The Webhook URL which is used to send the Message to Discord.
     * If not valid, will output an error.
     * @return The Webhook URL
     */
    String webhook();

    /**
     * The Interval in seconds in which the Server is pinged.
     * @return The Interval in which the Server is pinged
     */
    default int pingInterval() {
        return 60;
    }

    /**
     * The Message which is sent to Discord if the Server is pinged successfully and the last ping was not successful.
     * @return The Message which is sent to Discord if the Server is pinged successfully
     */
    default String pingedMessage() {
        return DEFAULT_PINGED_MESSAGE;
    }

    /**
     * The Message which is sent to Discord if the Server timed out and the last ping was successful.
     * @return
     */
    default String timeoutMessage() {
        return DEFAULT_TIMEOUT_MESSAGE;
    }

    /**
     * The result of the last ping.
     * @return The result of the last ping or null if no ping was done yet.
     */
    JsonObject lastPing();

    void setLastPing(JsonObject ping);

}
