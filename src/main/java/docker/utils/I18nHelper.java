package docker.utils;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nHelper {

	private static final Logger logger = LoggerFactory.getLogger(I18nHelper.class);

	@Value("${i18n.default-locale:it}")
	private String defaultLocaleValue;

	private Locale defaultLocale;

	@Autowired
	private MessageSource message;

	@PostConstruct
	private void init() {
		defaultLocale = Locale.forLanguageTag(defaultLocaleValue);
	}

	public String getMessage(String propertyPath) {
		try {
			return message.getMessage(propertyPath, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			printWarningLog(propertyPath, LocaleContextHolder.getLocale());
			try {
				return message.getMessage(propertyPath, null, defaultLocale);
			} catch (NoSuchMessageException ex) {
				return propertyPath;
			}
		}
	}

	public String getMessage(String propertyPath, Locale locale) {
		try {
			if (locale == null)
				locale = LocaleContextHolder.getLocale();
			return message.getMessage(propertyPath, null, locale);
		} catch (NoSuchMessageException e) {
			printWarningLog(propertyPath, LocaleContextHolder.getLocale());
			try {
				return message.getMessage(propertyPath, null, defaultLocale);
			} catch (NoSuchMessageException ex) {
				return propertyPath;
			}
		}
	}

	public String getMessage(String propertyPath, Object... params) {
		try {
			return message.getMessage(propertyPath, params, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			printWarningLog(propertyPath, LocaleContextHolder.getLocale());
			try {
				return message.getMessage(propertyPath, params, defaultLocale);
			} catch (NoSuchMessageException ex) {
				return propertyPath;
			}
		}
	}

	public String getMessage(String propertyPath, Object param) {
		Object[] params = null;
		if (param != null)
			params = new Object[] { param };
		try {
			return message.getMessage(propertyPath, params, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			printWarningLog(propertyPath, LocaleContextHolder.getLocale());
			try {
				return message.getMessage(propertyPath, params, defaultLocale);
			} catch (NoSuchMessageException ex) {
				return propertyPath;
			}
		}
	}

	public String getMessage(String propertyPath, Locale locale, Object... params) {
		try {
			if (locale == null)
				locale = LocaleContextHolder.getLocale();
			return message.getMessage(propertyPath, params, locale);
		} catch (NoSuchMessageException e) {
			printWarningLog(propertyPath, LocaleContextHolder.getLocale());
			try {
				return message.getMessage(propertyPath, params, defaultLocale);
			} catch (NoSuchMessageException ex) {
				return propertyPath;
			}
		}
	}

	protected void printWarningLog(String propertyPath, Locale locale) {
		logger.warn("propertyPath '{}' non trovato per il Locale {}", propertyPath, locale);
	}
}
