package yt.bam.bamradio;

import org.bukkit.plugin.java.*;
import com.sk89q.worldguard.bukkit.*;

import lombok.Getter;
import me.skymc.taboolib.fileutils.FileUtils;
import me.skymc.taboolib.message.MsgUtils;
import me.skymc.taboolib.string.Language;
import yt.bam.bamradio.bstats.Metrics;
import yt.bam.bamradio.command.CommandHandler;
import yt.bam.bamradio.command.abstracts.ICommand;
import yt.bam.bamradio.command.sub.*;
import yt.bam.bamradio.radiomanager.*;

import java.util.*;
import java.util.Map.Entry;

import org.bukkit.command.*;
import org.bukkit.plugin.*;
import java.io.*;
import org.bukkit.event.*;
import yt.bam.bamradio.radiomanager.listener.*;
import java.util.logging.*;
import org.bukkit.*;

public class BAMradio extends JavaPlugin {
	
	@Getter
	private static String prefix = "��7[��8BAMradio��7] ��r";
	
	@Getter
	private static BAMradio inst;
	
	@Getter
	private static Language language;
	
	@Getter
	private static RadioManager radioManager;
	
	@Getter
	private static WorldGuardPlugin worldGuardPlugin;
	
	@Getter
	private static boolean noteBlockAPI;
	
	@Getter
	private static List<ICommand> commands = new ArrayList<>();
    
    @Override
    public void onLoad() {
    	inst = this;
    	saveDefaultConfig();
    }
    
    @Override
    public void onEnable() {
    	// ����ͳ��
    	new Metrics(this);
    	
    	// ע������
    	Bukkit.getPluginCommand("bamradio").setExecutor(new CommandHandler());
    	
    	// ��� NoteBlockAPI ���
        if (Bukkit.getPluginManager().getPlugin("NoteBlockAPI") != null) {
            noteBlockAPI = true;
            MsgUtils.send("Detected NoteBlockAPI!", this);
        }
        else {
        	noteBlockAPI = false;
        }
        
        // ���������ļ�
        language = new Language(getConfig().getString("language"), this);
        
        // ��������
        radioManager = new RadioManager();
        
        // ����������
        if (getConfig().getStringList("enable_region").contains("all_region") && getConfig().getStringList("enable_world").contains("all_world")) {
        	Bukkit.getPluginManager().registerEvents(new ListenerPlayer(), this);
        }
        else {
            if ((worldGuardPlugin = getWGPlugin()) != null) {
                try {
                    MsgUtils.send("Detected WorldGuard!", this);
                    Bukkit.getPluginManager().registerEvents(new ListenerWorldGuard(), this);
                    Bukkit.getPluginManager().registerEvents(new ListenerRegion(), this);
                }
                catch (Exception e) {
                	MsgUtils.send("Was not able to bind to WorldGuard!", this);
                	Bukkit.getPluginManager().registerEvents(new ListenerPlayer(), this);
                }
            }
            else {
            	 MsgUtils.send("WorldGuard was not detected, disabling region suppport", this);
            	 Bukkit.getPluginManager().registerEvents(new ListenerPlayer(), this);
            }
        }
    }
    
    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        // �رղ���
        if (radioManager != null) {
        	radioManager.stopPlaying();
        }
    }
    
    /**
     * ���뱣�������
     * 
     * @return {@link WorldGuardPlugin}
     */
    private WorldGuardPlugin getWGPlugin() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin)plugin;
    }
}
