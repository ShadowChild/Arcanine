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

public class CommandTime extends AbstractCommand {

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        String[] args = event.getMessage().getContentRaw().split(" ");
        try {

            String time = args[1];
            String source = args[2].toUpperCase();
            String dest = args[3].toUpperCase();

            // We have to use an API because java does not like abbreviated time zones
            HttpResponse<JsonNode> sourceTimeZone = Unirest.get("http://api.timezonedb.com/v2.1/get-time-zone")
                    .header("accept", "application.json")
                    .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
                    .queryString("format", "json")
                    .queryString("by", "zone")
                    .queryString("zone", source)
                    .asJson();

//            TimeUnit.SECONDS.sleep(1);

            HttpResponse<JsonNode> destTimeZone = Unirest.get("http://api.timezonedb.com/v2.1/get-time-zone")
                    .header("accept", "application.json")
                    .queryString("key", Arcanine.LOADER.botCfg.tzdb_token)
                    .queryString("format", "json")
                    .queryString("by", "zone")
                    .queryString("zone", dest)
                    .asJson();

            if (sourceTimeZone.isSuccess() && destTimeZone.isSuccess()) {

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
                String sourceTime = sourceZoned.format(DateTimeFormatter.ofPattern("HH:mma"));
                long epoch = sourceZoned.toEpochSecond();

                ZoneId destId = ZoneId.of(destZoneName);
                ZonedDateTime zdt = Instant.ofEpochSecond(epoch).atZone(destId);
                String newTime = zdt.format(DateTimeFormatter.ofPattern("HH:mma"));
                // Instead of using the pattern "HH:mma z" we put the time zone in here, because that is what the user specified
                channel.sendMessage("```" + sourceTime + " " + source + " -> " + newTime + " " + dest + "```").queue();
            }
        } catch(ArrayIndexOutOfBoundsException e) {

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
