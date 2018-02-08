package yt.bam.bamradio.radiomanager;

import org.bukkit.entity.*;
import java.util.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.radiomanager.listener.noteblockapi.ListenerSong;
import yt.bam.bamradio.radiomanager.player.MinecraftMidiPlayer;
import yt.bam.bamradio.radiomanager.player.SequencerMidiPlayer;

import lombok.Getter;
import lombok.Setter;
import me.skymc.taboolib.message.MsgUtils;

import org.bukkit.command.*;
import java.io.*;
import javax.sound.midi.*;
import org.bukkit.*;

public class RadioManager {
	
	@Getter
	private List<Player> tunedIn = new ArrayList<>();
	
    @Getter
    private MidiPlayer midiPlayer;
    
    @Getter
    private NoteBlockPlayer boteBlockPlayer = null;
    
    @Getter
    private Integer nowPlayingIndex = 0;
    
	@Getter
	@Setter
    private String nowPlayingFile = "";
	
    @Getter
	@Setter
    private boolean nowPlaying = false;
    
    /**
     * 构造方法
     */
    public RadioManager() {
        // 如果启用 NoteBlockAPI
        if (BAMradio.isNoteBlockAPI()) {
        	boteBlockPlayer = new NoteBlockPlayer(this);
        	// 注册监听器
            Bukkit.getPluginManager().registerEvents(new ListenerSong(), BAMradio.getInst());
        }
        
        // 强制播放器
        if (BAMradio.getInst().getConfig().getBoolean("force-software-sequencer")) {
            midiPlayer = new MinecraftMidiPlayer(this);
        }
        else {
            try {
                midiPlayer = new SequencerMidiPlayer(this);
            }
            catch (MidiUnavailableException ex) {
            	midiPlayer = new MinecraftMidiPlayer(this);
                MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_EXCEPTION_MIDI_UNAVAILABLE"), BAMradio.getInst());
            }
        }
        
        // 添加玩家
        Bukkit.getOnlinePlayers().forEach(x -> tuneIn(x));
        
        // 如果启动自动播放
        if (BAMradio.getInst().getConfig().getBoolean("auto-play")) {
            String[] midis = listRadioFiles();
            if (midis != null && midis.length > 0) {
                playSong(midis[0]);
            }
        }
    }
    
    /**
     * 播放歌曲
     * 
     * @param player 玩家
     */
    public void tuneIn(Player player) {
        tunedIn.add(player);
        if (boteBlockPlayer != null) {
            boteBlockPlayer.tuneIn(player);
        }
        // 提示曲目
        nowPlaying(player, false);
    }
    
    /**
     * 退出播放
     * 
     * @param player
     */
    public void tuneOut(Player player) {
        tunedIn.remove(player);
        if (boteBlockPlayer != null) {
            boteBlockPlayer.tuneOut(player);
        }
    }
    
    /**
     * 播放下一首
     */
    public void playNextSong() {
        nowPlayingIndex++;
        String[] midiFileNames = listRadioFiles();
        if (nowPlayingIndex >= midiFileNames.length) {
            nowPlayingIndex = 0;
        }
        playSong(midiFileNames[nowPlayingIndex]);
    }
    
    /**
     * 随机播放
     */
    public void playRandomSong() {
        String[] midiFileNames = listRadioFiles();
        nowPlayingIndex = (int)(Math.random() * (midiFileNames.length + 1));
        if (nowPlayingIndex >= midiFileNames.length) {
            nowPlayingIndex = 0;
        }
        playSong(midiFileNames[nowPlayingIndex]);
    }
    
    /**
     * 指定播放
     * 
     * @param fileName 文件名
     * @return {@link Boolean}
     */
    public boolean playSong(String fileName) {
        if (nowPlaying) {
            stopPlaying();
        }
        File file = getMidiFile(fileName);
        if (file != null && midiPlayer != null) {
            return midiPlayer.playSong(fileName);
        }
        file = getNoteBlockFile(fileName);
        if (file != null) {
            if (boteBlockPlayer != null) {
                return boteBlockPlayer.playSong(fileName);
            }
            MsgUtils.send("NoteBlockAPI not found, can not play NBS file!", BAMradio.getInst());
        }
        return false;
    }
    
    /**
     * 停止播放
     */
    public void stopPlaying() {
        midiPlayer.stopPlaying();
        if (boteBlockPlayer != null) {
            boteBlockPlayer.stopPlaying();
        }
    }
    
    /**
     * 获取文件
     * 
     * @param fileName 文件名
     * @return {@link File}
     */
    public File getMidiFile(String fileName) {
        File midiFile = new File(BAMradio.getInst().getDataFolder(), fileName.replace(".mid", "") + ".mid");
        if (!midiFile.exists()) {
            return null;
        }
        return midiFile;
    }
    
    /**
     * 获取文件
     * 
     * @param fileName 文件名
     * @return {@link File}
     */
    public File getNoteBlockFile(final String fileName) {
        final File noteBlockFile = new File(BAMradio.getInst().getDataFolder(), fileName.replace(".nbs", "") + ".nbs");
        if (!noteBlockFile.exists()) {
            return null;
        }
        return noteBlockFile;
    }
    
    /**
     * 载入所有歌曲
     * 
     * @return {@link String[]}
     */
    public static String[] listRadioFiles() {
        File[] files = BAMradio.getInst().getDataFolder().listFiles();
        List<String> radioFiles = new ArrayList<>();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            if (file.getName().endsWith(".mid")) {
                radioFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".mid")));
            }
            if (BAMradio.isNoteBlockAPI() && file.getName().endsWith(".nbs")) {
                radioFiles.add(file.getName().substring(0, file.getName().lastIndexOf(".nbs")));
            }
        }
        return radioFiles.toArray(new String[0]);
    }
    
    /**
     * 载入所有歌曲
     * 
     * @return {@link String[]}
     */
    public static String[] listRadioFilesWithExtensions() {
        File[] files = BAMradio.getInst().getDataFolder().listFiles();
        List<String> radioFiles = new ArrayList<String>();
        for (File file : files) {
            if (file.getName().endsWith(".mid")) {
                radioFiles.add(file.getName());
            }
            if (BAMradio.isNoteBlockAPI() && file.getName().endsWith(".nbs")) {
                radioFiles.add(file.getName());
            }
        }
        return radioFiles.toArray(new String[0]);
    }
    
    /**
     * 显示播放曲目
     * 
     * @param player 玩家
     * @param force 显示
     */
    public void nowPlaying(CommandSender player, boolean force) {
    	// 是否显示曲目
        if (!force && !BAMradio.getInst().getConfig().getBoolean("show-current-track")) {
            return;
        }
        if (nowPlaying) {
        	player.sendMessage(BAMradio.getPrefix() + BAMradio.getLanguage().get("MIDI_MANAGER_NOW_PLAYING") + " " + ChatColor.YELLOW + nowPlayingFile.replace("_", " "));
        }
    }
}
