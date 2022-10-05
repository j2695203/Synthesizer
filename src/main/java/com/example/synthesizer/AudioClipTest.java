package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AudioClipTest {

    @Test
    void getSample() {
        AudioClip a1 = new AudioClip();
        a1.datas = new byte[]{0x3c, (byte) 0xa2, 0x01, (byte) 0xff};
        Assertions.assertEquals(0xff01, a1.getSample(1));
    }

    @Test
    void setSample() {

    }

    @Test
    void getData() {
    }
}