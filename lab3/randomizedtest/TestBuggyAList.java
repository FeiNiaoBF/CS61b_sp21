package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> anr = new AListNoResizing<>();
        BuggyAList<Integer> ba = new BuggyAList<>();
        for(int i = 0; i < 10; i++){
            anr.addLast(i);
            ba .addLast(i);
        }

        for(int j = 0; j < 10; j++){
            assertEquals(anr.removeLast(), ba.removeLast());
        }
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<Integer>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("AList: addLast(" + randVal + ")");
                System.out.println("Buggy: addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeAnr = correct.size();
                int sizeBa = broken.size();
                System.out.println("AList: size: " + sizeAnr);
                System.out.println("Buggy: size: " + sizeBa);
            } else if (operationNumber == 2) {
                // getLast
                if(correct.size() > 0 && broken.size() > 0){  // 只有大于0， 才有值
                    int aLast = correct.getLast();
                    int bLast = broken.getLast();
                    System.out.println("AList: getLast: " + aLast);
                    System.out.println("Buggy:  getLast: " + bLast);
                }
            }else if(operationNumber == 3) {
                // removeLast
                if(correct.size() > 0 && broken.size() > 0) {
                    int aReLast = correct.removeLast();
                    int bReLast = broken.removeLast();
                    System.out.println("AList: removeLest: " + aReLast);
                    System.out.println("Buggy: removeLest: " + bReLast);
                }

            }
        }
    }

    @Test
    public void moreRandomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        int N = 500;
        for (int i =0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 3);
        }


    }
}
