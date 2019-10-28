package com.msc.kbs;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Main {

    public static void main(String[] args) {
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
                            try {
                                Thread.sleep(250);
                            } catch (Exception e) {
                            }
                        }
                        rebond = true;
                    }
                    rebond = false;
                }
                po.low();
            }
            if (!tmp.isEmpty()) {
                System.out.println(tmp);
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
