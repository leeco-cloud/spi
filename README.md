### 使用手册

#### 1. 业务身份定义
定义业务身份，即添加Identity注解，并声明身份code，该注解可以添加在任何类上，每一个业务身份有且进需要定义一次
```java
@Identity(code = "bizCode")
public class IdentityBiz {

}

@Identity(code = "bizCode2")
public class IdentityBiz2 {

}
```
#### 2. SPI定义
在需要声明为SPI的接口上添加SPI注解即可
```java
@Spi
public interface SpiTestInterface {

    String testMethod(String name);

}
```

#### 3. SPI实现定义
在实现了SPI接口的实现类上添加SpiProvider注解，并声明当前实现对应的是一个业务身份

且添加了该注解的实现类默认是注册成spring的bean，因此实现类内部可以使用@Autowired等spring能力
```java
@SpiProvider(identityCode = "bizCode")
public class SpiTestInterfaceImpl implements SpiTestInterface {

    @Override
    public String testMethod(String name) {
        return "bizCode testMethod";
    }

}
```

```java
@SpiProvider(identityCode = "bizCode2")
public class SpiTestInterfaceImpl implements SpiTestInterface {

    @Override
    public String testMethod(String name) {
        return "bizCode2 testMethod";
    }

}
```
#### 4. 产品叠加
// todo


#### 5. 调用执行

##### 5.1 spring用法
```java
@Component
public class SpiTestService {

    // 直接使用spring原生依赖注入即可
    @Autowired
    private SpiTestInterface spiTestInterface;

    public void testSpiSpringInvoke(){
        // 业务身份装填到当前线程上下文
        BizSession.IDENTITY.set(name);
        // 直接调用即可
        String result = spiTestInterface.testMethod("testParam");
        // 清理当前线程上下文的业务身份
        BizSession.IDENTITY.remove();
    }

}
```

##### 5.2 非spring用法
非spring用法：使用显示调用链方式，主动传入：业务身份、SPI接口、目标方法和入参，则可以获取对应业务身份目标下的具体实现

```java
String result = SpiInvoke
        .identity("bizCode")
        .invoke(SpiTestInterface.class)
        .executeGetResult(service -> service.testMethod("testParam"));
```
