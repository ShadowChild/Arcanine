package me.shadowchild.arcanine.command;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import me.shadowchild.arcanine.Arcanine;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CommandTime extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        try {

            String time = args[1];
            String source = args[2].toUpperCase();
            String dest = args[3].toUpperCase();

            HttpResponse<JsonNode> sourceTimeZone = Unirest.get("http://api.timezonedb.com/v2.1/get-time-zone")
                    .header("accept", "application.json")
                    .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
                    .queryString("format", "json")
                    .queryString("by", "zone")
                    .queryString("zone", source)
                    .asJson();

            TimeUnit.SECONDS.sleep(1);

            HttpResponse<JsonNode> destTimeZone = Unirest.get("http://api.timezonedb.com/v2.1/get-time-zone")
                    .header("accept", "application.json")
                    .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
                    .queryString("format", "json")
                    .queryString("by", "zone")
                    .queryString("zone", dest)
                    .asJson();

            if(sourceTimeZone.isSuccess() && destTimeZone.isSuccess()) {

                JSONObject sourceObj = sourceTimeZone.getBody().getObject();
                System.out.println(sourceTimeZone.getBody().toString());
                String sourceZoneName = sourceObj.getString("zoneName");

                JSONObject destObj = destTimeZone.getBody().getObject();
                System.out.println(destTimeZone.getBody().toString());
                String destZoneName = destObj.getString("zoneName");

                System.out.println(sourceZoneName);
                ZoneId id = ZoneId.of(sourceZoneName);
                LocalDateTime sourceLocal = LocalDateTime.of(LocalDate.now(id), LocalTime.parse(time));
                ZonedDateTime sourceZoned = ZonedDateTime.of(sourceLocal, id);
                System.out.println(sourceZoned.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mma z")));
                long epoch = sourceZoned.toEpochSecond();

                ZoneId destId = ZoneId.of(destZoneName);

                HttpResponse<JsonNode> response = Unirest.get("http://api.timezonedb.com/v2.1/convert-time-zone")
                        .header("accept", "application/json")
                        .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
                        .queryString("format", "json")
                        .queryString("from", source)
                        .queryString("to", dest)
                        .queryString("time", epoch)
                        .asJson();

                TimeUnit.SECONDS.sleep(1);

                if (response.isSuccess()) {


                    System.out.println(response.getBody().toString());
                    JSONObject node = response.getBody().getObject();
                    if (node.getString("status").equals("OK")) {

                        System.out.println(node.getString("toZoneName"));
                        System.out.println(node.getString("offset"));
                        System.out.println(destId.getDisplayName(TextStyle.FULL, Locale.ROOT));
                        long newEpoch = node.getLong("toTimestamp");
                        ZonedDateTime zdt = Instant.ofEpochSecond(newEpoch).atZone(destId);
                        System.out.println(zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mma")));
                    }
                }
            }

//            LocalDateTime localTime = LocalDateTime.of(LocalDate.now(ZoneId.of("GMT")), LocalTime.parse(time));
//            ZonedDateTime zonedDateTime = ZonedDateTime.of(localTime, ZoneId.of("GMT"));
//            System.out.println(zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mma z")));
//            long epoch = zonedDateTime.toEpochSecond();
//
//            HttpResponse<JsonNode> response = Unirest.get("http://api.timezonedb.com/v2.1/convert-time-zone")
//                    .header("accept", "application/json")
//                    .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
//                    .queryString("format", "json")
//                    .queryString("from", source)
//                    .queryString("to", dest)
//                    .queryString("time", epoch)
//                    .asJson();
//
//            if(response.isSuccess()) {
//
//                JSONObject node = response.getBody().getObject();
//                if(node.getString("status").equals("OK")) {
//
//                    System.out.println(node.getString("fromZoneName"));
//                    System.out.println(node.getString("offset"));
//                    long newEpoch = node.getLong("toTimestamp");
//                    ZonedDateTime zdt = Instant.ofEpochSecond(newEpoch).atZone(ZoneId.of("GMT"));
//                    System.out.println(zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mma")));
//                }
//            }
        } catch(ArrayIndexOutOfBoundsException | InterruptedException e) {

            e.printStackTrace();
            channel.sendMessage("The time command was used incorrectly, please see usage using the help command and try again.").queue();
        }
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

    }
}
