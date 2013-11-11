package com.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 05.11.13
 * Time: 17:42
 * To change this template use File | Settings | File Templates.
 */
public class JavaClassUtil {
    String path;
    //FileInputStream in;
    int constantPullCount = 0;
    long constantPullSizeBytes = 0;
    long constantPullTable[][];


    public JavaClassUtil(String path) throws FileNotFoundException {
        this.path = path;
        //in = new FileInputStream(path);
    }

    public void showClassInformation() throws IOException {
        try {
            checkClass();
        } catch (NotAClassException e) {
            System.out.println("This file is not .class");
            return;
        }
        System.out.println("- - - - - - - - - - ");
        showVerison();
        checkConstantPullCount();
        constantPullScan();
        System.out.println("- - - - - - - - - - ");
        showThisClassName();
        showListOfFieldsAndMethods();

    }

    private void checkClass() throws NotAClassException, IOException {
        FileInputStream in = new FileInputStream(path);
        long classActualSignature = 0xCAFEBABE;
        long classSignature = readNumber(4, in);
        if (classActualSignature == classSignature) {
            System.out.println("This file is .class");
        } else {
            in.close();
            throw new NotAClassException();
        }
        in.close();
    }

    private void showVerison() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(4);
        int minorVersionInt = (int) readNumber(2, in);
        int majorVersionInt = (int) readNumber(2, in);
        System.out.println("Version : " + majorVersionInt + "." + minorVersionInt);
        in.close();
    }

    private void checkConstantPullCount() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(8);
        constantPullCount = (int) readNumber(2, in);
        System.out.println("constantPullCount : " + constantPullCount);
    }

    private long readNumber(int bytes, FileInputStream in) throws IOException {
        long result = 0;
        int[] numberArray = new int[bytes];
        for (int i = 0; i < bytes; i++) {
            numberArray[i] = in.read();
            numberArray[i] = numberArray[i] & 255;
            result = (result + numberArray[i] * (int) Math.pow(256, bytes - i - 1));
        }
        return result;
    }

    private void constantPullScan() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(10);
        long bytesSkipped = 10;
        constantPullTable = new long[constantPullCount][2];
        for (int i = 1; i < constantPullCount; i++) {
            int temp = in.read();
            bytesSkipped++;
            constantPullTable[i][0] = temp;
            switch (temp) {
                case 7:  // Class
                case 8:  //String
                case 16: // Method Type info
                    constantPullTable[i][1] = bytesSkipped;
                    in.skip(2);
                    bytesSkipped += 2;
                    break;
                case 9:  // Fieldref
                case 10: // Methodred
                case 11: // InterfaceMethodref
                case 3:  // Integer
                case 4:  // Float
                case 12: // Name and Type info
                case 18: // InvokeDynamic
                    constantPullTable[i][1] = bytesSkipped;
                    in.skip(4);
                    bytesSkipped += 4;
                    break;
                case 5:  // Long
                case 6:  // Double
                    constantPullTable[i][1] = bytesSkipped;
                    in.skip(8);
                    bytesSkipped += 8;
                    break;
                case 1:  // UTF8_info
                    constantPullTable[i][1] = bytesSkipped;
                    int length = (int) readNumber(2, in);
                    in.skip(length);
                    bytesSkipped += length + 2;
                    break;
                case 15: //Methodhandle info
                    constantPullTable[i][1] = bytesSkipped;
                    in.skip(3);
                    bytesSkipped += 3;
                    break;
            }

        }
        constantPullSizeBytes = bytesSkipped;
        in.close();
    }

    public void showConstPulltable() {
        for(int i = 1; i < constantPullCount; i++ ) {
            System.out.println(i + " : " + constantPullTable[i][0] + " byte " + (constantPullTable[i][1]));
        }
    }



    public void showThisClassName() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(constantPullSizeBytes+2);
        int thisClass = (int)readNumber(2,in) ;
        //System.out.println("thisClass = " + thisClass);
        in.close();
        in = new FileInputStream(path);
        in.skip(constantPullTable[thisClass][1]);
        int thisClassNameIndex = (int)readNumber(2,in) ;
       // System.out.println("thisClassNameIndex = " + thisClassNameIndex);
        in.close();
        in = new FileInputStream(path);
        in.skip(constantPullTable[thisClassNameIndex][1]);
        int length = (int)readNumber(2,in);
        byte [] name = new byte[length];
        in.read(name);
        String nameString = new String(name);
        System.out.println("Class Name: " + nameString);
        in.close();
    }

    public void showListOfFieldsAndMethods() throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(constantPullSizeBytes+6);
        int interfaceCount = (int) readNumber(2,in);
        in.skip(2 * interfaceCount);
        int fieldsCount = (int) readNumber(2,in);
        System.out.println("- - - - - - - - - - ");
        System.out.println("Fields : ");
        for (int i = 0; i < fieldsCount; i++) {
            showField(in);
        }
        System.out.println("- - - - - - - - - - ");
        int methodCount = (int) readNumber(2, in);
        System.out.println("- - - - - - - - - - ");
        System.out.println("Methods : ");
        for (int i = 0; i < methodCount; i++) {
            showMethod(in);
        }
        System.out.println("- - - - - - - - - - ");




    }


    public void showField(FileInputStream in) throws IOException {
        in.skip(2);
        int nameIndex = (int) readNumber(2,in);
        showFieldName(nameIndex);
        in.skip(2);
        int attributesCount = (int) readNumber(2,in);
        for (int i = 0; i < attributesCount; i++) {
            skipAtributeInfo(in);
        }
    }

    public void showMethod(FileInputStream in) throws IOException {
        in.skip(2);
        int nameIndex = (int) readNumber(2,in);
        showMethodName(nameIndex);
        in.skip(2);
        int attributesCount = (int) readNumber(2,in);
        for (int i = 0; i < attributesCount; i++) {
            skipAtributeInfo(in);
        }
    }
    public void skipAtributeInfo(FileInputStream in) throws IOException {
        in.skip(2);
        long attribtueLength = readNumber(4,in);
        in.skip(attribtueLength);
    }


    public void showFieldName(int nameIndex) throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(constantPullTable[nameIndex][1]);
        int length = (int)readNumber(2,in);
        byte [] name = new byte[length];
        in.read(name);
        String nameString = new String(name);
        System.out.println("Field Name : " + nameString);
        in.close();
    }

    public void showMethodName(int nameIndex) throws IOException {
        FileInputStream in = new FileInputStream(path);
        in.skip(constantPullTable[nameIndex][1]);
        int length = (int)readNumber(2,in);
        byte [] name = new byte[length];
        in.read(name);
        String nameString = new String(name);
        System.out.println("Method Name : " + nameString);
        in.close();
    }




    class NotAClassException extends Exception {


        NotAClassException() {
        }

        NotAClassException(String message) {
            super(message);
        }

        NotAClassException(String message, Throwable cause) {
            super(message, cause);
        }

        NotAClassException(Throwable cause) {
            super(cause);
        }

        NotAClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

}
