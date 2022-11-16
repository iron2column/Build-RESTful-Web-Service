package top.lessmore.demo.restservice;

/**
 * @author ChenMingYang
 * @date 2022-11-16 19:25
 */
public class Greeting {
    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
