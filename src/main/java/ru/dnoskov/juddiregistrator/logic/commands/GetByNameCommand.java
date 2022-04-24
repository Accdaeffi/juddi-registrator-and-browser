package ru.dnoskov.juddiregistrator.logic.commands;

import ru.dnoskov.juddiregistrator.exception.IncorrectNumberOfArgumentsException;
import ru.dnoskov.juddiregistrator.integration.Browser;

public class GetByNameCommand extends AbsCommand {
	
	private final String serviceName;

	public GetByNameCommand(String args) throws IncorrectNumberOfArgumentsException {
		String[] arguments = args.split(" ");
		
		if (arguments.length != 1) {
			throw new IncorrectNumberOfArgumentsException();
		}
		
		this.serviceName = arguments[0];
	}

	@Override
	public String executeCommand() {
		StringBuilder sb = new StringBuilder();
		
        Browser br = new Browser();
        sb.append(br.getServiceDescriptionByName(serviceName));

		return sb.toString();
	}


}
