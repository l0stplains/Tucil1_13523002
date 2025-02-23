package tucil_1_stima;

import tucil_1_stima.cli.MainDriver;
import tucil_1_stima.gui.MainApp;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        boolean running = false;
        for(String x : args){
            if(Objects.equals(x, "-cli")){
                running = true;
                MainDriver.start(args); // CLI mode
            }
        }
        if(!running) MainApp.show(); // GUI mode
    }

}