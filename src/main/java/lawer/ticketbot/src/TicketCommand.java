package lawer.ticketbot.src;

import lawer.ticketbot.backend.Config;
import lawer.ticketbot.utils.TicketManager;
import lawer.ticketbot.backend.Utilidades;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TicketCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Config.prefix + "ticket")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                event.getMessage().delete().queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("📫 Central de Atendimento");
                embedBuilder.setDescription(Config.ticketdesc);
                embedBuilder.setColor(Config.color);

                event.getChannel().sendMessage(embedBuilder.build()).queue(message -> {
                    message.addReaction("📨").queue();
                    TicketManager.ticketmsgid.put("ticket", message.getId());
                    Config.setconfigstring("ticketmsgid", message.getId());
                });
            } else {
                event.getMessage().reply(Config.msgerror).queue();
            }
        }
        if (args[0].equalsIgnoreCase(Config.prefix + "close")) {
            if (TicketManager.tickets.containsKey(event.getChannel().getId())) {
                event.getMessage().delete().queue();
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("📫 Central de Atendimento");
                embedBuilder.setDescription("> Deseja fecha o ticket?");
                embedBuilder.setColor(Config.color);
                event.getChannel().sendMessage(embedBuilder.build()).queue(message -> {
                    message.addReaction("✅").queue();
                    message.addReaction("❎").queue();
                    TicketManager.closemsg.put(message.getId(), "1");
                });
            }
        }
        if (args[0].equalsIgnoreCase(Config.prefix + "ticketadd")) {
            if (TicketManager.tickets.containsKey(event.getChannel().getId())) {
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_READ)) {
                    if (args.length > 1) {
                        event.getChannel().createPermissionOverride(Objects.requireNonNull(event.getGuild().getMemberById(args[1]))).setAllow(
                                Permission.MESSAGE_WRITE,
                                Permission.MESSAGE_READ,
                                Permission.MESSAGE_HISTORY,
                                Permission.MESSAGE_EMBED_LINKS,
                                Permission.MESSAGE_ATTACH_FILES,
                                Permission.MESSAGE_ADD_REACTION,
                                Permission.MESSAGE_EXT_EMOJI
                        ).queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        embedBuilder.setAuthor("📫 Ticket");
                        embedBuilder.setDescription("> O usuário: <@" + args[1] + "> foi adicionado ao chat.");
                        embedBuilder.setColor(Config.color);

                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                    } else {
                        event.getChannel().sendMessage(Utilidades.error(event.getMember(), "Use: " + Config.prefix + "ticketadd (discordid)")).queue();
                    }
                } else {
                    event.getChannel().sendMessage(Utilidades.error(event.getMember(), Config.msgerror)).queue();
                }
            }
        }
        if (args[0].equalsIgnoreCase(Config.prefix + "ticketremove")) {
            if (TicketManager.tickets.containsKey(event.getChannel().getId())) {
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_READ)) {
                    if (args.length > 1) {
                        event.getChannel().putPermissionOverride(Objects.requireNonNull(event.getGuild().getMemberById(args[1]))).reset().queue();

                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        embedBuilder.setAuthor("📫 Ticket");
                        embedBuilder.setDescription("> O usuário: <@" + args[1] + "> foi removido do chat.");
                        embedBuilder.setColor(Config.color);

                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                    } else {
                        event.getChannel().sendMessage(Utilidades.error(event.getMember(), "Use: " + Config.prefix + "ticketremove (discordid)")).queue();
                    }
                } else {
                    event.getChannel().sendMessage(Utilidades.error(event.getMember(), Config.msgerror)).queue();
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (!event.getUser().isBot()) {
            if (TicketManager.ticketmsgid.get("ticket") != null) {
                if (event.getReaction().getMessageId().equals(TicketManager.ticketmsgid.get("ticket"))) {
                    event.getReaction().removeReaction(event.getUser()).queue();
                    if (TicketManager.isticket.contains(event.getMember().getId())) {
                        return;
                    }
                    if (event.getReactionEmote().getName().equals("📨")) {
                        TicketManager.CriarTicket(event.getGuild(), event.getMember(), "📨ticket");
                    }
                }
                if (TicketManager.tickets.containsKey(Objects.requireNonNull(event.getReaction().getTextChannel()).getId())) {
                    if (TicketManager.closemsg.containsKey(event.getReaction().getMessageId())) {
                        if (event.getReactionEmote().getName().equals("❌")) {

                            Utilidades.EnviarLog(event.getGuild(), event.getMember().getUser().getAsTag() + " deletou o " + event.getReaction().getTextChannel().getName() + " criado pelo usuário: <@" + TicketManager.tickets.get(Objects.requireNonNull(event.getReaction().getTextChannel()).getId()) + ">");

                            event.getReaction().clearReactions().queue();
                            TextChannel textChannel = event.getReaction().getTextChannel();
                            TicketManager.DeletarTicket(textChannel);
                        }
                        if (event.getReactionEmote().getName().equals("✅")) {

                            Utilidades.EnviarLog(event.getGuild(), event.getMember().getUser().getAsTag() + " deletou o " + event.getReaction().getTextChannel().getName() + " criado pelo usuário: <@" + TicketManager.tickets.get(Objects.requireNonNull(event.getReaction().getTextChannel()).getId()) + ">");

                            event.getChannel().deleteMessageById(event.getReaction().getMessageId()).queue();
                            TextChannel textChannel = event.getReaction().getTextChannel();
                            TicketManager.DeletarTicket(textChannel);
                        }
                        if (event.getReactionEmote().getName().equals("❎")) {
                            event.getChannel().deleteMessageById(event.getReaction().getMessageId()).queue();
                        }
                    }
                }
            }
        }
    }
}