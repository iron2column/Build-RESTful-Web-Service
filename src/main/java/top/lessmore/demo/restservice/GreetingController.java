package top.lessmore.demo.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenMingYang
 * @date 2022-11-16 19:41
 */
@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public Greeting greeting(String name) {
        return new Greeting(1, "Hello, " + name);
    }
}
