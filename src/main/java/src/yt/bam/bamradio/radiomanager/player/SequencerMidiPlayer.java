package yt.bam.bamradio.radiomanager.player;

import java.util.logging.*;
import org.bukkit.entity.*;
import yt.bam.bamradio.*;
import yt.bam.bamradio.radiomanager.Instrument;
import yt.bam.bamradio.radiomanager.MidiPlayer;
import yt.bam.bamradio.radiomanager.NotePitch;
import yt.bam.bamradio.radiomanager.RadioManager;

import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.scheduler.*;

import me.skymc.taboolib.message.MsgUtils;

import java.io.*;
import java.util.*;
import javax.sound.midi.*;
import org.bukkit.*;

public class SequencerMidiPlayer implements MidiPlayer, Receiver {
	
    private final Sequencer sequencer;
    private final Map<Integer, Byte> channelPatches;
    private RadioManager manager;
    
    public SequencerMidiPlayer(RadioManager manager) throws MidiUnavailableException {
        this.manager = manager;
        manager.getTunedIn().clear();
        this.channelPatches = new HashMap<Integer, Byte>();
        (this.sequencer = MidiSystem.getSequencer()).open();
        this.sequencer.getTransmitter().setReceiver(this);
    }
    
    public void stopPlaying() {
        sequencer.stop();
        Bukkit.getScheduler().cancelTasks(BAMradio.getInst());
    }
    
    public boolean playSong(String midiName) {
        try {
            manager.setNowPlayingFile(midiName);
            File midiFile = manager.getMidiFile(midiName);
            if (midiFile == null) {
                return false;
            }
            try {
                Sequence midi = MidiSystem.getSequence(midiFile);
                sequencer.setSequence(midi);
                sequencer.start();
                manager.setNowPlaying(true);
            }
            catch (InvalidMidiDataException ex) {
                MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_INVALID_MIDI") + " " + midiName, BAMradio.getInst());
            }
            catch (IOException e2) {
                MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_CORRUPT_MIDI") + " " + midiName, BAMradio.getInst());
            }
            for (Player player : manager.getTunedIn()) {
                manager.nowPlaying(player, false);
            }
            new BukkitRunnable() {
                public void run() {
                    if (!manager.isNowPlaying()) {
                        cancel();
                    }
                    if (!sequencer.isRunning() || sequencer.getMicrosecondPosition() > sequencer.getMicrosecondLength()) {
                        stopPlaying();
                        if (BAMradio.getInst().getConfig().getBoolean("auto-play-next")) {
                            manager.playNextSong();
                        }
                    }
                }
            }.runTaskTimer(BAMradio.getInst(), 20L, 20L);
        }
        catch (Exception e) {
            MsgUtils.send(BAMradio.getLanguage().get("MIDI_MANAGER_CORRUPT_MIDI") + " " + midiName + " (" + e.getMessage() + ")", BAMradio.getInst());
        }
        return true;
    }
    
    protected void finalize() {
        this.sequencer.close();
    }
    
    public void close() {
    }
    
    public void send(MidiMessage message, long timeStamp) {
        if (!(message instanceof ShortMessage)) {
            return;
        }
        ShortMessage event = (ShortMessage) message;
        if (event.getCommand() == 144) {
            int midiNote = event.getData1();
            float volume = event.getData2() / 127;
            if (volume == 0.0f) {
                volume = 1.0f;
            }
            volume += BAMradio.getInst().getConfig().getInt("volume");
            int note = Integer.valueOf((midiNote - 6) % 24);
            int channel = event.getChannel();
            byte patch = 1;
            if (channelPatches.containsKey(channel)) {
                patch = channelPatches.get(channel);
            }
            for (Player player : manager.getTunedIn()) {
                Sound sound = Instrument.getInstrument(patch, channel);
                if (sound != null) {
                    if (sound == Sound.BLOCK_NOTE_PLING) {
                        player.playSound(player.getLocation().add(0.0, 20.0, 0.0), sound, volume, NotePitch.getPitch(note));
                    }
                    else {
                        player.playSound(player.getLocation(), sound, volume, NotePitch.getPitch(note));
                    }
                }
            }
        }
        else if (event.getCommand() == 192) {
            channelPatches.put(event.getChannel(), (byte)event.getData1());
        }
        else if (event.getCommand() == 252) {
            stopPlaying();
            manager.playNextSong();
        }
    }
}
