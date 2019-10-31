package com.msc.kbs;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static Properties sounds;

    private static String[] getEnv(){
        
        String[] str = null;
        FileReader fr = null;
        try {
            fr = new FileReader("env.txt");
            BufferedReader br = new BufferedReader(fr);
            str = br.lines().toArray(String[]::new);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
            
            }
        }
        return str;
    }
    
    
    public static void main(String[] args) {

        sounds = new Properties();
        String[] env = getEnv();
        try {
            sounds.load(new FileReader(new File("sounds-config.properties")));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        
        
        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput pin21 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_21, "21", PinState.LOW);
        GpioPinDigitalOutput pin22 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "22", PinState.LOW);
        GpioPinDigitalOutput pin23 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "23", PinState.LOW);
        GpioPinDigitalOutput pin24 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "24", PinState.LOW);
        GpioPinDigitalInput pin25 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_25, "25", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput pin27 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_27, "27", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput pin28 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, "28", PinPullResistance.PULL_DOWN);
        GpioPinDigitalInput pin29 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, "29", PinPullResistance.PULL_DOWN);

        GpioPinDigitalOutput pinsOut[] = new GpioPinDigitalOutput[4];
        GpioPinDigitalInput pinsIn[] = new GpioPinDigitalInput[4];

        pinsOut[0] = pin21;
        pinsOut[1] = pin22;
        pinsOut[2] = pin23;
        pinsOut[3] = pin24;
        pinsIn[0] = pin25;
        pinsIn[1] = pin27;
        pinsIn[2] = pin28;
        pinsIn[3] = pin29;

        boolean run = true;
        String tmp = "";
        GpioPinDigitalOutput po;
        int nbTour = 0;
        while (run) {
            for (int i = 0; i < pinsOut.length; i++) {
                po = pinsOut[i];
                po.high();
                boolean rebond = false;
                for (int j = 0; j < pinsIn.length; j++) {
                    while (pinsIn[j].isHigh()) {
                        if (!rebond) {
                            tmp += traitement(i, j);
                            i = -1;
                            nbTour = 0;
                        }
                        rebond = true;
                    }
                    rebond = false;
                }
                po.low();
                nbTour++;
            }
            if (!tmp.isEmpty() && nbTour >= 3000) {
                System.out.println(tmp);
                nbTour = 0;                
                try {
                    Runtime rt = Runtime.getRuntime();
                    String line = "bash ./go.sh "+sounds.getProperty(tmp);
                    System.out.println(line);
                    Process process = rt.exec(line, env, new File("."));
                    int i = process.waitFor();
                    System.out.println(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tmp = "";
            }

        }

    }

    private static String traitement(int i, int j) {
        if (i == 0 && j == 0) {
            return ("1");
        } else if (i == 0 && j == 1) {
            return ("4");
        } else if (i == 0 && j == 2) {
            return ("7");
        } else if (i == 0 && j == 3) {
            return ("A");
        } else if (i == 1 && j == 0) {
            return ("2");
        } else if (i == 1 && j == 1) {
            return ("5");
        } else if (i == 1 && j == 2) {
            return ("8");
        } else if (i == 1 && j == 3) {
            return ("0");
        } else if (i == 2 && j == 0) {
            return ("3");
        } else if (i == 2 && j == 1) {
            return ("6");
        } else if (i == 2 && j == 2) {
            return ("9");
        } else if (i == 2 && j == 3) {
            return ("B");
        } else if (i == 3 && j == 0) {
            return ("F");
        } else if (i == 3 && j == 1) {
            return ("E");
        } else if (i == 3 && j == 2) {
            return ("D");
        } else if (i == 3 && j == 3) {
            return ("C");
        }
        return "";
    }

}
