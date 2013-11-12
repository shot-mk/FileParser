package com.parser;
import com.parser.infos.FileInfo;
import org.junit.Test;



/**
 * Created with IntelliJ IDEA.
 * User: shot
 * Date: 12.11.13
 * Time: 18:54
 * To change this template use File | Settings | File Templates.
 */
public class ParserTest {
    @Test
    public void testParse() throws Exception {
        String path = "D:/gif.gif";
        Parser parser = new Parser(path);

        parser.initialize(path);
        FileInfo info = parser.parse(path);
        for(String c : info.getFileData()){
            System.out.println(c);
        }
    }
}
