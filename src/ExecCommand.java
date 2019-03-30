package src;

public class ExecCommand {

    public ExecCommand(String command, String answer, String error) {
        this.command = command;
        this.answer = answer;
        this.error = error;
    }

    private final String command;
    private final String answer;
    private final String error;

    public String getCommand() {
        return command;
    }

    public String getAnswer() {
        return answer;
    }

    public String getError() {
        return error;
    }
}
