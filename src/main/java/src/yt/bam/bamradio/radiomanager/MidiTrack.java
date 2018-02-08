package yt.bam.bamradio.radiomanager;

import javax.sound.midi.*;

import yt.bam.bamradio.radiomanager.player.MinecraftMidiPlayer;

public class MidiTrack {
	
    private final MinecraftMidiPlayer player;
    private final Track track;
    private int event;
    
    public MidiTrack(final MinecraftMidiPlayer player, Track track) {
        this.event = 0;
        this.player = player;
        this.track = track;
    }
    
    public void nextTick(float currentTick) {
        while (event < track.size() - 1 && track.get(event).getTick() <= currentTick) {
            player.onMidiMessage(track.get(event).getMessage());
            event++;
        }
    }
}
