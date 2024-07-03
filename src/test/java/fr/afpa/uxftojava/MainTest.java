package fr.afpa.uxftojava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class MainTest 
{

    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void normalizeString()
    {
        assertEquals("Testaaa", Class.normalizeString("_/Test     \t\na+aa/_", true, true, true, true, true, true));    
    }

}
