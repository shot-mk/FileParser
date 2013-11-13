package com.parser;
import com.parser.infos.FileInfo;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 12.11.13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class ParserTest {


    @Test(expected = FileNotFoundException.class)
    public void testParseMethodFileNotFound() throws  IOException {
        String testPath = "D:/file.file";
        Parser parser = new Parser(testPath);
    }

    @Test(expected = IOException.class)
    public void testParseMethodIOException() throws IOException {
        String testPath = "D:/testfileMy";
        Parser parser = new Parser(testPath);
        FileInfo info = parser.parse();

    }


    @Test
    public void testParse() throws Exception {
        String path = "D:/test.zip";
        Parser parser = new Parser(path);
        FileInfo info = parser.parse();
        for(String c : info.getFileData()){
            System.out.println(c);
        }
    }
}
