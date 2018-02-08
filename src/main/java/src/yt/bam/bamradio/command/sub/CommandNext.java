package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;

public class CommandNext implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        if (BAMradio.getRadioManager().isNowPlaying()) {
            BAMradio.getRadioManager().stopPlaying();
        }
        BAMradio.getRadioManager().playNextSong();
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_NEXT_HELP");
    }
    
    public String getSyntax() {
        return "/br next";
    }
    
    public String getPermission() {
        return "bamradio.next";
    }
    
    public List<String> getName() {
        return Arrays.asList("next");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
