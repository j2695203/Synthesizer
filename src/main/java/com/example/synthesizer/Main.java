package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Main {
    static void getSampleTest() {
        AudioClip a1 = new AudioClip();
        a1.datas[0] = (byte) 0x1A;
        a1.datas[1] = (byte) 0x2B;
        a1.datas[2] = (byte) 0xA1;
        a1.datas[3] = (byte) 0xFF;
        Assertions.assertEquals(0x2B1A, a1.getSample(0)); // positive number
        Assertions.assertEquals(0xFFA1, a1.getSample(1)); // negative number
    }

    static void setSampleTest(){
        AudioClip a2 = new AudioClip();
        a2.setSample(0, (short) 0x3ABC);
        a2.setSample(2, (short) 0xFF33);
        Assertions.assertEquals(0x3ABC, a2.getSample(0)); // positive number
        Assertions.assertEquals(0xFF33, a2.getSample(2)); // negative number
    }

    public static void main(String[] args) throws LineUnavailableException {
//        getSampleTest();
//        setSampleTest();

        // Get properties from the system about samples rates, etc.
        // AudioSystem is a class from the Java standard library.
        Clip c = AudioSystem.getClip(); // Note, this is different from our AudioClip class.

        // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

        AudioComponent gen = new SineWave(440); // Your code
        AudioClip clip = gen.getClip();         // Your code

        c.open( format16, clip.getData(), 0, clip.getData().length ); // Reads data from our byte array to play it.

        System.out.println( "About to play..." );
        c.start(); // Plays it.
        c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total

        // Makes sure the program doesn't quit before the sound plays.
        while( c.getFramePosition() < clip.getData().length || c.isActive() || c.isRunning() ){
            // Do nothing while we wait for the note to play.
        }

        System.out.println( "Done." );
        c.close();

    }
}
