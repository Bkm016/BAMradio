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

public class CommandUnmute implements ICommand {
    
    public void execute(CommandSender sender, String[] args) {
        BAMradio.getRadioManager().tuneOut((Player)sender);
        BAMradio.getRadioManager().tuneIn((Player)sender);
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_UNMUTE_HELP");
    }
    
    public String getSyntax() {
        return "/br unmute";
    }
    
    public String getPermission() {
        return "bamradio.mute";
    }
    
    public List<String> getName() {
        return Arrays.asList("unmute");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return false;
    }
}
