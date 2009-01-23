/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.locke.library.utilities.io.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Streams {

	/**
	 * Writes the input stream to the output stream. Input is done without a
	 * Reader object, meaning that the input is copied in its raw form.
	 * 
	 * @param in
	 *            The input stream
	 * @param out
	 *            The output stream
	 * @return Number of bytes copied from one stream to the other
	 * @throws IOException
	 */
	public static int copy(final InputStream in, final OutputStream out,
			final long maximum) throws IOException, StreamTooLongException {
		final byte[] buffer = new byte[4096];
		int bytesCopied = 0;
		while (true) {
			int byteCount = in.read(buffer, 0, buffer.length);
			if (byteCount <= 0) {
				break;
			}
			out.write(buffer, 0, byteCount);
			bytesCopied += byteCount;
			if (bytesCopied > maximum) {
				throw new StreamTooLongException();
			}
		}
		return bytesCopied;
	}

}
