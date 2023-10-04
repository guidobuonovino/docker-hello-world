package docker.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocaleConfig implements WebMvcConfigurer {

	@Value("${i18n.default-locale:it}")
	private String defaultLocaleValue;

	private Locale defaultLocale;

	@Value("${i18n.supported-locales:it,en}")
	private List<String> supportedLocalesValues;

	private List<Locale> supportedLocales = new ArrayList<>();

	@PostConstruct
	private void init() {
		supportedLocalesValues.forEach(l -> supportedLocales.add(Locale.forLanguageTag(l)));
		defaultLocale = Locale.forLanguageTag(defaultLocaleValue);
	}

	@Bean
	public AcceptHeaderLocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
		acceptHeaderLocaleResolver.setDefaultLocale(defaultLocale);
		acceptHeaderLocaleResolver.setSupportedLocales(supportedLocales);
		return acceptHeaderLocaleResolver;
	}

	@Bean
	public LocalValidatorFactoryBean validator(MessageSource messageSource) {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}
}
