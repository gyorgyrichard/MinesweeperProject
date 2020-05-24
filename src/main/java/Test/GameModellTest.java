package Test;

import org.example.GameModell;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;


public class GameModellTest {

    @Test
    public void neighbournumber() {
        GameModell gm = new GameModell("Test", "könnyű");
        gm.setBombs(new boolean[][]{
                // 0     1     2     3     4     5     6     7     8     9     10
                {false, false, false, false, false, false, false, false, false, false, false},    //0
                {false, false, false, false, false, false, false, true, false, false, true},      //1
                {false, false, false, true, false, false, true, false, false, false, false},      //2
                {false, false, false, false, false, false, false, false, false, false, false},    //3
                {false, false, false, false, false, true, false, false, false, false, false},     //4
                {false, true, false, false, true, false, false, false, false, false, true},       //5
                {false, false, false, false, false, false, false, false, false, false, false},    //6
                {false, false, false, false, false, false, false, false, false, false, false},    //7
                {false, false, false, true, false, false, false, false, false, false, false},     //8
                {false, false, false, true, false, false, false, false, false, false, false},     //9
                {false, true, false, false, false, false, false, false, false, false, false}});   //10

        assertEquals(0, gm.neighbournumber(6, 6));
        assertEquals(1, gm.neighbournumber(1, 3));
        assertEquals(2, gm.neighbournumber(4, 4));
        assertEquals(0, gm.neighbournumber(8, 8));
        assertEquals(2, gm.neighbournumber(8, 2));
        assertEquals(1, gm.neighbournumber(5, 9));
    }

    @Test
    public void getBomb() {
        GameModell gm = new GameModell("Test", "könnyű");
        gm.setBombs(new boolean[][]{
                // 0     1     2     3     4     5     6     7     8     9     10
                {false, false, false, false, false, false, false, false, false, false, false},    //0
                {false, false, false, false, false, false, false, true, false, false, true},      //1
                {false, false, false, true, false, false, true, false, false, false, false},      //2
                {false, false, false, false, false, false, false, false, false, false, false},    //3
                {false, false, false, false, false, true, false, false, false, false, false},     //4
                {false, true, false, false, true, false, false, false, false, false, true},       //5
                {false, false, false, false, false, false, false, false, false, false, false},    //6
                {false, false, false, false, false, false, false, false, false, false, false},    //7
                {false, false, false, true, false, false, false, false, false, false, false},     //8
                {false, false, false, true, false, false, false, false, false, false, false},     //9
                {false, true, false, false, false, false, false, false, false, false, false}});   //10

        assertEquals(true, gm.getBomb(5, 10));
        assertEquals(false, gm.getBomb(2, 2));
        assertEquals(true, gm.getBomb(5, 4));
        assertEquals(false, gm.getBomb(3, 8));
    }

    @Test
    public void setFlag() {
        GameModell gm = new GameModell("Test", "könnyű");

        gm.setFlagged(5, 6, true);
        assertEquals(true, gm.getFlagged(5, 6));

        gm.setFlagged(2, 2, true);
        assertEquals(true, gm.getFlagged(2, 2));

        gm.setFlagged(5, 6, false);
        assertEquals(false, gm.getFlagged(5, 6));

        gm.setFlagged(2, 2, false);
        assertEquals(false, gm.getFlagged(2, 2));
    }

    @Test
    public void gameModell() { //Runnable Lambda kifejezes
        assertThrows(InvalidParameterException.class, () -> new GameModell("Test", "Túlkönnyű"));
        assertThrows(InvalidParameterException.class, () -> new GameModell("Test", "Baby"));
        //assertThrows(InvalidParameterException.class, () -> new GameModell("Test", "könnyű"));
    }
}