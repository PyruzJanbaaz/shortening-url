# URL Shortening Service
A simple URL shortening Service by Spring Boot

You need the following tools and technologies to develop the same.
- Spring-Boot 3.1.2
- Springfox-swagger 2.0.4
- Lombok 1.18.30
- MapStruct 1.5.5.Final
- JavaSE 17
- Maven 3.3.9

# Dependencies
Open the pom.xml file for spring-aop configuration:

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.2</version>
        <relativePath/> <!-- lookup parent from repository -->
      </parent>
      
and dpendencies:

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>


# Usage
Run the project and go to http://localhost:7777/swagger-ui.html on your browser!
