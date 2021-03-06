# feign-form-content-type
This is created from feign-form code base,gives flexibility to provide content types for each attachements individually

## How to define each multipart content type

```java

  @RequestLine("POST /root/test")
  @Headers({
    "Content-Type: multipart/form-data",
    "Accept: */*",
    "X-Auth-Username: {xAuthUsername}"
  })
  Long createPostExample(@Param("xAuthUsername") String xAuthUsername, @Param("category;application/json") Object dao, @Param("img;image/jpeg") Object img);

```

  `@Param("category;application/json") Object category` Has `semicolon(;)` seperated value which is `"{name};{content-type}"`

# Below are from feign-form
# Form Encoder

This module adds support for encoding **application/x-www-form-urlencoded** and **multipart/form-data** forms.

## Add dependency

Include the dependency to your project's pom.xml file:
```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.github.openfeign.form</groupId>
        <artifactId>feign-form</artifactId>
        <version>3.0.3</version>
    </dependency>
    ...
</dependencies>
```

## Usage

Add `FormEncoder` to your `Feign.Builder` like so:

```java
SomeApi github = Feign.builder()
                     .encoder(new FormEncoder())
                     .target(SomeApi.class, "http://api.some.org");
```

Moreover, you can decorate the existing encoder, for example JsonEncoder like this:

```java
SomeApi github = Feign.builder()
                     .encoder(new FormEncoder(new JacksonEncoder()))
                     .target(SomeApi.class, "http://api.some.org");
```

And use them together:

```java
interface SomeApi {

    @RequestLine("POST /json")
    @Headers("Content-Type: application/json")
    void json (Dto dto);

    @RequestLine("POST /form")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    void from (@Param("field1") String field1, @Param("field2") String field2);

}
```

You can specify two types of encoding forms by `Content-Type` header.

### application/x-www-form-urlencoded

```java
interface SomeApi {

    ...

    @RequestLine("POST /authorization")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    void authorization (@Param("email") String email, @Param("password") String password);

    ...

}
```

### multipart/form-data

```java
interface SomeApi {

    ...

    @RequestLine("POST /send_photo")
    @Headers("Content-Type: multipart/form-data")
    void sendPhoto (@Param("is_public") Boolean isPublic, @Param("photo") File photo);

    ...

}
```

In example above, we send file in parameter named **photo** with additional field in form **is_public**.

> **IMPORTANT:** You can specify your files in API method by declaring type **File** or **byte[]**.

### Spring MultipartFile and Spring Cloud Netflix @FeignClient support

You can also use Form Encoder with Spring `MultipartFile` and `@FeignClient`.

Include the dependencies to your project's pom.xml file:
```xml
<dependencies>
    ...
    <dependency>
        <groupId>io.github.openfeign.form</groupId>
        <artifactId>feign-form</artifactId>
        <version>3.0.3</version>
    </dependency>
    <dependency>
        <groupId>io.github.openfeign.form</groupId>
        <artifactId>feign-form-spring</artifactId>
        <version>3.0.3</version>
    </dependency>
    ...
</dependencies>
```

```java
@FeignClient(name = "file-upload-service", configuration = FileUploadServiceClient.MultipartSupportConfig.class)
public interface FileUploadServiceClient extends IFileUploadServiceClient {

    public class MultipartSupportConfig {

        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }
}
```

Or, if you don't need Spring's standard encoder:

```java
@FeignClient(name = "file-upload-service", configuration = FileUploadServiceClient.MultipartSupportConfig.class)
public interface FileUploadServiceClient extends IFileUploadServiceClient {

    public class MultipartSupportConfig {

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }
}
```
