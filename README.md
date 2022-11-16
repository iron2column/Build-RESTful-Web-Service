# Spring Guide

# 构建 RESTful Web 服务

## 依赖

`Spring Web`

## 思考： service 的交互

- Service 会处理 `GET` 请求 `/greeting`，在请求字符串中可选 `name` 参数。

- `GET` 请求应该返回使用的 `200 OK` 响应，其中应该包含响应体的 JSON。

    ```json
    {
        "id": 1,
        "content": "Hello, World!"
    }
    ```

    - `id` 字段是用于问候语的唯一标识符
    - `content` 字段是表示问候语的文本

## 建模：创建表示问候语的资源表示类

`Greeting` 是一个普通的 Java 对象。但是它用于代表响应请求的**资源**。

```java
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
```

> Service 交互中要求响应应为 JSON 格式。
>
> Spring Web 已经将 [Jackson JSON](https://github.com/FasterXML/jackson) 库作为默认选择。
>
> Jackson JSON 会自动管理 `Greeting` 转为 JSON。

## 控制器：创建处理请求的 Controller

以 Spring 的方式构建的 RESTful web 服务，HTTP 请求是由**控制器（controller**）处理的。Controller
组件通过 **`@RestController` 注解**进行标识。

`GreetingController` 来处理 `GET` 请求`/greeting`，返回一个 `Greeting` 类的实例：

```java

@RestController
public class GreetingController {
    // 计数器（原子性）
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format("Hello, %s", name));
    }
}
```

上面的 Controller 虽然简单，但隐含了大量信息：

- **`@GetMapping` 注解**：此注解确保了 HTTP GET 请求 `/greeting` 被映射到 `greeting()` 方法上。

  > 还有更多同类型注解用于其他 HTTP 动作，如 `PostMapping` => `POST`。
  >
  > `@RequestMapping` 注解是它们从此注解派生出来的，因此 `@RequestMapping(method=GET)`

- 
