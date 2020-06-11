package me.shadowchild.arcanine.command.champs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.shadowchild.arcanine.command.AbstractCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CommandChamps extends AbstractCommand {

    LoadingCache<String, ArrayList<WrestlerStat>> cache;

    public CommandChamps() {

        cache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(

            new CacheLoader<String, ArrayList<WrestlerStat>>() {

                @Override
                public ArrayList<WrestlerStat> load(String key) throws Exception {

                    return getChampsForPromotion(key);
                }
        });
        Thread t = new Thread(() -> {

            cache.refresh("wwe");
            cache.refresh("aew");
            cache.refresh("njpw");
            cache.refresh("roh");
        });
        t.start();
    }

    @Override
    public void onMessage(MessageReceivedEvent event, MessageChannel channel, User sender, String alias) {

        try {

            switch (alias) {

                case "wwe":
                    channel.sendMessage(showChampions("wwe", channel).setColor(Color.BLACK).build()).queue();
                    break;
                case "aew":
                    break;
                case "njpw":
                    break;
                case "roh":
                    break;
                case "champs": {

                    MessageBuilder builder = new MessageBuilder();
                    builder.append(sender);
                    builder.append(", you have incorrectly used this command, here is how you should use it: \n");
                    builder.appendCodeLine(getUsage());
                    builder.sendTo(channel).queue();
                    break;
                }
            }
        } catch(ExecutionException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onReactionAdd(MessageReactionAddEvent event, MessageChannel channel, User sender) {

    }

    @Override
    public void onShutdown(JDA client) {

        cache.invalidateAll();
    }

    private EmbedBuilder showChampions(String promotion, MessageChannel channel) throws ExecutionException {

        ArrayList<WrestlerStat> list = cache.get(promotion);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(promotion.toUpperCase() + " Champions");
//        builder.setColor(Color.BLACK);
        list.forEach((ws) -> {

            builder.addField(ws.title, ws.name, true);
        });
//        channel.sendMessage(builder.build()).queue();
        return builder;
    }

    public ArrayList<WrestlerStat> getChampsForPromotion(String promotion) {

        ArrayList<WrestlerStat> ret = new ArrayList<>();

        Document doc;
        switch(promotion) {

            case "wwe": {

                try {

                    doc = Jsoup.connect("https://www.wwe.com/superstars").get();
                    Element content = doc.getElementById("content");
                    Elements classes = doc.getElementsByClass("superstars-champions--info");
                    classes.forEach((e) -> {

                        String superstarName = e.child(0).ownText();
                        String championship = e.child(1).ownText();
                        ret.add(new WrestlerStat(superstarName, championship));
                    });
                } catch (IOException e) {

                    e.printStackTrace();
                }
                break;
            }
            default: break;
        }
        return ret;
    }

    public void clearCache() {

        cache.invalidateAll();
    }
}
