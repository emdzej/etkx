package pl.etkx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.etkx")
public class EtkxApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtkxApplication.class, args);
    }
}
