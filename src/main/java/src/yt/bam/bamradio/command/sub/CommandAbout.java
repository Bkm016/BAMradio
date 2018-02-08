package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import org.bukkit.command.*;

import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.command.abstracts.ICommand;
import org.bukkit.permissions.*;
import org.bukkit.*;

public class CommandAbout implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(BAMradio.getPrefix() + ChatColor.GREEN + BAMradio.getInst().getName() + " " + ChatColor.WHITE + BAMradio.getLanguage().get("COMMAND_ABOUT_BY") + ChatColor.GREEN + " " + BAMradio.getInst().getDescription().getAuthors());
        sender.sendMessage(BAMradio.getPrefix() + ChatColor.GREEN + "Proudly presenting BAMcraft (bam.yt)");
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_ABOUT_HELP");
    }
    
    public String getSyntax() {
        return "/br about";
    }
    
    public String getPermission() {
        return null;
    }
    
    public List<String> getName() {
        return Arrays.asList("about");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
