package View;

import java.util.LinkedHashMap;

public class Display {
    LinkedHashMap<Integer, Command> commands;
    private int commandNumber = 0;

    public void add(Command c) {
        commandNumber++;
        commands.put(commandNumber, c);
    }

    public void printAllCommands() {
        for (Command c : commands.values()) {
            c.displayText();
            System.out.println("\n");
        }
    }
}
