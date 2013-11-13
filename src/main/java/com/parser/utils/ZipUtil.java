package com.parser.utils;

import com.parser.infos.FileInfo;
import com.parser.infos.ZipInfo;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 09.11.13
 * Time: 9:57
 * To change this template use File | Settings | File Templates.
 */
public class ZipUtil implements FileParser{
    FileInputStream in;
    String signature;
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

    private void checkZip() throws IOException{
        int signature = (int) readNumber(4,in);
        if (signature == 0x04034b50) {
            this.signature = "zip";
        }
    }

    private String showCompressionMetod() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(8);
        int compMetod = (int) readNumber(2,in);
        in.close();
        return compressionMethod[compMetod];
    }

    private String showFileName() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(26);
        int fileNameLength = (int) readNumber(2,in);
        in.skip(2);
        byte[] fileNameByteArray = new byte[fileNameLength];
        in.read(fileNameByteArray);
        fileName = new String(fileNameByteArray);
        in.close();
        return fileName;
    }

     private List<String> showListOfFiles() throws IOException {
         LinkedList<String> listOfFiles = new LinkedList<String>();
         while(skipToFile()) {
             in.skip(22);
             int fileNameLength = (int) readNumber(2, in);
             in.skip(2);
             byte[] fileNameByteArray = new byte[fileNameLength];
             in.read(fileNameByteArray);
             String fileName = new String(fileNameByteArray);
             if(fileName.charAt(fileName.length()-1) != '/')
                listOfFiles.add(fileName);
         }
         return listOfFiles;
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

    @Override
    public boolean checkFile(String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        int signature = (int) readNumber(4,in);
        in.close();
        if (signature == 0x04034b50)
           return true;
        return false;
    }

    @Override
    public String getFileSignature() throws IOException {
        return null;
    }

    @Override
    public FileInfo parse(String path) throws IOException {
        if (checkFile(path) == false)
            return null;
        ZipInfo result = new ZipInfo();
        checkZip();
        result.setSignature(signature);
        result.setCompressionMethod(showCompressionMetod());
        result.setFileName(showFileName());
        result.setFiles(showListOfFiles());
        return result;
    }



}
