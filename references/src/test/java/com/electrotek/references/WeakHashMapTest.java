package com.electrotek.references;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.WeakHashMap;

import org.junit.Test;

/**
 * Educational test to show how java.util.WeakHashMap works.
 * 
 * @author Viktoras Agejevas
 *
 */
public class WeakHashMapTest {
    
    @Test
    public void letsSeeIfWeakHashMapClearsItself() throws InterruptedException {       
        Map<MemoryBlackHole, Integer> map = new WeakHashMap<>();
        
        MemoryBlackHole fb1 = new MemoryBlackHole();
        MemoryBlackHole fb2 = new MemoryBlackHole();
        MemoryBlackHole fb3 = new MemoryBlackHole();
        MemoryBlackHole fb4 = new MemoryBlackHole();
        
        map.put(fb1, 1);
        map.put(fb2, 2);
        map.put(fb3, 3);
        map.put(fb4, 4);
        
        System.out.println(map);
        assertEquals(4, map.size());
        
        fb1 = null;
        fb2 = null;
        fb3 = null;
        
        System.gc();
        Thread.sleep(100);
        
        System.out.println(map);
        assertEquals(1, map.size());
        assertNotNull(map.get(fb4));
    }

}
