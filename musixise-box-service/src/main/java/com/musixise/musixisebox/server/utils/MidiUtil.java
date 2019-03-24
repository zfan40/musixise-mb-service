package com.musixise.musixisebox.server.utils;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MidiUtil {

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static List<Long> getMachines(List<MidiTrack> midiTrackList) {

        List<Long> machines = new ArrayList<>();
        midiTrackList.stream().forEach( s -> {
            machines.add(s.frequency.longValue() *2);
        });

        //排序去重
        return machines.stream().sorted().distinct().collect(Collectors.toList());

    }

    public static Double getFrequency(int midi) {
        Double pow = Math.pow(2, (midi - 69) / 12.0);
        return pow * 440;
    }

    public static List<MidiTrack> getTracks(InputStream inputStream) throws InvalidMidiDataException, IOException {

        Sequence sequence = MidiSystem.getSequence(inputStream);


        int key = 0;
        int octave = 0;
        int note = 0;
        String noteName = "";
        int velocity = 0;

        List<MidiTrack> midiTrackList = new ArrayList<>();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            //System.out.println("Track " + trackNumber + ": size = " + track.size());
            //System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        key = sm.getData1();
                        octave = (key / 12) - 1;
                        note = key % 12;
                        noteName = NOTE_NAMES[note];
                        velocity = sm.getData2();
                        //System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                        midiTrackList.add(new MidiTrack(noteName, key, event.getTick(), getFrequency(key)));
                    } else if (sm.getCommand() == NOTE_OFF) {
                        key = sm.getData1();
                        octave = (key / 12) - 1;
                        note = key % 12;
                        noteName = NOTE_NAMES[note];
                        velocity = sm.getData2();
                        //System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        //System.out.println("Command:" + sm.getCommand());
                    }



                } else {
                    //System.out.println("Other message: " + message.getClass());
                }
            }

            //System.out.println();
        }

        return midiTrackList;
    }


//    public static void main(String[] args) throws InvalidMidiDataException, IOException {
//
//
//        List<MidiTrack> tracks = MidiUtil.getTracks("");
//
//        List<Long> machines = getMachines(tracks);
//
//        System.out.println(tracks);
//    }

    public static class MidiTrack {

        /**
         * 音名
         */
        private String name;

        /**
         * midi
         */
        private int midi;

        /**
         * 时长
         */
        private long time;

        /**
         * 频率
         */
        private Double frequency;

        public MidiTrack(String name, int midi, long time, Double frequency) {
            this.name = name;
            this.midi = midi;
            this.time = time;
            this.frequency = frequency;
        }

        public Double getFrequency() {
            return frequency;
        }

        public void setFrequency(Double frequency) {
            this.frequency = frequency;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMidi() {
            return midi;
        }

        public void setMidi(int midi) {
            this.midi = midi;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

}
