package yt.bam.bamradio.command.sub;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.command.abstracts.ICommand;

public class CommandRandom implements ICommand {
	
    public void execute(CommandSender sender, String[] args) {
        if (BAMradio.getRadioManager().isNowPlaying()) {
            BAMradio.getRadioManager().stopPlaying();
        }
        BAMradio.getRadioManager().playRandomSong();
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_RANDOM_HELP");
    }
    
    public String getSyntax() {
        return "/br random";
    }
    
    public String getPermission() {
        return "bamradio.play";
    }
    
    public List<String> getName() {
        return Arrays.asList("random");
    }
    
    public String getExtendedHelp() {
        return null;
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
