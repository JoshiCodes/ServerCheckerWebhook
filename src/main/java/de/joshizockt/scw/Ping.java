package de.joshizockt.scw;

import com.google.gson.JsonObject;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Ping {
    private static final String SRV_QUERY_PREFIX = "_minecraft._tcp.%s";

    public static JsonObject ping(String host, int port) {
        JsonObject o = new JsonObject();
        try {

            Record[] records = new Lookup(String.format(SRV_QUERY_PREFIX, host), Type.SRV).run();

            if (records != null) {

                for (Record record : records) {
                    SRVRecord srv = (SRVRecord) record;

                    host = srv.getTarget().toString().replaceFirst("\\.$", "");
                    port = srv.getPort();
                }

            }
        } catch (TextParseException e) {
            e.printStackTrace();
        }

        long ping = 0;
        boolean success = false;
        try (final Socket socket = new Socket()) {
            long start = System.currentTimeMillis();
            socket.connect(new InetSocketAddress(host, port));
            ping = System.currentTimeMillis() - start;
            success = true;
        } catch (IOException e) {
            ping = -1;
            success = false;
        }

        o.addProperty("success", success);
        o.addProperty("ping", ping);

        return o;

    }

}
