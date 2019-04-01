package com.musixise.musixisebox.server.utils;


import org.springframework.data.util.Pair;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.*;

public class MidiUtil {

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final Double minGapTime = 1200.00;

    public static List<Long> getMachines( List<MidiTrack> midiTrackList) {

        //put same frequency togeter
        Map<Long, List<MidiTrack>> frequencyMap = new HashMap<Long, List<MidiTrack>>();
        //final result for machines
        List<Long> machinesList = new ArrayList<>();

        //note bucket list
        Map<Long, List<Pair<Long, Double> >> bucketMap = new HashMap<>();

        //复制自己
        midiTrackList = add20Sec(midiTrackList);

        midiTrackList.stream().forEach( s -> {
            //频率
            Long frequencyVal = s.frequency.longValue() * 2;

            if (frequencyMap.containsKey(frequencyVal)) {
                List<MidiTrack> oldList = frequencyMap.get(frequencyVal);
                ArrayList tmp = new ArrayList(oldList);
                tmp.add(s);
                frequencyMap.put(frequencyVal, tmp);
                //check time gap whether gt 1.2s

                //取出音片数据列表
                List<Pair<Long, Double>> pairsList = bucketMap.get(frequencyVal);

                Collections.reverse(pairsList);

                Boolean canReuse = false;

                List<Long> notAvaliableBucket = new ArrayList<>();
                //标记不能用的音片
                for (Pair<Long, Double> pair : pairsList) {
                    //排除已经不能用的音片
                    if (!notAvaliableBucket.contains(pair.getFirst())) {
                        //继续比较时间
                        if (Math.abs(s.getTime() - pair.getSecond()) > minGapTime) {
                            //可以复用这个音片
                            Collections.reverse(pairsList);
                            pairsList.add(Pair.of(pair.getFirst(), s.getTime()));
                            bucketMap.put(frequencyVal, pairsList);
                            canReuse = true;
                            break;
                        } else {
                            //标记音片不能用
                            notAvaliableBucket.add(pair.getFirst());
                        }
                    }
                }


                if (!canReuse) {
                    //new bucket
                    machinesList.add(frequencyVal);
                    Collections.reverse(pairsList);
                    //找出当前最大音片数+1
                    long maxBucketNum = getBucketMax(pairsList);
                    pairsList.add(Pair.of(maxBucketNum+1, s.getTime()));
                    bucketMap.put(frequencyVal, pairsList);
                }


            } else {
                //init
                frequencyMap.put(frequencyVal, Arrays.asList(s));
                machinesList.add(frequencyVal);
                bucketMap.put(frequencyVal, new ArrayList<Pair<Long, Double>>() {{
                    add(Pair.of(1L, s.getTime()));
                }});
            }


        });

        return machinesList;

    }

    private static List<MidiTrack> add20Sec( List<MidiTrack> midiTrackList) {

        List<MidiTrack> newMidiTrackList = new ArrayList<>();
        midiTrackList.forEach( m -> {
            newMidiTrackList.add(m.clone());

        });

        midiTrackList.forEach( m -> {
            m.setTime(m.getTime() + 20000.00);
            newMidiTrackList.add(m.clone());

        });

        return newMidiTrackList;
    }

    public static Long getBucketMax(List<Pair<Long, Double>> pairList) {

        Long max = 0L;
        for (Pair<Long, Double> s : pairList) {
            if (s.getFirst() > max) {
                max = s.getFirst();
            }
        }

        return max;
    }

    public static Double getFrequency(int midi) {
        Double pow = Math.pow(2, (midi - 69) / 12.0);
        return pow * 440;
    }

    public static List<MidiTrack> getTracks(URL url) throws InvalidMidiDataException, IOException {
        return getTracks(MidiSystem.getSequence(url));
    }

    public static List<MidiTrack> getTracks(InputStream inputStream) throws InvalidMidiDataException, IOException {
        return getTracks(MidiSystem.getSequence(inputStream));
    }

    public static List<MidiTrack> getTracks(Sequence sequence) throws InvalidMidiDataException, IOException {

        int ppq         = sequence.getResolution();
        int key         = 0;
        int octave      = 0;
        int note        = 0;
        String noteName = "";
        int velocity    = 0;
        int tempo       = 120;

        List<MidiTrack> midiTrackList = new ArrayList<>();
        int trackNumber = 0;
        for (Track track : sequence.getTracks()) {
            trackNumber++;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON) {
                        key = sm.getData1();
                        octave = (key / 12) - 1;
                        note = key % 12;
                        noteName = NOTE_NAMES[note];
                        velocity = sm.getData2();

                        double ticksToMs = ticksToMs(event.getTick(), ppq, tempo);
                        midiTrackList.add(new MidiTrack(noteName, key, ticksToMs, getFrequency(key)));
                    } else if (sm.getCommand() == NOTE_OFF) {
                        key = sm.getData1();
                        octave = (key / 12) - 1;
                        note = key % 12;
                        noteName = NOTE_NAMES[note];
                        velocity = sm.getData2();
                    } else {
                    }



                }  else if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    //META_TEMPO
                    if (metaMessage.getType() == 81) {
                        //tempo change
                        tempo = tempoChanged(metaMessage);
                    }
                } else {
                }
            }

        }

        return midiTrackList;
    }


    private static int tempoChanged(MetaMessage meta) {
        int newTempoMSPQ = (meta.getData()[2] & 0xFF) |
                ((meta.getData()[1] & 0xFF) << 8) |
                ((meta.getData()[0] & 0xFF) << 16);
        return 60000000 / newTempoMSPQ;
    }


//    public static void main(String[] args) throws InvalidMidiDataException, IOException {
//        //List<MidiTrack> tracks = MidiUtil.getTracks(new URL("https://img.musixise.com/6dTh3SHJ_xuemaojiao.mid"));
//        List<MidiTrack> tracks = MidiUtil.getTracks(new URL("https://audio.musixise.com/mawUa3G0_output.mid"));
//        List<Long> machines = getMachines(tracks);
//        System.out.println(machines);
//
//    }

    private static double ticksToMs(long ticks, long resolutionTicksPerBeat, long tempoBPM) {
        return 60000.00 / (tempoBPM * resolutionTicksPerBeat) * ticks;
    }

    public static class MidiTrack implements Cloneable, Serializable {

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
        private double time;

        /**
         * 频率
         */
        private Double frequency;

        public MidiTrack(String name, int midi, double time, Double frequency) {
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

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        @Override
        protected MidiTrack clone() {

            MidiTrack midiTrack = null;
            try {
                midiTrack = (MidiTrack) super.clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return midiTrack;
        }
    }

}
