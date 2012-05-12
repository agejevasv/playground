package com.electrotek.references;

/**
 * Dummy memory hungry POJO. Used to demonstrate how Soft and Weak references 
 * behave. Eats up 5MB during creation.
 * 
 * @author Viktoras Agejevas
 *
 */
class FatBastard {
    
    @SuppressWarnings("unused")
    private byte[] weight = new byte[1024 * 1024 * 5];
    
}