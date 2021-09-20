package lawer.ticketbot.src;

import lawer.ticketbot.backend.Config;
import lawer.ticketbot.backend.Utilidades;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReloadCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Config.prefix + "reload")) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
                Config.configset();
                event.getChannel().sendMessage(Utilidades.sucess(event.getMember(), "As configurações foram atualizadas com sucesso!")).queue();
            } else {
                event.getChannel().sendMessage(Utilidades.error(event.getMember(), Config.msgerror)).queue();
            }
        }
    }
}