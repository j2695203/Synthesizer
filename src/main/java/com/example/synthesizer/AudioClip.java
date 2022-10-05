package com.example.synthesizer;

import java.util.Arrays;

public class AudioClip {
    static double duration = 2.0;
    static int sampleRate = 44100;
    byte[] datas = new byte[ 44100*2*2 ];

    public int getSample(int i) {
        int lower = Byte.toUnsignedInt(datas[ 2 * i ]); // use mask to avoid the signed digit
        int upper = datas[ ( 2 * i ) + 1 ] << 8 & 0xFF00;
        return upper | lower;
    }

    public void setSample(int i, short value){
        datas[2 * i] = (byte) (value & 0xFF);
        datas[( 2 * i ) + 1] = (byte) (value >> 8);
    }

    public byte[] getData(){
        return Arrays.copyOf(datas, datas.length);
    }
}

