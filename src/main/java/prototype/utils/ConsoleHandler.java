package prototype.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import static prototype.program.Main.run;

public class ConsoleHandler implements Runnable {
    private final Scanner sc = new Scanner(System.in);
    public static volatile Queue<String> consoleInput = new LinkedList<String>();

    public void run() {
        while (run) {
            consoleInput.add(sc.nextLine());
        }
    }
}
