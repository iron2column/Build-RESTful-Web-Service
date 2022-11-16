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

上面的 Controller 虽然简单，但隐含了不少信息：

- **`@GetMapping` 注解**：此注解确保了 HTTP GET 请求 `/greeting` 被映射到 `greeting()` 方法上。

  > 还有更多同类型注解用于其他 HTTP 动作，如 `PostMapping` => `POST`。
  >
  > 它们是由 `@RequestMapping` 注解派生出来的，因此 `@RequestMapping(method=RequestMethod.GET)`等效于 `@GetMapping`。

- **`@RequestParam` 注解**：此注解用于将请求的查询字符串参数 `name` 与 `greeting()` 方法的形参 `name` 绑定。

  > `deafultValue` 将在请求缺省 `name` 时为形参注入指定的默认值

- **`@RestController` 注解**：此注解标记 class 为一个控制器，且限制每一个方法返回的是一个领域对象而不是一个视图（view）。

- **幕后英雄**：`Greeting` 对象必须被转换为 JSON，而开发者并不没有手动处理。这多亏于 Spring 的 HTTP 消息转换支持。由于
  Jackson 2 位于 classpath 上，所以 Spring 的 `MappingJackson2HttpMessageConverter` 能自动地将对象转换为 JSON。

  > `@RestController` 其实是一个简写，它包含了 `@Controller` 和 `@ResponseBody` 注解

## 探索：MVC 控制器 vs RESTful 控制器

传统的 MVC Controller 与 RESTful web service Controller 不同点在于：**HTTP 响应体被创建的方式**。

传统的 MVC 控制器是依靠视图技术将问候语数据进行服务端渲染，转为 HTML，而 RESTful web service
控制器则是填充并返回一个 `Greeting` 这类代表资源的对象。*且大多数情况下对数据将作为 JSON 直接写入 HTTP 响应。*

## 构建：📦 打包装箱为可执行 JAR

使用熟悉的打包构建方式，将此项目打包为 jar 包并运行
