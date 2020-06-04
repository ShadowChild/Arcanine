package io.github.shadowchild.arcanine.util;

import discord4j.core.GatewayDiscordClient;
import io.github.shadowchild.arcanine.Arcanine;

import java.net.URL;

public class Resources {

    public final URL JAR_FILE = Arcanine.class.getProtectionDomain().getCodeSource().getLocation();

    public final Loader loader = new Loader("assets");

    public GatewayDiscordClient client;

    public final String prefix = ">";

    public String accessToken = null;
    public boolean hasGui = false;
}
