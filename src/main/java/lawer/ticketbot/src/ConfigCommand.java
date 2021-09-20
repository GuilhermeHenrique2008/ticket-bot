package lawer.ticketbot.src;

import lawer.ticketbot.backend.Config;
import lawer.ticketbot.backend.Utilidades;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConfigCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Config.prefix + "config")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("list")) {

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        embedBuilder.setAuthor("🤖 Configurações");
                        embedBuilder.setDescription("prefix (str)\n" +
                                "logs (str)\n" +
                                "chatidlogs (str)\n" +
                                "ticketnumber (int)\n" +
                                "ticketmsgid (str)\n" +
                                "ticketcatid (str)\n" +
                                "ticketroleid (str)\n" +
                                "ticketdesc (str)\n" +
                                "msgerror (str)\n");
                        embedBuilder.setColor(Config.color);
                        embedBuilder.setThumbnail(event.getGuild().getIconUrl());

                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                        return;
                    }
                }
                if (args.length == 4) {
                    if (args[1].equalsIgnoreCase("get")) {
                        if (args[2].equalsIgnoreCase("str")) {
                            event.getChannel().sendMessage(Utilidades.sucess(event.getMember(), " Valor da config " + args[3] + " é " + Config.getconfigstring(args[3]))).queue();
                        }
                        if (args[2].equalsIgnoreCase("int")) {
                            event.getChannel().sendMessage(Utilidades.sucess(event.getMember(), " Valor da config " + args[3] + " é " + Config.getconfigint(args[3]))).queue();
                        }
                        return;
                    }
                }
                if (args.length <= 4) {
                    event.getChannel().sendMessage(Utilidades.error(Objects.requireNonNull(event.getMember()), "Use: " + Config.prefix + "config (set/get/list) (str/int) (config) (value)")).queue();
                } else {
                    if (args[1].equalsIgnoreCase("set")) {
                        if (args[2].equalsIgnoreCase("str")) {
                            Config.setconfigstring(args[3], args[4]);
                            event.getChannel().sendMessage(Utilidades.sucess(event.getMember(), " O valor da config " + args[3] + " foi alterado para " + args[4])).queue();
                            Utilidades.EnviarLog(event.getGuild(), event.getMember().getUser().getAsTag() + " alterou a config " + args[3] + " para " + args[4]);
                        }
                        if (args[2].equalsIgnoreCase("int")) {
                            Config.setconfigint(args[3], Integer.parseInt(args[4]));
                            event.getChannel().sendMessage(Utilidades.sucess(event.getMember(), " O valor da config " + args[3] + " foi alterado para " + Integer.parseInt(args[4]))).queue();
                            Utilidades.EnviarLog(event.getGuild(), event.getMember().getUser().getAsTag() + " alterou a config " + args[3] + " para " + Integer.parseInt(args[4]));
                        }
                    }
                }
            } else {
                event.getChannel().sendMessage(Utilidades.error(event.getMember(), Config.msgerror)).queue();
            }
        }
    }
}