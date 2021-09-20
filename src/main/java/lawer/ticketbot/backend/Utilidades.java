package lawer.ticketbot.backend;

import net.dv8tion.jda.api.entities.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades {

    public static String gethora() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("kk:mm");
        return format.format(date);
    }

    public static String error(Member member, String msg) {
        return member.getAsMention() + " " + msg;
    }

    public static String sucess(Member member, String msg) {
        return member.getAsMention() + " " + msg;
    }

    public static void EnviarLog(Guild guild, String msg) {
        System.out.println("[LOGS] " + msg);

        if (Config.logs) {
            TextChannel text = guild.getTextChannelById(Config.idchatlogs);
            assert text != null;
            text.sendMessage("[LOGS] " + msg).queue();
        }
    }
}