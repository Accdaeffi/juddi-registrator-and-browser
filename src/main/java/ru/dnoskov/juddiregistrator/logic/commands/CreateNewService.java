package ru.dnoskov.juddiregistrator.logic.commands;

import ru.dnoskov.juddiregistrator.exception.IncorrectNumberOfArgumentsException;
import ru.dnoskov.juddiregistrator.integration.ServicePublisher;

public class CreateNewService extends AbsCommand {

	private final String businessKey;
	private final String serviceName;
	private final String serviceWsdl;
	
	public CreateNewService(String args) throws IncorrectNumberOfArgumentsException {
		String[] arguments = args.split(" ", 3);
		
		if (arguments.length != 3) {
			throw new IncorrectNumberOfArgumentsException();
		}
		
		businessKey = arguments[0].trim();
		serviceWsdl = arguments[1].trim();
		serviceName = arguments[2].trim();
	}

	@Override
	public String executeCommand() {
		StringBuilder sb = new StringBuilder();
		
		ServicePublisher sp = new ServicePublisher();
        sb.append(sp.publish(businessKey, serviceWsdl, serviceName));
		
		return sb.toString();
	}

}
