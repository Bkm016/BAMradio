package yt.bam.bamradio.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.bam.bamradio.BAMradio;
import yt.bam.bamradio.command.abstracts.ICommand;
import yt.bam.bamradio.command.sub.CommandAbout;
import yt.bam.bamradio.command.sub.CommandHelp;
import yt.bam.bamradio.command.sub.CommandInfo;
import yt.bam.bamradio.command.sub.CommandList;
import yt.bam.bamradio.command.sub.CommandMute;
import yt.bam.bamradio.command.sub.CommandNext;
import yt.bam.bamradio.command.sub.CommandPlay;
import yt.bam.bamradio.command.sub.CommandRandom;
import yt.bam.bamradio.command.sub.CommandStop;
import yt.bam.bamradio.command.sub.CommandUnmute;

/**
 * @author sky
 * @since 2018年2月8日 下午4:40:16
 */
public class CommandHandler implements CommandExecutor {
	
	/**
	 * 注册命令
	 */
	public CommandHandler() {
		BAMradio.getCommands().add(new CommandList());
		BAMradio.getCommands().add(new CommandMute());
		BAMradio.getCommands().add(new CommandNext());
		BAMradio.getCommands().add(new CommandPlay());
		BAMradio.getCommands().add(new CommandRandom());
		BAMradio.getCommands().add(new CommandInfo());
		BAMradio.getCommands().add(new CommandStop());
        BAMradio.getCommands().add(new CommandUnmute());
        BAMradio.getCommands().add(new CommandHelp());
        BAMradio.getCommands().add(new CommandAbout());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ICommand command = null;
        if (args.length == 0) {
            command = new CommandHelp();
        }
        else {
        	// 检查所有已注册命令
            for (ICommand _cmd : BAMradio.getCommands()) {
            	if (_cmd.getName().contains(args[0])) {
            		command = _cmd;
            		break;
            	}
            }
            // 指令不存在
            if (command == null) {
                sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_UNKNOWN_COMMAND"));
                return true;
            }
            // 如果没权限
            if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            	sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_NO_PERMISSION"));
            	return true;
            }
            // 如果是后台
            if (!command.allowedInConsole() && !(sender instanceof Player)) {
            	sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_ONLY_CHAT"));
            	return true;
            }
        }
        // 执行命令
        command.execute(sender, args);
        return true;
    }

}
