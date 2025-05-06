/**
 * Job Enum of the Perils Along the Platte Game
 * Represents the various occupations available to the player.
 * Each job provides unique bonuses and abilities that affect gameplay.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Job.java
 */

public enum Job {
    /** 
     * Farmer: Specializes in food management and spoilage reduction.
     * Provides a 25% reduction in food spoilage rate.
     */
    FARMER,
    
    /** 
     * Blacksmith: Expert in wagon maintenance and part durability.
     * Provides a 25% reduction in wagon part breakage.
     */
    BLACKSMITH,
    
    /** 
     * Carpenter: Skilled in wagon repairs and construction.
     * Provides a 25% reduction in wagon repair costs.
     */
    CARPENTER,
    
    /** 
     * Merchant: Proficient in trading and resource management.
     * Provides a 10% discount when buying and 10% bonus when selling.
     */
    MERCHANT,
    
    /** 
     * Doctor: Specializes in health management and medical care.
     * Provides a 25% increase in medical treatment effectiveness.
     */
    DOCTOR,
    
    /** 
     * Hunter: Expert in hunting and food acquisition.
     * Provides a 25% increase in hunting success rate.
     */
    HUNTER,
    
    /** 
     * Teacher: Focuses on morale management and education.
     * Provides a 25% reduction in morale loss.
     */
    TEACHER,
    
    /** 
     * Preacher: Specializes in morale and spiritual guidance.
     * Provides a 25% reduction in morale loss.
     */
    PREACHER
}