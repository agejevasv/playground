package com.electrotek.references;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Educational test to show how SoftHashMap works.
 * 
 * @author Viktoras Agejevas
 *
 */
public class SoftHashMapTest {

    @Test
    public void letsSeeIfSoftHashMapClearsItself() throws InterruptedException {       
        Map<Integer, FatBastard> map = new SoftHashMap<>();

        FatBastard fb1 = new FatBastard();
        FatBastard fb2 = new FatBastard();
        FatBastard fb3 = new FatBastard();
        FatBastard fb4 = new FatBastard();
        
        map.put(1, fb1);
        map.put(2, fb2);
        map.put(3, fb3);
        map.put(4, fb4);
        
        System.out.println(map);
        assertEquals(4, map.size());
        
        fb1 = null;
        fb2 = null;
        fb3 = null;
        
        System.gc();
        Thread.sleep(100);
        System.out.println(map);
        assertEquals(4, map.size());
        
        List<byte[]> eatMemory = new ArrayList<>();
        
        while (map.size() != 1) {
            eatMemory.add(new byte[1024]);
        }

        System.out.println(map);
        assertEquals(fb4, map.get(4));
    }
}
