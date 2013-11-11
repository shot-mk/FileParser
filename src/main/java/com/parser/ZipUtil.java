package com.parser;

import java.io.*;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 09.11.13
 * Time: 9:57
 * To change this template use File | Settings | File Templates.
 */
public class ZipUtil {
    FileInputStream in;
    String path;
    String fileName;
    String[] compressionMethod = {
            "The file is stored (no compression)",
            "The file is Shrunk",
            "The file is Reduced with compression factor 1",
            "The file is Reduced with compression factor 2",
            "The file is Reduced with compression factor 3",
            "The file is Reduced with compression factor 4",
            "The file is Imploded",
            "Reserved for Tokenizing compression algorithm",
            "The file is Deflated",
            "Enhanced Deflating using Deflate64(tm)",
            "PKWARE Data Compression Library Imploding",
            "Reserved by PKWARE",
            "File is compressed using BZIP2 algorithm"
    };


    public ZipUtil(String path) throws IOException {
        this.path = path;
        in = new FileInputStream(path);
    }

    private long readNumber(int bytes, FileInputStream in) throws IOException {
        long result = 0;
        int[] numberArray = new int[bytes];
        for (int i = 0; i < bytes; i++) {
            numberArray[i] = in.read();
        }
        for(int i = bytes - 1; i >= 0; i --) {
            numberArray[i] = numberArray[i] & 255;
            result = (result + numberArray[i] * (int) Math.pow(256, i));
        }
        return result;
    }

    public void showZipInformation() throws IOException {
        try{
            checkZip();
        } catch (NotAZipException e) {
            System.out.println("FIle is not zip.");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
        showCompressionMetod();
        showFileName();
        showListOfFiles();
    }

    private void checkZip() throws IOException, NotAZipException {
        int signature = (int) readNumber(4,in);
        if (signature == 0x04034b50) {
            System.out.println("File is zip.");
        } else {
            throw new NotAZipException();
        }
    }

    private void showCompressionMetod() throws IOException {
        in.skip(4);
        int compMetod = (int) readNumber(2,in);
        System.out.println("Compression metod : " + compressionMethod[compMetod]);
    }

    private void showFileName() throws IOException {
        in.skip(16);
        int fileNameLength = (int) readNumber(2,in);
        in.skip(2);
        byte[] fileNameByteArray = new byte[fileNameLength];
        in.read(fileNameByteArray);
        fileName = new String(fileNameByteArray);
        System.out.println("File name : " + fileName);
    }

     private void showListOfFiles() throws IOException {
         System.out.println("- - - - - - - - - - - - - - - - ");
         System.out.println("Files : ");
         LinkedList<String> listOfFiles = new LinkedList<String>();
         while(skipToFile()) {
             in.skip(22);
             int fileNameLength = (int) readNumber(2, in);
             in.skip(2);
             byte[] fileNameByteArray = new byte[fileNameLength];
             in.read(fileNameByteArray);
             String fileName = new String(fileNameByteArray);
             listOfFiles.add(fileName);
         }
         for(String c : listOfFiles) {
             if(!(c.charAt(c.length() - 1) == '/'))
                System.out.println(c);
         }
         System.out.println("- - - - - - - - - - - - - - - - ");
     }

    private boolean skipToFile() throws IOException {
        int byte1 = 0, byte2 = 0, byte3 = 0, byte4 = 0;
        while (in.available() > 0) {
            if (byte1 == 0x50 && byte2 == 0x4B && byte3 == 0x03 && byte4 == 0x04)
                return true;
            if (byte1 == 0x50 && byte2 == 0x4B && (byte3 == 0x06 || byte3 == 0x01) && (byte4 == 0x08 || byte4 == 0x02))
                return false;
            byte1 = byte2;
            byte2 = byte3;
            byte3 = byte4;
            byte4 = in.read();
        }
        return false;
    }


    class NotAZipException extends Exception{
        NotAZipException() {
        }

        NotAZipException(String message) {
            super(message);
        }

        NotAZipException(String message, Throwable cause) {
            super(message, cause);
        }

        NotAZipException(Throwable cause) {
            super(cause);
        }

        NotAZipException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
