package lawer.ticketbot.utils;

import lawer.ticketbot.backend.Config;
import lawer.ticketbot.backend.Utilidades;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TicketManager {

    public static HashMap<String, String> ticketmsgid = new HashMap<>();
    public static HashMap<String, String> tickets = new HashMap<>();
    public static HashMap<String, String> closemsg = new HashMap<>();
    public static ArrayList<String> isticket = new ArrayList<>();
    public static int ticketid = Config.getconfigint("ticketnumber");

    public static void CriarTicket(Guild guild, Member member, String emoj) {

        ticketid = ticketid + 1;
        Config.setconfigint("ticketnumber", ticketid);

        TextChannel suport;
        suport = Objects.requireNonNull(guild.getCategoryById(Config.ticketcatid)).createTextChannel(emoj + "・" + ticketid).complete();

        suport.putPermissionOverride(guild.getPublicRole()).setDeny(new Permission[] {
                Permission.MESSAGE_READ
        }).queue();

        suport.createPermissionOverride(member).setAllow(new Permission[] {
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI
        }).queue();

        suport.putPermissionOverride((IPermissionHolder) Objects.requireNonNull(guild.getRoleById(Config.ticketrole))).setAllow(new Permission[] {
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_READ,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_EMBED_LINKS,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI
        }).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("📫 Ticket");
        embedBuilder.setDescription("> Olá " + member.getUser().getAsMention() + " 👋, conte-nos sua dúvida e em breve alguém da equipe entrará em contato com você.\n\nClique no ❌ para fechar o ticket.");
        embedBuilder.setColor(Config.color);
        embedBuilder.setThumbnail(guild.getIconUrl());

        suport.sendMessage(embedBuilder.build()).queue(message -> {
            message.addReaction("❌").queue();
            closemsg.put(message.getId(), "1");
        });
        tickets.put(suport.getId(), member.getId());
        isticket.add(member.getId());

        Utilidades.EnviarLog(guild, member.getUser().getAsMention() + " criou o ticket: " + ticketid);
    }

    public static void DeletarTicket(TextChannel textChannel) {
        isticket.remove(tickets.get(textChannel.getId()));
        tickets.remove(textChannel.getId());
        textChannel.sendMessage("O ticket será finalizado em **5 segundos**.").queue();
        textChannel.delete().queueAfter(5, TimeUnit.SECONDS);
    }
}