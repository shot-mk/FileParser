package com.parser;

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


public class GifUtil implements FileParser{
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

    public void showGIFInformation() throws IOException{
        try {
            checkGIF();
        } catch (NotAGifException e) {
            System.out.println("File is not GIF.");
            return;
        }
        getVersion();
        getWidth();
        getHeight();
        readFlagByte();
        if(flagByte / 128 == 1) {           // Взятие первого старшего бита.
            System.out.println("Image have global pallet.");
            getGlobalPalletSize();
            System.out.println("Global pallet size :" + globalPalletSize + " bytes.");

        } else {
            System.out.println("Image don`t have global pallet.");
        }
        getNumberOfFrames();
        System.out.println("Image have : " + numberOfFrames + " frames.");



    }

    private boolean checkFile (String path) throws IOException, NotAGifException {
        byte[] signature = new byte [3];
        in.read(signature);
        String signatureString = new String(signature);
        if(signatureString.equals("GIF"))
            this.signatureString = signatureString;
        else
            throw new NotAGifException();
    }

    private void getVersion() throws IOException {
        char[] version = new char [3];
        for(int i = 0; i < version.length; i++) {
            version[i] = (char) in.read();
        }
        String versionString = new String(version);
        System.out.println("Version : " + versionString);
    }

    private void getWidth() throws IOException {
        int[] width = new int[2];
        for(int i = 0; i < width.length; i++) {
            width[i] = in.read();
        }
        System.out.println("Image width : " + (width[0] + width[1]*256));
    }

    private void getHeight() throws IOException {
        int[] height = new int[2];
        for(int i = 0; i < height.length; i++) {
            height[i] = in.read();
        }
        System.out.println("Image height : " + (height[0] + height[1]*256));
    }

    private void readFlagByte() throws IOException {
        flagByte = in.read();
    }

    private void getGlobalPalletSize() {
        int globalPalletNumber = (flagByte & 7);
        globalPalletSize = (int) Math.pow(2, globalPalletNumber + 1) * 3;
    }

    private int getLocalPalletSize(int localFlagByte) {
        int localPalletNumber = (localFlagByte & 7);
        int localPalletSize = (int) Math.pow(2, localPalletNumber + 1) * 3;
        return localPalletSize;
    }

    private void getNumberOfFrames () throws IOException {
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

//    public boolean checkFile(String path) {
//        if(signatureString == "GIF")
//            return true;
//        else
//            return false;
//    }

    public String getFileSignature() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public FileInfo parse(String path) {
        GifInfo result = new GifInfo();

        return result;
    }


    class NotAGifException extends Exception{

        NotAGifException() {
        }

        NotAGifException(String message) {
            super(message);
        }

        NotAGifException(String message, Throwable cause) {
            super(message, cause);
        }

        NotAGifException(Throwable cause) {
            super(cause);
        }

        NotAGifException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
