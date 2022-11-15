# shorteningURL
A simple URL shortening Service by Spring Boot

You need the following tools and technologies to develop the same.
- Spring-Boot 2.7.3
- Springfox-swagger2 2.9.2
- Lombok 1.18.8
- JavaSE 11
- Maven 3.3.9

# Dependencies
Open the pom.xml file for spring-aop configuration:

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.3</version>
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
