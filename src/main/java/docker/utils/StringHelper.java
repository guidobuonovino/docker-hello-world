package docker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringHelper {

	private static final Logger log = LoggerFactory.getLogger(StringHelper.class);

	private StringHelper() {
		// Auto-generated constructor stub
	}

	public static String toJsonString(Object o) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.error("Serialize {}: {}", o.getClass().getName(), e.getMessage());
			return o.getClass().getName() + "@" + Integer.toHexString(o.hashCode());
		}
	}
}
