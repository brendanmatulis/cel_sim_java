package prototype.engine.TUI;

import java.util.List;

public interface CommandHandler {
    //Code gotten from John Kugelman (https://stackoverflow.com/questions/51201059/what-is-a-good-way-to-organize-commands-for-a-command-line-interface/51201985#51201985)

    void handle(List<String> args);
}
