package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Console {

    private ArrayList<ExecCommand> commands_list = new ArrayList<>();

    private Process process;

    public void exec(String command) {
        try {
            process = Runtime.getRuntime().exec(command);

            commands_list.add(new ExecCommand(command, createAnswer(), createError()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createAnswer() {
        if (process != null) {
            InputStream is = process.getInputStream();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String result = br.lines().collect(Collectors.joining("\n"));
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return "something terrible happened in the console\n" + e.toString();
            }
        } else {
            return "process null";
        }

    }

    private String createError() {
        if (process != null) {
            InputStream is = process.getErrorStream();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String result = br.lines().collect(Collectors.joining("\n"));
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return "something terrible happened in the console\n" + e.toString();
            }
        } else {
            return "process null";
        }
    }

    public ExecCommand getLastExecCommand() {
        return getExecCommand(commands_list.size() - 1);
    }

    public ExecCommand getExecCommand(int i) {
        if (i >= commands_list.size() || i < 0) {
            return null;
        } else {
            return commands_list.get(i);
        }
    }

    public void close() {
    }
}
