package ru.dnoskov.juddiregistrator;

import ru.dnoskov.juddiregistrator.logic.commands.*;
import ru.dnoskov.juddiregistrator.logic.commands.errors.*;
import ru.dnoskov.juddiregistrator.exception.*;

public class CommandParser {

	private static CommandParser instance;
	
	private CommandParser() {
	}
	
	public static CommandParser getParser() {
		if (instance == null) {
			instance = new CommandParser();
		}
		
		return instance;
	}
	
	public AbsCommand parseMessage(String input) {
		String arr[] = input.split(" ", 2);
		String command = arr[0].trim();
		String arguments = (arr.length > 1) ? arr[1].trim() : null;

		AbsCommand commandHandler;
		
		try {
			switch (command) {
				case "/getByName": {
					commandHandler = new GetByNameCommand(arguments);
				}
				break;
				case "/createNewService": {
					commandHandler = new CreateNewService(arguments);
				}
				break;
			
				default: {
					commandHandler = new UnknownCommand();
				}
				break;
			}
		}
		catch (IncorrectNumberOfArgumentsException ex) {
			commandHandler = new IncorrectNumberOfArgumentsCommand();
		}
		catch (NumberFormatException ex) {
			commandHandler = new NumberFormatCommand();
		}
		
		return commandHandler;
	}

}
