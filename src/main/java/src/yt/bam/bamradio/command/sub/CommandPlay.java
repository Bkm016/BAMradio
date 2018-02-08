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

public class CommandPlay implements ICommand {
    
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length == 1) {
                new CommandList().execute(sender, args);
            }
            else {
                if (BAMradio.getRadioManager().isNowPlaying()) {
                    BAMradio.getRadioManager().stopPlaying();
                }
                if (isInteger(args[1])) {
                    int index = Integer.parseInt(args[1]);
                    String[] fileList = RadioManager.listRadioFiles();
                    if (index < fileList.length) {
                        BAMradio.getRadioManager().playSong(fileList[index]);
                    }
                    else {
                        sender.sendMessage(BAMradio.getPrefix() + ChatColor.RED + BAMradio.getLanguage().get("COMMAND_PLAY_EXCEPTION_NOT_FOUND") + " \"" + args[1] + "\"");
                    }
                }
                else if (!BAMradio.getRadioManager().playSong(args[1])) {
                    sender.sendMessage(BAMradio.getPrefix() + ChatColor.RED + BAMradio.getLanguage().get("COMMAND_PLAY_EXCEPTION_NOT_FOUND") + " \"" + args[1] + "\"");
                }
            }
        }
        catch (Exception e) {
            sender.sendMessage(BAMradio.getPrefix() + ChatColor.RED + e.getMessage());
        }
    }
    
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public String getHelp() {
        return BAMradio.getLanguage().get("COMMAND_PLAY_HELP");
    }
    
    public String getSyntax() {
        return "/br play <name|index>";
    }
    
    public String getPermission() {
        return "bamradio.play";
    }
    
    public List<String> getName() {
        return Arrays.asList("play");
    }
    
    public String getExtendedHelp() {
        return BAMradio.getLanguage().get("COMMAND_PLAY_EXTENDED_HELP");
    }
    
    public boolean allowedInConsole() {
        return true;
    }
}
