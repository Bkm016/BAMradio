package yt.bam.bamradio.radiomanager.player;

import java.util.logging.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.radiomanager.Instrument;
import yt.bam.bamradio.radiomanager.MidiPlayer;
import yt.bam.bamradio.radiomanager.MidiTrack;
import yt.bam.bamradio.radiomanager.NotePitch;
import yt.bam.bamradio.radiomanager.RadioManager;

import org.bukkit.entity.*;
import org.bukkit.command.*;
import java.io.*;
import java.util.*;
import javax.sound.midi.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;

import me.skymc.taboolib.message.MsgUtils;

import org.bukkit.plugin.*;

public class MinecraftMidiPlayer implements MidiPlayer {
	
    public static long MILLIS_PER_TICK = 3L;
    private List<MidiTrack> midiTracks;
    private Map<Integer, Integer> channelPatches;
    private float tempo;
    private int resolution;
    private long timeLeft;
    private float currentTick;
    private Timer timer;
    private RadioManager manager;
    
    public MinecraftMidiPlayer(RadioManager manager) {
        this.currentTick = 0.0f;
        this.manager = manager;
        this.timer = new Timer();
        this.midiTracks = new ArrayList<MidiTrack>();
        this.channelPatches = new HashMap<Integer, Integer>();
    }
    
    public void stopPlaying() {
        synchronized (this.midiTracks) {
            this.manager.setNowPlaying(false);
            this.midiTracks.clear();
            this.timer.cancel();
            this.timer = new Timer();
        }
    }
    
    public boolean playSong(String midiName) {
        this.manager.setNowPlayingFile(midiName);
        File midiFile = this.manager.getMidiFile(midiName);
        if (midiFile == null) {
            return false;
        }
        try {
            Sequence midi = MidiSystem.getSequence(midiFile);
            int microsPerQuarterNote = 0;
            Track firstTrack = midi.getTracks()[0];
            for (int i = 0; i < firstTrack.size(); ++i) {
                if (firstTrack.get(i).getMessage().getStatus() == 255 && firstTrack.get(i).getMessage().getMessage()[1] == 81) {
                    MetaMessage message = (MetaMessage) firstTrack.get(i).getMessage();
                    for (byte b :  message.getData()) {
                        microsPerQuarterNote <<= 8;
                        microsPerQuarterNote += b;
                    }
                    break;
                }
            }
            this.tempo = 500000.0f / microsPerQuarterNote * 0.8f * 0.15f;
            this.timeLeft = midi.getMicrosecondLength() / 1000L;
            this.resolution = (int)Math.floor(midi.getResolution() / 24);
            for (int i = 0; i < midi.getTracks().length; ++i) {
                MidiTrack midiTrack = new MidiTrack(this, midi.getTracks()[i]);
                this.midiTracks.add(midiTrack);
            }
        }
        catch (InvalidMidiDataException ex) {
        	MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_INVALID_MIDI") + " " + midiName, BAMradio.getInst());
        }
        catch (IOException ex2) {
            MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_CORRUPT_MIDI") + " " + midiName, BAMradio.getInst());
        }
        for (Player player : this.manager.getTunedIn()) {
            this.manager.nowPlaying(player, false);
        }
        this.timer.scheduleAtFixedRate(new TickTask(), 3L, 3L);
        return true;
    }
    
    public void onMidiMessage(MidiMessage event) {
        if (event instanceof ShortMessage) {
            ShortMessage message = (ShortMessage)event;
            if (message.getCommand() == 144) {
                int midiNote = message.getData1();
                float volume = message.getData2();
                if (volume == 0.0f) {
                    volume = 1.0f;
                }
                volume += BAMradio.getInst().getConfig().getInt("volume");
                int note = Integer.valueOf((midiNote - 6) % 24);
                int channel = message.getChannel();
                int patch = 1;
                if (this.channelPatches.containsKey(channel)) {
                    patch = this.channelPatches.get(channel);
                }
                Sound instrument = Instrument.getInstrument(patch, channel);
                float notePitch = NotePitch.getPitch(note);
                if (instrument != null) {
                    for (Player player : this.manager.getTunedIn()) {
                        player.playSound(player.getLocation(), instrument, volume, notePitch);
                    }
                }
            }
            else if (message.getCommand() == 192) {
                this.channelPatches.put(message.getChannel(), message.getData1());
            }
            else if (message.getCommand() == 252) {
                this.stopPlaying();
                this.manager.playNextSong();
            }
        }
        else if (event instanceof MetaMessage) {
            MetaMessage message2 = (MetaMessage)event;
            if (message2.getType() == 81) {
                int microsPerQuarterNote = 0;
                for (byte b : message2.getData()) {
                    microsPerQuarterNote <<= 8;
                    microsPerQuarterNote += b;
                }
                this.tempo = 500000.0f / microsPerQuarterNote * 0.8f * 0.15f;
            }
        }
    }
    
    public class TickTask extends TimerTask {
    	
        public TickTask() {
            manager.setNowPlaying(true);
            currentTick = 0.0f;
        }
        
        public void run() {
            if (manager.isNowPlaying()) {
                currentTick += tempo * resolution;
                synchronized (midiTracks) {
                    for (MidiTrack track : midiTracks) {
                        track.nextTick(currentTick);
                    }
                }
                timeLeft -= 3L;
                if (timeLeft <= 0L) {
                    stopPlaying();
                    if (BAMradio.getInst().getConfig().getBoolean("auto-play-next")) {
                        new BukkitRunnable() {
                            public void run() {
                                manager.playNextSong();
                            }
                        }.runTask(BAMradio.getInst());
                    }
                }
            }
            else {
                this.cancel();
            }
        }
    }
}
