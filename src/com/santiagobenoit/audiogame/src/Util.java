package com.santiagobenoit.audiogame.src;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Utility class.
 * @author Santiago Benoit
 */
public class Util {
    
    public static synchronized void playSound(String path, float gainAmount, float panAmount) {
        if (!GUI.gameover) {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(GUI.class.getResourceAsStream(path)));
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(gainAmount);
                panControl = (FloatControl) clip.getControl(FloatControl.Type.PAN);
                panControl.setValue(panAmount);
                clip.start();
                clip.addLineListener((LineEvent event) -> {
                    LineEvent.Type type = event.getType();
                    if (type == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                
            }
        }
    }
    
    public static void setGain(int gain) {
        gainControl.setValue(gain);
    }
    
    public static void setPan(int pan) {
        panControl.setValue(pan);
    }
    
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
    
    private static volatile FloatControl gainControl;
    private static volatile FloatControl panControl;
}
