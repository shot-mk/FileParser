package com.parser.infos;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nikita
 * Date: 11.11.13
 * Time: 19:57
 * To change this template use File | Settings | File Templates.
 */
public class GifInfo implements FileInfo {
    private String version;
    private int width;
    private int height;
    private String signature;
    private int globalPalletSize;
    private int numberOfFrames;



    public List<String> getFileData() {
        List<String> mylist = new LinkedList<String>();
        mylist.add("File is " + signature);
        mylist.add("Version : " + version);
        mylist.add("Width : " + width);
        mylist.add("Height : " + height);
        mylist.add("Global pallet size : " + globalPalletSize);
        mylist.add("Number of frames : " + numberOfFrames);
        return mylist;
    }

    public String getFileSignature() {
        return signature;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setGlobalPalletSize(int globalPalletSize) {
        this.globalPalletSize = globalPalletSize;
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    public String getVersion() {
        return version;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getGlobalPalletSize() {
        return globalPalletSize;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }
}
