package ru.test.load;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@SpringBootApplication
@RestController
public class Main {


    public static int rTimer;

    static {
        try {
            rTimer = Integer.parseInt(config("time"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Main() throws FileNotFoundException {
    }

    public static void main(String[] args) throws Exception {

        System.setProperty("server.port", config("port"));

        SpringApplication.run(Main.class, args);
    }

    @RequestMapping("/**")
    public String getAny() {
        return ("");
    }

    @GetMapping("/hello")
    public String getBody(@RequestParam(value = "name", defaultValue = "World") String name,
                          @RequestHeader(value = "hashid", defaultValue = "111111") String hashid,
                          HttpServletResponse response) {
        myTimer();
        response.addHeader("Hashid", hashid);
        return String.format("You request GET - %s! Hashid from header - %s", name, hashid);
    }

    @GetMapping("/hello/{id}")
    public String getBodyId(@PathVariable String id) {
        myTimer();
        return String.format("You request GET - %s! Hashid from header - %s", id);
    }

    @PostMapping(value = "/hello")
    public String postBody() {
        myTimer();
        return String.format("You request POST - WORLD! Timer = %d", rTimer);
    }

    private void myTimer() {
        try {
            Thread.sleep(rTimer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String config(String str) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("application.yml"));
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        System.out.println("Config = " + data);
        String s = data.get(str).toString();
        System.out.println(str + " - " + s);
        return s;
    }


}
