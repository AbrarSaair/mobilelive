package com.mobilelive.etee.mobilelive.parser;

import java.io.BufferedReader;

/**
 * The interface Parser.
 */
public interface IParser {
	/**
	 * Parse data object.
	 *
	 * @param object the object
	 * @return the object
	 */
	Object parseData(Object object);

	/**
	 * Parse input stream object.
	 *
	 * @param bufferedReader the buffered reader
	 * @return the object
	 */
	Object parseInputStream(BufferedReader bufferedReader);
}
