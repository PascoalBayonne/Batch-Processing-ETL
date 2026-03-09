package pt.bayonne.sensei.futmanager;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FutmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FutmanagerApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
        return args -> {
            ChatClient chatClient = chatClientBuilder.build();

            String content = chatClient.prompt("Tell me a joke about football")
                    .call()
                    .content();

            System.out.println(content);
        };

    }

}
