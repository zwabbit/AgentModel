package agentsimulation;
/*************************************************************************
 *  Compilation:  javac Interval.java
 *  Execution:    java Interval
 *
 *  Implementation of an interval.
 *
 *************************************************************************/

public class Interval<Key extends Comparable> { 
    public final Key low;      // left endpoint
    public final Key high;     // right endpoint
   
    public Interval(Key low, Key high) {
        if (less(high, low)) throw new RuntimeException("Illegal argument");
        this.low  = low;
        this.high = high;
    }

    // is x between low and high
    public boolean contains(Key x) {
        return !less(x, low) && !less(high, x);
    }

    // does this interval a intersect interval b?
    public boolean intersects(Interval<Key> b) {
        Interval<Key> a  = this;
        if (less(a.high, b.low)) return false;
        if (less(b.high, a.low)) return false;
        return true;
    }

    // does this interval a equal interval b?
    public boolean equals(Interval<Key> b) {
        Interval<Key> a  = this;
        return a.low.equals(b.low) && a.high.equals(b.high);
    }


    // comparison helper functions
    private boolean less(Key x, Key y) {
        return x.compareTo(y) < 0;
    }

    // return string representation
    public String toString() {
        return "[" + low + ", " + high + "]";
    }



    // test client
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);

        Interval<Integer> a = new Interval<Integer>(5, 17);
        Interval<Integer> b = new Interval<Integer>(5, 17);
        Interval<Integer> c = new Interval<Integer>(5, 18);
        System.out.println(a.equals(b));
        System.out.println(!a.equals(c));
        System.out.println(!b.equals(c));


        // generate N random points in [-1, 2] and compute
        // fraction that lies in [0, 1]
        Interval<Double> interval = new Interval<Double>(0.0, 1.0);
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            Double x = 3 * Math.random() - 1.0;
            if (interval.contains(x)) cnt++;
        }
        System.out.println("fraction = " + (1.0 * cnt / N));
    }
}