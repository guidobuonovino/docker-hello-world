package docker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import docker.config.MainConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
@ContextConfiguration(classes = { MainConfig.class, TestConfiguration.class })
@ComponentScan
class MainApplicationTests {

	@Value("${i18n.default-locale}")
	private String defaultLocaleValue;

	private Locale defaultLocale;

	@Test
	void contextLoads() throws Exception {
		defaultLocale = Locale.forLanguageTag(defaultLocaleValue);
		assertThat(defaultLocale).isNotNull();
	}
}
