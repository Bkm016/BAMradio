package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import java.util.logging.*;
import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;
import org.bukkit.permissions.*;
import org.bukkit.*;

public class CommandStop implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        if (BAMradio.getRadioManager().isNowPlaying()) {
            BAMradio.getRadioManager().stopPlaying();
            sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_STOP_MESSAGE"));
        }
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_STOP_HELP");
    }
    
    public String getSyntax() {
        return "/br stop";
    }
    
    public String getPermission() {
        return "bamradio.stop";
    }
    
    public List<String> getName() {
        return Arrays.asList("stop");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
