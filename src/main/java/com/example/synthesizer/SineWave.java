package com.example.synthesizer;
import java.lang.Math;

interface AudioComponent{
    AudioClip getClip();
    boolean hasInput();
    void connectInput( AudioComponent input);
}

public class SineWave implements AudioComponent{
    int frequency_;

   SineWave(int frequency){
        this.frequency_ = frequency;
    }

    @Override
    public AudioClip getClip() {
        AudioClip adClip = new AudioClip();
        for (int i = 0; i < adClip.datas.length/2; i++ ){
            adClip.setSample(i, (short) (Short.MAX_VALUE * Math.sin( 2*Math.PI*frequency_ * i / adClip.sampleRate )));
            //adClip.datas[i] = (byte) (Short.MAX_VALUE * Math.sin( 2*Math.PI*frequency_ * i / adClip.sampleRate ));  // will save reversed order
        }
        return adClip;
    }

    @Override
    public boolean hasInput() { //?????????????
        // frequency
        // volume
        // or new audioClip
        return false;
    }

    @Override
    public void connectInput(AudioComponent input) {
//        if ( hasInput() == true ){
//            //store a reference to the AudioComponent parameter
//            //If the component doesn't accept inputs, you can assert( false ) in here
//        }
    }


}
