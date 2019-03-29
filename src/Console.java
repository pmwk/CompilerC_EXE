package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Console {



    public void exec(String command) {
        System.out.println(command);
        try {

            Process process = Runtime.getRuntime().exec(command);
            InputStream is = process.getInputStream();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String result = br.lines().collect(Collectors.joining("\n"));
                System.out.println(result);
            }
            InputStream eis = process.getErrorStream();
            try(BufferedReader br = new BufferedReader(new InputStreamReader(eis))) {
                String result = br.lines().collect(Collectors.joining("\n"));
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
