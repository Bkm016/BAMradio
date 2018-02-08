package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;
import yt.bam.bamradio.radiomanager.*;
import org.bukkit.permissions.*;
import org.bukkit.*;

public class CommandList implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(BAMradio.getPrefix() + ChatColor.GREEN + BAMradio.getLanguage().get("COMMAND_LIST_TITLE"));
        String[] fileList = RadioManager.listRadioFilesWithExtensions();
        int i = 0;
        for (String name : fileList) {
            String suffix = "";
            if (name.contains(".mid")) {
                name = name.substring(0, name.lastIndexOf(".mid"));
                suffix = ChatColor.DARK_BLUE + "MID";
            }
            if (name.contains(".nbs")) {
                name = name.substring(0, name.lastIndexOf(".nbs"));
                suffix = ChatColor.DARK_GREEN + "NBS";
            }
            sender.sendMessage(ChatColor.GREEN + "[" + ChatColor.BOLD + i + ChatColor.RESET + "" + ChatColor.GREEN + "] " + suffix + " " + ChatColor.RESET + name);
            ++i;
        }
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_LIST_HELP");
    }
    
    public String getSyntax() {
        return "/br list";
    }
    
    public String getPermission() {
        return "bamradio.list";
    }
    
    public List<String> getName() {
        return Arrays.asList("list");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
