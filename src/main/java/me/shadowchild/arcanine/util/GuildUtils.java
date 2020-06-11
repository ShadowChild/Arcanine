package me.shadowchild.arcanine.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.atomic.AtomicBoolean;

public class GuildUtils {

    public static boolean isUserAdmin(Guild guild, User user) {

        // Atomic for using in lambda
        AtomicBoolean isAdmin = new AtomicBoolean(false);

        guild.getMember(user).getRoles().forEach(role -> {

            if(role.hasPermission(Permission.ADMINISTRATOR)) {

                isAdmin.set(true);
            }
        });

        return isAdmin.get();
    }
}
