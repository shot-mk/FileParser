package com.parser.utils;

import com.parser.infos.FileInfo;
import com.parser.infos.GifInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 05.11.13
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */


public class GifUtil implements FileParser {
    String path;
    FileInputStream in;
    int flagByte; // Байт наличия глобальной палитры.
    int globalPalletSize = 0; // Размер глобальной палитры в байтах.
    int numberOfFrames = 0;
    String signatureString;

    public GifUtil(String path) throws FileNotFoundException{
        this.path = path;
        in = new FileInputStream(path);
    }

    public boolean checkFile (String path) throws IOException {
        FileInputStream in = new FileInputStream(path);
        byte[] signature = new byte [3];
        in.read(signature);
        String signatureString = new String(signature);
        in.close();
        if(signatureString.equals("GIF")) {
            this.signatureString = signatureString;
            return true;
        }
        return false;
    }

    private String getVersion() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(3);
        char[] version = new char [3];
        for(int i = 0; i < version.length; i++) {
            version[i] = (char) in.read();
        }
        String versionString = new String(version);
        in.close();
        return versionString;
    }

    private int getWidth() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(6);
        int[] width = new int[2];
        for(int i = 0; i < width.length; i++) {
            width[i] = in.read();
        }
        int widthInteger = (width[0] + width[1]*256);
        in.close();
        return widthInteger;
    }

    private int getHeight() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(8);
        int[] height = new int[2];
        for(int i = 0; i < height.length; i++) {
            height[i] = in.read();
        }
        int heightInteger = height[0] + height[1] * 256;
        in.close();
        return heightInteger;
    }

    private void readFlagByte() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(10);
        flagByte = in.read();
        in.close();
    }

    private int getGlobalPalletSize() {
        int globalPalletNumber = (flagByte & 7);
        globalPalletSize = (int) Math.pow(2, globalPalletNumber + 1) * 3;
        return globalPalletSize;
    }

    private int getLocalPalletSize(int localFlagByte) {
        int localPalletNumber = (localFlagByte & 7);
        int localPalletSize = (int) Math.pow(2, localPalletNumber + 1) * 3;
        return localPalletSize;
    }

    private void getNumberOfFrames () throws IOException {
        in.skip(11);
        in.skip(globalPalletSize);
        while(in.available() > 1) {
            int temp = in.read();
            skipExtension(temp);
            skipFrame(temp);
        }
    }

    private void skipExtension (int temp) throws IOException {
        if(temp == '!') {
            in.skip(1);
            temp = in.read();
            while(temp != 0) {
                in.skip(temp);
                temp = in.read();
            }
        }
    }

    private void skipFrame (int temp) throws IOException {
        if(temp == ',') {
            in.skip(8);
            int localFlagByte = in.read();
            if (localFlagByte / 128 == 1) {
                int localPalletSize = getLocalPalletSize(localFlagByte);
                in.skip(localPalletSize+1);
            }
            else {
                in.skip(1);
            }
            temp = in.read();
            while(temp != 0) {
                in.skip(temp);
                temp = in.read();
            }
            numberOfFrames++;
        }
    }

    public String getFileSignature() throws IOException {
        FileInputStream in = new FileInputStream(path);
        byte[] signature = new byte [3];
        in.read(signature);
        String signatureString = new String(signature);
        in.close();
        return signatureString;
    }

    public FileInfo parse(String path) throws IOException {
        if(checkFile(path) == false)
            return null;
        GifInfo result = new GifInfo();
        result.setSignature(getFileSignature());
        result.setVersion(getVersion());
        result.setWidth(getWidth());
        result.setHeight(getHeight());
        readFlagByte();
        if(flagByte / 128 == 1) {           // Взятие первого старшего бита.
            getGlobalPalletSize();
            result.setGlobalPalletSize(getGlobalPalletSize());
        }
        getNumberOfFrames();
        result.setNumberOfFrames(numberOfFrames);
        return result;
    }

}
