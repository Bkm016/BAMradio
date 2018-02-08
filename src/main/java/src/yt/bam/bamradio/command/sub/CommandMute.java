package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;

import org.bukkit.entity.*;
import org.bukkit.permissions.*;
import org.bukkit.*;

public class CommandMute implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        BAMradio.getRadioManager().tuneOut((Player)sender);
        sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MUTE_MESSAGE"));
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_MUTE_HELP");
    }
    
    public String getSyntax() {
        return "/br mute";
    }
    
    public String getPermission() {
        return "bamradio.mute";
    }
    
    public List<String> getName() {
        return Arrays.asList("mute");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return false;
    }
}
