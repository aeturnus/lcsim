/*
 * The IComponent specifies an interface for objects that act as components for the Little Computer system. Each must act on a clock cycle
 */

///*
package lcsimlib;
public abstract class Component 
{
    private long totalTime;
    private long cycles;
    protected String name;
    
    Component()
    {
        totalTime = 0;
        cycles = 0;
    }
    
    abstract public void init(LCSystem system);
    abstract protected void cycleInternal();
    
    abstract public void openConfig();
    abstract public void show();
    
    final public void cycle()
    {
        long start = System.nanoTime();
        cycleInternal();
        long end = System.nanoTime();
        ++cycles;
        totalTime += end - start;
    }
    final public void clearTotalTime()
    {
        totalTime = 0;
    }
    final public void clearCycles()
    {
        cycles = 0;
    }
    final public long getCycles() { return cycles;}
    final public long getTotalTime() { return totalTime;}
    final public long getAverageTime() { return cycles!=0?totalTime/cycles:0;}
    final public String getName(){return name;}
}
//*/
/*
package lcsimlib;
public abstract class Component 
{
    private long averageTime;
    private int cycles;
    protected String name;
    
    Component()
    {
        averageTime = 0;
        cycles = 0;
    }
    
    abstract public void init(LCSystem system);
    abstract public void cycle();
    
    abstract public void openConfig();
    abstract public void show();
    
    final public void profileCycle()
    {
        long start = System.nanoTime();
        cycle();
        long end = System.nanoTime();
        ++cycles;
        averageTime = (averageTime + end - start)/(cycles>1?2:1);
    }
    final public void clearAverageTime()
    {
        averageTime = 0;
    }
    final public long getAverageTime() { return averageTime;}
    final public String getName(){return name;};
}
*/