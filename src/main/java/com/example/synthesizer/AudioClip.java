package com.example.synthesizer;

import java.util.ArrayList;
import java.util.Arrays;

public class AudioClip {
    static double duration = 2.0;
    int sampleRate = 44100;
    byte[] samples;

    int getSample(int i) {
        int lower = samples[ 2 * i ];
        int upper = samples [ ( 2 * i ) + 1 ] << 8;
        return upper | lower;
    }

    void setSample(int i, short value){
        samples[2 * i] = (byte) (value & 0x00FF);
        samples[( 2 * i ) + 1] = (byte) (value >> 8);
    }

    byte[] getData(){
        return Arrays.copyOf(samples, samples.length);
    }
}
