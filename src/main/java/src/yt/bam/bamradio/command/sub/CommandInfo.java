package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;

public class CommandInfo implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        if (BAMradio.getRadioManager().isNowPlaying()) {
            BAMradio.getRadioManager().nowPlaying(sender, true);
        }
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_INFO_HELP");
    }
    
    public String getSyntax() {
        return "/br info";
    }
    
    public String getPermission() {
        return null;
    }
    
    public List<String> getName() {
        return Arrays.asList("info");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
