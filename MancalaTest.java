package com.ai;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Category(value = UnitTest.class)
public class MancalaTest {

    public boolean assertEqual(List<Integer> a, List<Integer> b) {
        assertEquals(a.size(), b.size());
        for(int i = 0; i < a.size(); i++) {
            assertEquals(a.get(i), b.get(i));
        }
        return true;
    }

    Mancala m;
    static List<Integer> input;
    static int sz;
    static int passCount = 0;
    static int totalCount = 0;

    @Before
    public void fill() {
        m = new Mancala(Mancala.gameType.Basic, Mancala.Strategy.Greedy);
        input = new ArrayList<>();
        sz = m.boardSize;
        Random r = new Random(10);
        for(int i = 0; i < 2 * sz ; i++) {
            input.add(r.nextInt());
        }
        m.setStones(input);
    }

    @Test
    public void testSetStones() {
        totalCount++;

        List<Integer> out1 = m.getStones1();
        List<Integer> out2 = m.getStones2();
        List<Integer> out = new ArrayList<Integer>(out1);
        out.addAll(out2);
        assertEqual(input, out);

        passCount++;
    }

    @Test
    public void testFlipStones() {
        totalCount++;

        m.setStonesFlip(input);

        List<Integer> out1 = m.getStones1();
        List<Integer> out2 = m.getStones2();
        List<Integer> out = new ArrayList<Integer>(out2);
        out.addAll(out1);
        assertEqual(input, out);

        passCount++;
    }

    @Test
    public void testGoalState() {
        totalCount++;

        input.set(sz-1, 1);
        m.setStones(input);

        assertEquals(0, m.isGoalState(input)? 1:0);

        input.set(sz-1, 0);
        input.set( 2 * sz - 1, 0);

        assertEquals(1, m.isGoalState(input)? 1:0);

        passCount++;

    }

    @After
    public void showSummary() {
        System.out.println("Test statistics: " +passCount+ " passed out of " +totalCount);

    }
}
/*
@Test(timeout=1000) annotation specifies that method will be failed if it takes longer than 1000 milliseconds (1 second).

@BeforeClass annotation specifies that method will be invoked only once, before starting all the tests.

@Before annotation specifies that method will be invoked before each test.

 */
