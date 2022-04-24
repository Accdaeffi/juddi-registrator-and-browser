package ru.dnoskov.juddiregistrator.logic.commands.errors;

import ru.dnoskov.juddiregistrator.logic.commands.AbsCommand;

public class IncorrectNumberOfArgumentsCommand extends AbsCommand {

	@Override
	public String executeCommand() {
		return "Неправильное количество аргументов!";
	}

}
