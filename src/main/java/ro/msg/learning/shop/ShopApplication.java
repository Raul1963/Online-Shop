package ro.msg.learning.shop;

import jakarta.mail.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ro.msg.learning.shop.config.RsaConfigProperties;

import java.util.Properties;

@SpringBootApplication
@EnableConfigurationProperties(RsaConfigProperties.class)
@EnableScheduling
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
