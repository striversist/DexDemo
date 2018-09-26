package com.fs.dexdemo.utils;

import java.io.*;

// ----------------------------------------------------------------------------

/**
 * Sample usage
 * java -encrypt <dir with classes to be encrypted> <full class name1> <full class name1> ...
 */
public class Encrypter {

    public static final String USAGE = "usage: Encrypter " +
            "(" +
            "-encrypt <output dir> <file 1> <file 2> ..." +
            "-decrypt <output dir> <file 1> <file 2> ..." +
            ")";

    public static boolean TRACE = true;

    private static final String AES_KEY = "abcdefghabcdefgh";
    private static AESHelper sAESHelper = new AESHelper();

    public static void main(final String[] args) throws Exception {
        if (args.length == 1)
            throw new IllegalArgumentException(USAGE);

        if (args.length < 3)
            throw new IllegalArgumentException(USAGE);

        final File outputDirectory = new File(args[1]);
        if ("-encrypt".equals(args[0])) {
            for (int i = 2; i < args.length; ++i) {
                final File file = new File(args[i]);
                AESEncrypt(outputDirectory, file);
            }
        } else if ("-decrypt".equals(args[0])) {
            for (int i = 2; i < args.length; ++i) {
                final File file = new File(args[i]);
                AESDecrypt(outputDirectory, file);
            }
        } else {
            throw new IllegalArgumentException(USAGE);
        }
    }

    private static void cryptFile(File outputDirectory, File file) throws IOException {
        final byte[] classBytes;

        InputStream inputStream = null;
        final File srcFile = new File(outputDirectory, file.getPath());
        try {
            srcFile.getParentFile().mkdirs();

            inputStream = new FileInputStream(srcFile);

            classBytes = readFully(inputStream);
            crypt(classBytes);  //encrypt
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignore) {
                }
            }
        }

        OutputStream out = null;
        try {
            final File outputFile = new File(outputDirectory, file.getPath());
            outputFile.getParentFile().mkdirs();

            out = new FileOutputStream(outputFile);
            out.write(classBytes);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ignore) {
                }
            }
        }

        if (TRACE) System.out.println("encrypted [" + file + "]");
    }

    /**
     * De/encrypts binary data in a given byte array. Calling the method again
     * reverses the encryption.
     */
    private static void crypt(final byte[] data) {
        for (int i = 8; i < data.length; ++i) data[i] ^= 0x5A;
    }

    /**
     * Reads the entire contents of a given stream into a flat byte array.
     */
    private static byte[] readFully(final InputStream in)
            throws IOException {
        final ByteArrayOutputStream buf1 = new ByteArrayOutputStream();
        final byte[] buf2 = new byte[8 * 1024];

        for (int read; (read = in.read(buf2)) > 0; ) {
            buf1.write(buf2, 0, read);
        }

        return buf1.toByteArray();
    }

    private static void AESEncrypt(File outputDirectory, File file) {
        String sourceFilePath = file.getAbsolutePath();
        String destFilePath = outputDirectory.getAbsolutePath() + File.separator + file.getName() + ".encrypted";
        sAESHelper.encryptFile(AES_KEY, sourceFilePath, destFilePath);
    }

    private static void AESDecrypt(File outputDirectory, File file) {
        String sourceFilePath = file.getAbsolutePath();
        String destFilePath = outputDirectory.getAbsolutePath() + File.separator + file.getName() + ".decrypted";
        sAESHelper.decryptFile(AES_KEY, sourceFilePath, destFilePath);
    }
}