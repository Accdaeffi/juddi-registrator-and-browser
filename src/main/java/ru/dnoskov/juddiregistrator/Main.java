package ru.dnoskov.juddiregistrator;

import java.util.Scanner;

import ru.dnoskov.juddiregistrator.logic.commands.AbsCommand;

public class Main {

    public static void main(String args[]) {
    	
		CommandParser parser = CommandParser.getParser();
		
		try (Scanner in = new Scanner(System.in)) {
			
			while (true) { 
		    	String input = in.nextLine();
		    	
		    	if (input.equals("/exit")) {
		    		break;
		    	}
		    	
		    	AbsCommand command = parser.parseMessage(input);
		    	
		    	System.out.println(command.executeCommand());
		    
			}
		}
		
	}

}
