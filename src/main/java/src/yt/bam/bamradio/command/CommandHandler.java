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
 * @since 2018��2��8�� ����4:40:16
 */
public class CommandHandler implements CommandExecutor {
	
	/**
	 * ע������
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
        	// ���������ע������
            for (ICommand _cmd : BAMradio.getCommands()) {
            	if (_cmd.getName().contains(args[0])) {
            		command = _cmd;
            		break;
            	}
            }
            // ָ�����
            if (command == null) {
                sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_UNKNOWN_COMMAND"));
                return true;
            }
            // ���ûȨ��
            if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            	sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_NO_PERMISSION"));
            	return true;
            }
            // ����Ǻ�̨
            if (!command.allowedInConsole() && !(sender instanceof Player)) {
            	sender.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("COMMAND_MANAGER_ONLY_CHAT"));
            	return true;
            }
        }
        // ִ������
        command.execute(sender, args);
        return true;
    }

}
