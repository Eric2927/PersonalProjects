package RNG;

import java.util.Random;

import general.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RNGCommands extends ListenerAdapter {

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		Random RNG = new Random();
		
		if (args[0].equalsIgnoreCase(Bot.prefix + "coinflip")) {
			// True is heads, False is tails
			if (RNG.nextBoolean()) {
				event.getChannel().sendMessage("It was heads!").queue();
			} else {
				event.getChannel().sendMessage("It was tails!").queue();
			}
		}
		
		// pick command
		// pick x out of y choices randomly
		else if (args[0].equalsIgnoreCase(Bot.prefix + "pick")) {
			try {
				int totalChoices;
				switch (args.length) {
				// If only one extra argument presented, then pick 1 out of y
				case 2:
					totalChoices = Integer.parseInt(args[1]);
					int numChosen = RNG.nextInt(totalChoices) + 1;
					event.getChannel().sendMessage(Integer.toString(numChosen)).queue();
					break;
				case 3:
					totalChoices = Integer.parseInt(args[2]);
					int numToChoose = Integer.parseInt(args[1]);
					if (numToChoose <= totalChoices && numToChoose >= 0) {
						String chosenNumbers = "";
						for (int i = 0; i < numToChoose; i ++) {
							chosenNumbers += RNG.nextInt(totalChoices) + 1;
							chosenNumbers += " ";
						}
						event.getChannel().sendMessage(chosenNumbers).queue();
					}
					
					break;
				default:
					EmbedBuilder invalidParamNumError = new EmbedBuilder();
					invalidParamNumError.setColor(Bot.errorColor);
					invalidParamNumError.setTitle("Invalid number of parameters.");
					invalidParamNumError.setDescription("Command syntax: " + Bot.prefix + "pick {num to pick} [num total]");
					event.getChannel().sendMessage(invalidParamNumError.build()).queue((embed) -> invalidParamNumError.clear());
					break;
				}
			} catch (NumberFormatException e) {
				
			}
		}
	}
}
