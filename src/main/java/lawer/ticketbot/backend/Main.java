package lawer.ticketbot.backend;

import lawer.ticketbot.src.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        Config.LoadConfig();
        jda = JDABuilder.createDefault(Config.tokenbot).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        jda.addEventListener(new TicketCommand());
        jda.addEventListener(new ConfigCommand());
        jda.addEventListener(new ReloadCommand());
    }
}