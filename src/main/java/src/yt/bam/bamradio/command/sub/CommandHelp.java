package yt.bam.bamradio.command.sub;

import org.bukkit.command.*;

import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.command.abstracts.ICommand;
import java.util.*;
import org.bukkit.*;

public class CommandHelp implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "#####################################################");
        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + BAMradio.getInst().getName() + " " + BAMradio.getInst().getDescription().getVersion() + " by " + BAMradio.getInst().getDescription().getAuthors());
        for (ICommand cmd : BAMradio.getCommands()) {
            if (cmd.getPermission() == null || sender.hasPermission(cmd.getPermission())) {
                sender.sendMessage(ChatColor.WHITE + cmd.getSyntax() + " - " + cmd.getHelp());
            }
        }
        sender.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "#####################################################");
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_HELP_HELP");
    }
    
    public String getSyntax() {
        return "/br help";
    }
    
    public String getPermission() {
        return null;
    }
    
    public List<String> getName() {
        return Arrays.asList("help");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
