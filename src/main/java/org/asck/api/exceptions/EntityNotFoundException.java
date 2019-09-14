package org.asck.api.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.util.StringUtils;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -1143994718314529924L;

	public EntityNotFoundException(Class<?> clazz, String... searchParamsMap) {
		super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), toMap(searchParamsMap)));
	}

	private static String generateMessage(String entity, Map<String, String> searchParams) {
		return StringUtils.capitalize(entity) + " was not found for parameters " + searchParams;
	}

	private static Map<String, String> toMap(String... entries) {
		if (entries.length % 2 == 1)
			throw new IllegalArgumentException("Invalid entries");
		return IntStream.range(0, entries.length / 2).map(i -> i * 2).collect(HashMap::new,
				(m, i) -> m.put(entries[i], entries[i + 1]), Map::putAll);
	}

}
