package com.example.synthesizer;

import org.junit.jupiter.api.Assertions;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;

public class Main {
    static void SampleTest(){
        AudioClip a1 = new AudioClip();
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE; i++){
            a1.setSample(0, i);
            Assertions.assertEquals(i, a1.getSample(0));
        }
    }

    public static void main(String[] args) throws LineUnavailableException {
        SampleTest();
        // Get properties from the system about samples rates, etc.
        // AudioSystem is a class from the Java standard library.
        Clip c = AudioSystem.getClip(); // Note, this is different from our AudioClip class.

        // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );


        // Create each component
        AudioComponent gen1 = new SineWave(440);
        AudioComponent gen2 = new SineWave(220);
        AudioComponent gen3 = new LinearRamp(50,2000);
        VFSineWave vfSineWave = new VFSineWave();

        // Mixer, Processor
        Mixer mixer = new Mixer();
        mixer.connectInput(gen1);
        mixer.connectInput(gen2);
        vfSineWave.connectInput(gen3);

        // Filter
        Filter volume = new Filter(0.5);
//        volume.connectInput(mixer);
        volume.connectInput(vfSineWave);

        // Clip to be played
        AudioClip clip = volume.getClip();

        // Play method by library
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
