package ru.dnoskov.juddiregistrator.logic.commands.errors;

import ru.dnoskov.juddiregistrator.logic.commands.AbsCommand;

public class NumberFormatCommand extends AbsCommand {

	@Override
	public String executeCommand() {
		return "Неправильный формат аргументов!";
	}

}
