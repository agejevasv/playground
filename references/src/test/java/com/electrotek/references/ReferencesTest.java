package com.electrotek.references;
import static org.junit.Assert.*;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.electrotek.references.MemoryBlackHole;

/**
 * Educational test to show how Soft and Weak references work.
 * 
 * @author Viktoras Agejevas
 *
 */
public class ReferencesTest {

    List<byte[]> iLikeToEatMemory;
    ReferenceQueue<MemoryBlackHole> softQueue;
    ReferenceQueue<MemoryBlackHole> weakQueue;
    SoftReference<MemoryBlackHole> soft;
    WeakReference<MemoryBlackHole> weak;
    
    @Before
    public void init() {
        iLikeToEatMemory = new ArrayList<>();
        softQueue = new ReferenceQueue<>();
        weakQueue = new ReferenceQueue<>();
        soft = new SoftReference<>(new MemoryBlackHole(), softQueue);
        weak = new WeakReference<>(new MemoryBlackHole(), weakQueue);
        printOut();
    }
    
    @Test
    public void letsSeeWhenReferencesAreCollected() throws InterruptedException {
        assertNotNull(soft.get());
        assertNotNull(weak.get());

        System.gc();
        Thread.sleep(500);
        printOut();
        
        assertNotNull(soft.get());
        assertNull(weak.get());

        for (int i = 0; i <= 1024 * 1024; i++) {
            iLikeToEatMemory.add(new byte[1024]);
            if (soft.get() == null) {
                printOut();
                break;
            }
        }
        
        assertNull(soft.get());
    }

    private void printOut() {
        System.out.println("Soft queue (object is GCed): " + softQueue.poll());
        System.out.println("Weak queue (object is GCed): " + weakQueue.poll());
        System.out.println("Soft ref: " + soft.get());
        System.out.println("Weak ref: " + weak.get());
        System.out.println();
    }
}
