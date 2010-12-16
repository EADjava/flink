/***********************************************************************************************************************
 *
 * Copyright (C) 2010 by the Stratosphere project (http://stratosphere.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/

package eu.stratosphere.nephele.io.compression.library.lzo;

import eu.stratosphere.nephele.io.compression.CompressionException;
import eu.stratosphere.nephele.io.compression.CompressionLibrary;
import eu.stratosphere.nephele.io.compression.Compressor;
import eu.stratosphere.nephele.io.compression.Decompressor;
import eu.stratosphere.nephele.util.NativeCodeLoader;
import eu.stratosphere.nephele.util.StringUtils;

public class LzoLibrary implements CompressionLibrary {

	/**
	 * The file name of the native lzo library.
	 */
	private static final String NATIVELIBRARYFILENAME = "liblzocompression.so.1.0";

	public LzoLibrary(String nativeLibraryDir)
												throws CompressionException {

		if (!NativeCodeLoader.isLibraryLoaded(NATIVELIBRARYFILENAME)) {
			try {
				NativeCodeLoader.loadLibraryFromFile(nativeLibraryDir, NATIVELIBRARYFILENAME);

				LzoCompressor.initIDs();
				LzoDecompressor.initIDs();
			} catch (Exception e) {
				throw new CompressionException(StringUtils.stringifyException(e));
			}
		}
	}

	@Override
	public Compressor getCompressor() throws CompressionException {

		return new LzoCompressor();
	}

	@Override
	public Decompressor getDecompressor() throws CompressionException {

		return new LzoDecompressor();
	}

	@Override
	public int getUncompressedBufferSize(int compressedBufferSize) {

		/*
		 * Calculate size of compressed data buffer according to
		 * LZO Manual http://www.oberhumer.com/opensource/lzo/lzofaq.php
		 */
		return ((compressedBufferSize - 67) / 17) * 16;
	}

	@Override
	public String getLibraryName() {
		return "LZO";
	}
}
