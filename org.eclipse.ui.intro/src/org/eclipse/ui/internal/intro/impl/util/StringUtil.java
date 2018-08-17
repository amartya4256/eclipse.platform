/*******************************************************************************
 * Copyright (c) 2004, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.intro.impl.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class StringUtil {

    public static String concat(String... strings) {
    	StringBuilder buffer = new StringBuilder();
    	for (String string : strings) {
			buffer.append(string);
		}
    	return buffer.toString();
    }

	public static String decode(String s, String enc) throws UnsupportedEncodingException {
		try {
			return URLDecoder.decode(s, enc);
		}
		catch (Exception ex) {
			// fall back to original string
			return s;
		}
	}

    // Removes leading and trailing whitespace and replaces other
    // occurrences with a single space.

	public static String normalizeWhiteSpace(String input) {
		if (input == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		boolean atStart = true;
		boolean whitespaceToInsert = false;
		for (int i = 0; i < input.length(); i++) {
			char next = input.charAt(i);
			if (Character.isWhitespace(next)) {
				if (!atStart) {
					whitespaceToInsert = true;
				}
			} else {
				if (whitespaceToInsert) {
					result.append(' ');
					whitespaceToInsert = false;
				}
				atStart = false;
				result.append(next);
			}
		}
		return result.toString();
	}

}
