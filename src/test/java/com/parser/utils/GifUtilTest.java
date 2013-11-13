package com.parser.utils;

import org.junit.Test;
import static  org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 13.11.13
 * Time: 14:50
 * To change this template use File | Settings | File Templates.
 */
public class GifUtilTest {

    @Test
     public void testCheckFileMethod() throws IOException {
        String path = new String("D:/gif.gif");
        GifUtil testUtil = new GifUtil(path);
        boolean result = testUtil.checkFile(path);
        assertTrue(result);
    }

    @Test
     public  void testCheckFileMethodOnFalse() throws IOException {
        String path = new String("D:/TestClass1.class");
        GifUtil testUtil = new GifUtil(path);
        boolean result = testUtil.checkFile(path);
        assertFalse(result);
    }

    @Test (expected = FileNotFoundException.class)
    public  void testCheckFileFileNotFoundException() throws IOException {
        String path = new String("D:/file.file");
        GifUtil testUtil = new GifUtil(path);
        boolean result = testUtil.checkFile(path);
        assertFalse(result);
    }


}
