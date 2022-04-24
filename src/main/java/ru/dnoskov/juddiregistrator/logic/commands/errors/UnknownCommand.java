package ru.dnoskov.juddiregistrator.logic.commands.errors;

import ru.dnoskov.juddiregistrator.logic.commands.AbsCommand;

public class UnknownCommand extends AbsCommand {

	@Override
	public String executeCommand() {
		return "Неизвестная команда!";
	}

}
