package yt.bam.bamradio.command.abstracts;

import java.util.List;

import org.bukkit.command.*;
import org.bukkit.permissions.*;

public abstract interface ICommand {
	
	abstract void execute(CommandSender sender, String[] args);
    
	abstract String getHelp();
    
	abstract String getExtendedHelp();
    
	abstract String getSyntax();
    
	abstract String getPermission();
    
	abstract List<String> getName();
    
	abstract boolean allowedInConsole();
}
