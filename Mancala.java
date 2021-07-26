
// Anant Utgikar
// All Rights Reserved.

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;


public class Mancala {

    enum gameType {

        Basic(0, "Basic"),
        Intermediate(1, "Intermediate"),
        Advanced(2, "Advanced");

        public int val;
        public String name;

        gameType(int v, String n) {
            this.val = v;
            this.name = n;
        }
    }

    enum Strategy {

        Greedy(0, "Greedy"),
        Minimax(1, "Minimax"),
        MonteCarlo(2, "MonteCarlo"),
        MCvsMinimax(3, "MCvsMinimax"),
        GreedyvsMinimax(4, "GreedyvsMinimax");

        public int val;
        public String name;

        Strategy(int v, String n) {
            this.val = v;
            this.name = n;
        }
    }


    public List<Integer> getStones2() {
        return stones2;
    }

    public void setStones2(List<Integer> stones2) {
        this.stones2 = stones2;
    }

    public List<Integer> getStones1() {
        return stones1;
    }

    public void setStones1(List<Integer> stones1) {
        this.stones1 = stones1;
    }

    List<Integer> stones1;
    List<Integer> stones2;

    Mancala(gameType g, Strategy s) {

        stones2 = new ArrayList<>(Collections.nCopies(boardSize, 0));
        stones1 = new ArrayList<>(Collections.nCopies(boardSize, 0));

        setBoardState(0);

        this.s = s;
    }

    public boolean gameOver() {
        return (rowEmpty(stones1) || rowEmpty(stones2));
    }

    public int metric(List<Integer> stones) {
        return (stones.get(boardSize - 1) - stones.get(2 * boardSize - 1));
    }

    public void setStones(List<Integer> stones) {
        setStones1(stones.subList(0, boardSize));
        setStones2(stones.subList(boardSize, 2 * boardSize));
    }

    public void setStonesFlip(List<Integer> stones) {
        setStones2(stones.subList(0, boardSize));
        setStones1(stones.subList(boardSize, 2 * boardSize));
    }

    public void setStones(List<Integer> stones1, List<Integer> stones2) {
        setStones1(stones1);
        setStones2(stones2);
    }

    public boolean isGoalState(List<Integer> state) {
        return (rowEmpty(state) || rowEmpty(state.subList(boardSize, state.size())));
    }

    public boolean rowEmpty(List<Integer> stones) {
        boolean isEmpty = true;
        for (int i = 0; i < boardSize - 1; i++) {
            if (stones.get(i) != 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    List<Integer> oneMove(List<Integer> stonesA, List<Integer> stonesB, int pos, boolean debug) {
        List<Integer> merged = new ArrayList<>(stonesA);
        merged.addAll(stonesB);
        return oneMove(merged, pos, debug);
    }

    /**
     * Perform one player move at given state 'combo', starting with cup at 'pos'
     * Common to all modes - minimax, greedy, MC etc
     * @param combo
     * @param pos
     */
    List<Integer> oneMove(List<Integer> combo, int pos, boolean debug) {

        int pickupFrom = pos;
        int mergedSize = 2 * boardSize - 1;
        List<Integer> merged = new ArrayList<>(combo);
        int numAtPos = merged.get(pos);
        int oppGoal = merged.get(mergedSize);
        merged.remove(mergedSize);
        int k = 0;
        if (debug) {
            System.out.println("\t merged = " + merged);
        }

        while (numAtPos != 0) {

            merged.set(pickupFrom, 0);
            k = (pickupFrom) % mergedSize;

            if (debug) {
                System.out.print("\t\t\t starting at = " + k);
            }

            for (; ((numAtPos > 0)); numAtPos--) {
                k = (k + 1) % mergedSize;
                merged.set(k, merged.get(k) + 1);
            }

            if (debug) {
                System.out.print("\t distributed till = " + k);
                System.out.println(" \t merged = " + merged);
            }

            if (k == boardSize - 1) {
                break;
            }
            pickupFrom = k;
            numAtPos = merged.get(k);
            if (numAtPos == 1) {
                break;
            }
            if (debug) {
                System.out.println("\n\t\t   next move to pick up from cup" + k);
            }

        }

        merged.add(oppGoal);

        return merged;
    }

    public static class Node {

        Node parent;
        Node nexthop;
        int parentMove;
        List<Integer> stateStr;
        int level; // 0,even min --- 1,odd max
        int numChildMetrics;
        int numChild;
        int mcWin;
        int mcTotal;

        HashMap<Node, Double> childMetric;

        public List<Integer> getStateStr() {
            return stateStr;
        }

        public void setStateStr(List<Integer> stateStr) {
            this.stateStr = stateStr;
        }

        public int incrementChild() {
            return this.numChild++;
        }

        public int incrementChildMetrics() {
            return this.numChildMetrics++;
        }

        public boolean allChildMetricsSet() {
            return (this.numChild == this.numChildMetrics);
        }

        public HashMap<Node, Double> getChildMetric() {
            return childMetric;
        }

        public boolean addChildMetric(Node n, double m) {
            if (!this.childMetric.containsKey(n)) {
                System.out.println("error child not found" + getStateStr() + " --> " + n.getStateStr());
            }
            this.childMetric.put(n, m);
            return true;
        }

        public boolean addChild(Node n, double m) {
            this.childMetric.put(n, m);
            return true;
        }


        public double computeMetric(boolean debug) {
            int op = level % 2;
            double ret = (op == 0) ? 999 : -999;
            List<Double> val = new ArrayList<>(childMetric.values());
            String opStr = (op == 0) ? "\t Min " : "\t Max ";

            if (debug) {
                System.out.print("\n \t child val: " + val + opStr);
            }
            for (double v : val) {
                if (op == 0) {
                    ret = (ret < v) ? ret : v;
                } else {
                    ret = (ret < v) ? v : ret;
                }
            }
            if (debug) {
                System.out.println("  Node level : " + level + " metric= " + ret);
            }
            return ret;
        }

        public void clearChildrenMetric() {
            this.numChildMetrics = 0;
            this.numChild = 0;
            this.childMetric.clear();
        }

        public int getMcWin() {
            return mcWin;
        }

        public void setMcWin(int mcWin) {
            this.mcWin = mcWin;
        }

        public int getMcTotal() {
            return mcTotal;
        }

        public void setMcTotal(int mcTotal) {
            this.mcTotal = mcTotal;
        }

        Node(Node p, int l, int pm, List<Integer> state) {
            this.parent = p;
            this.level = l;
            this.stateStr = state;
            this.childMetric = new HashMap<>();
            this.numChild = 0;
            this.numChildMetrics = 0;
            this.nexthop = null;
            this.parentMove = pm;
            this.mcTotal = 0;
            this.mcWin = 0;
        }
    }

    class NodeCost implements Comparable<NodeCost> {
        Node n;
        int cost;

        NodeCost(Node x, int c) {
            this.cost = c;
            this.n = x;
        }

        public int compareTo(NodeCost x) {
            return ((this.cost > x.cost) ? 1 : -1);
        }
    }

    public void myPrint(String str, boolean localDebug) {
        if(dbg || localDebug) {
            System.out.println(str);
        }
    }
    
    /**
     * 
     */

    public void minimax(int depth, boolean dbg) {

        List<Integer> initial = new ArrayList<>(stones1);
        initial.addAll(stones2);

        List<Node> currentLevel = new ArrayList<>();
        List<Node> nextLevel = new ArrayList<>();
        PriorityQueue<NodeCost> nodeHeuristic = new PriorityQueue<>();

        List<Integer> nextState;
        String bt = " ";

        // create root node with no node-val + empty-child-node-metrics.
        // put root in level0 list -- BFS
        Node root = new Node(null, 1, 0, initial);
        currentLevel.add(root);
        int count = 0;

        for (Node n : currentLevel) {
//            System.out.println(" \n Node state: " + n.stateStr);
        }

        int levelSize = currentLevel.size();
        Node newRoot = root;

        while (levelSize != 0) {
            nextLevel.clear();
//            System.out.println(" \n Num nodes at level: " + levelSize);

            for (Node x : currentLevel) {
                if (count == MAX_LEVEL_COUNT) {
                    System.out.println(" \n breaking after level: " + count);
                    break;
                }
                count++;

                System.out.println(" \n" + bt + " Node state: " + x.getStateStr() + "  level= " + x.level);

                // for each node in level
                // for each pos-state, create child-nodes with parent set as node, level += 1
                // if child = goal, get metric, propagate to parent as child-metric;
                //     if all metrics done, propagate to its parent and so on till root - if (root) break
                // else put all new level in List
                for (int i = 0; i < boardSize - 1; i++) {

                    if (x.getStateStr().get(i) == 0) {
                        System.out.println(bt + "\t after picking cup " + (i + 1) + " \t\t skipping 0-state-in " + splitBoard(x.getStateStr()));
                        continue;
                    }
                    System.out.print(bt + " \t after picking cup " + (i + 1));
                    x.incrementChild();
                    nextState = oneMove(x.getStateStr(), i, false);
                    Node m = new Node(x, x.level + 1, i, new ArrayList<>(nextState));
                    String nbrState = " \t Child Node state: " + splitBoard(nextState) + "\t  level= ";

                    if (isGoalState(nextState) || ((m.level - newRoot.level) == depth)) {
                        int v = metric(nextState);
                        x.addChild(m, v);
                        x.incrementChildMetrics();
                        System.out.println(nbrState + m.level + "  goal heuristic= " + v);
//                        System.out.println(" \n \t\t\t is goooooooal state: " + v);
                        NodeCost nc = new NodeCost(m, v);
                        nodeHeuristic.add(nc);

                    } else {
                        x.addChild(m, 0);
                        System.out.println(nbrState + m.level);
//                        System.out.println(" \t\t Node nbr state: " + m.getStateStr());
                        nextLevel.add(m);
                    }
                }
                Node nptr = x;
                String nodeParent = "";
                while (nptr.allChildMetricsSet()) {
                    double nv = nptr.computeMetric(false);
                    String opStr = ((nptr.level % 2) == 0) ? "  Min " : "  Max ";

                    System.out.println("\n \t setting metric of " + nodeParent + " " + opStr + " Node state: " + nptr.getStateStr() + " to " + nv);
                    if (nptr == newRoot) {
                        for (Node nptrChild : nptr.getChildMetric().keySet()) {
                            if (nptr.getChildMetric().get(nptrChild) == nv) {
                                nptr.nexthop = nptrChild;
                            }
                        }
                        if (!AlgosCompete) {
                            System.out.println(" \t\t Metric set for iteration newRoot, breaking ");
                        }
                        break;

                    }
                    Node parent = nptr.parent;
                    parent.addChildMetric(nptr, nv);
//                    System.out.println(" \t\t\t with parent state: " + parent.getStateStr() + " l=" + parent.level);
                    parent.incrementChildMetrics();
                    nptr = parent;
                    nodeParent = " parent ";
                }
//                System.out.println(" \n \t\t\t parent state: " + nptr.getStateStr() + " level= " + nptr.level);

            }
            count = 0;

            //   currentLevel = nextLevel;
            //   if(nextLevel.size() == 0) break;
            //   repeat this block

            currentLevel.clear();
//            System.out.println("cl sz= " + currentLevel.size());
            if (newRoot.nexthop == null) {
                currentLevel.addAll(nextLevel);
                bt = "\t >> \t";
                System.out.println("\n\t Continuing to compute further depth level of each of these child states, for Minimax heuristic");
//            System.out.println("cl sz= " + currentLevel.size());
            } else {
                newRoot = newRoot.nexthop;
                newRoot.clearChildrenMetric();
                // clear children accounts for multi level
                if (AlgosCompete) {
                    System.out.println("\t next State = " + newRoot.getStateStr() + " after move= cup " + (newRoot.parentMove + 1));
                    setStones(newRoot.getStateStr());
                    break;
                }
                currentLevel.add(newRoot);
//                System.out.println(" num (newRoot = nexthop) child= " + newRoot.numChild);

            }
            levelSize = currentLevel.size();
            for (Node m : currentLevel) {
                //               System.out.println(" Nodes: " + m.getStateStr() + "  l=" + m.level);
            }
        }
        if (!AlgosCompete) {
            for (Node m : nextLevel) {
                System.out.println(" \n Node level: " + m.getStateStr());
            }
            for (Node m : root.getChildMetric().keySet()) {
                System.out.println(" \n root children state: " + m.getStateStr() + " metric= " + root.getChildMetric().get(m));
            }
            if (root.nexthop != null) {
                System.out.println(" \n Node level 1 next hop: " + root.nexthop.getStateStr() + " move= " + root.nexthop.parentMove);
            }
        }

    }

    public int computeMCmetrics(Node n, boolean debug) {

        int rootMCwin = 0;
        int rootMCAll = 0;
        double maxMetric = 0;
        int bestMove = -1;
        for (Node child :
                n.getChildMetric().keySet()) {
            rootMCAll += child.getMcTotal();
            rootMCwin += child.getMcWin();
        }
        for (Node child :
                n.getChildMetric().keySet()) {

            /* Domain Expert Optional

            Random rand = new Random(System.currentTimeMillis());
            double domainExpertWeight = rand.nextDouble();

            monteCarloDomainExpertFactor * domainExpertWeight / child.getMcTotal();

             */

            double cMetric = (child.getMcWin() / child.getMcTotal()) +
                    Math.sqrt(monteCarloExploreFactor * Math.log(rootMCAll) / child.getMcTotal());

            n.addChildMetric(child, cMetric);
            if (cMetric > maxMetric) {
                maxMetric = cMetric;
                bestMove = child.parentMove;
            }

            if (debug) {
                System.out.println("  MC: \t After parent pick cup=" + (child.parentMove+1) + "  metric= " + cMetric);
                System.out.println("  MC: \t Child Node state: " + splitBoard(child.getStateStr()));
                System.out.println("  MC: \t Child Node (Win/Total) = " + child.getMcWin() + " / " + child.getMcTotal() + '\n');
            }
        }
        if (debug) {
//            System.out.println(" child Metrics= " + n.getChildMetric().values() + '\n');
        }

        return bestMove;
    }

    /**
     * 
     */

    public void monteCarlo() {

        List<Integer> initial = new ArrayList<>(stones1);
        initial.addAll(stones2);
        Random rand = new Random(System.currentTimeMillis());

        List<Node> currentLevel = new ArrayList<>();
        List<Node> nextLevel = new ArrayList<>();
        PriorityQueue<NodeCost> nodeHeuristic = new PriorityQueue<>();

        List<Integer> nextState;
        String bt = " ";

        // create root node with no node-val + empty-child-node-metrics.
        // put root in level0 list -- BFS
        Node root = new Node(null, 1, 0, initial);
        currentLevel.add(root);
        int count = 0;

        for (Node n : currentLevel) {
//            System.out.println(" \n Node state: " + n.stateStr);
        }

        int levelSize = currentLevel.size();
        Node newRoot = null;
        int rootMcWin = 0;
        int rootMcTotal = 0;

        while (levelSize != 0) {
            nextLevel.clear();
//            System.out.println(" \n Num nodes at level: " + levelSize);

            for (Node x : currentLevel) {
                if (count == MAX_LEVEL_COUNT) {
                    System.out.println(" \n breaking after level: " + count);
                    break;
                }
                count++;

                System.out.println(" \n" + bt + " Root Node state: " + x.getStateStr() + "  level= " + x.level + '\n');

                // for each node in level
                // for each pos-state, create child-nodes with parent set as node, level += 1
                // if child = goal, get metric, propagate to parent as child-metric;
                //     if all metrics done, propagate to its parent and so on till root - if (root) break
                // else put all new level in List
                for (int i = 0; i < boardSize - 1; i++) {

                    if (x.getStateStr().get(i) == 0) {
                        System.out.println(bt + "\t after picking cup " + (i + 1) + " \t\t skipping 0-state-in " + splitBoard(x.getStateStr()));
                        continue;
                    }
//                    System.out.println(bt + " \t after picking cup " + (i + 1));
                    x.incrementChild();
                    nextState = oneMove(x.getStateStr(), i, false);
                    Node m = new Node(x, x.level + 1, i, new ArrayList<>(nextState));
                    int mcWin = 1 + rand.nextInt(20);
                    int mcAll = 1 + rand.nextInt(20);
                    m.setMcWin(mcWin);
                    m.setMcTotal(m.getMcWin() + mcAll);
                    x.addChild(m, 0);
                    rootMcWin += mcWin;
                    rootMcTotal += mcAll;
                }

                int move = computeMCmetrics(x, true);
                // Can either run MC at each node or random everywhere downstream
                for (Node ch : x.getChildMetric().keySet()) {
                    if (ch.parentMove == move) {
                        newRoot = ch;
                        setStonesFlip(ch.getStateStr());
                        // Flip if Algo vs Algo
                        System.out.println(" * Chosen move is cup-" + move + " ; next state = " + ch.getStateStr());
                        break;
                    }
                }

            }
            if (AlgosCompete) {
                currentLevel.clear();
            }
            levelSize = currentLevel.size();
        }

        if (AlgosCompete) {
            return;
        }
        /* Simulation playout */

        while (!gameOver()) {
            List<Integer> steps = new ArrayList<>();
            for (int i = 0; i < boardSize - 1; i++) {
                if (stones2.get(i) != 0) {
                    System.out.println("\n Turn of Other Player with board : " + getStones2());
                    steps = oneMove(getStones1(), getStones2(), i, false);
                    System.out.println("   Player picked stones from cup " + (i + 1) + " to get to state: \t" + splitBoard(steps) + "  \t with metric \t " + steps.get(boardSize - 1));
                    break;
                }
            }

            setStonesFlip(steps);
//            System.out.println(" Player1 board next: " + getStones2()+ '\n');
        }

        /* Back Propagation */

        System.out.println("\n***  Game over: *** \n");
        if (stones1.get(boardSize - 1) > stones2.get(boardSize - 1)) {
            System.out.println(" MonteCarlo Player 1 wins ; Computing new metric with increased exploitation \n");
            newRoot.setMcWin(newRoot.getMcWin() + 1);
            root.setMcWin(root.getMcWin() + 1);
        } else {
            System.out.println(" MonteCarlo Player 1 lost ; Computing new metric with decreased exploitation \n");
        }
        newRoot.setMcTotal(newRoot.getMcTotal() + 1);
        root.setMcTotal(root.getMcTotal() + 1);
        int move = computeMCmetrics(root, true);


    }

    public String splitBoard(List<Integer> board) {
        return board.subList(0, boardSize) + " and " + board.subList(boardSize, 2 * boardSize);
    }

    /**
     * 
     */

    public void greedy() {

        List<Integer> steps = null;
        int bestMove = -1;
        int mostStones = -1;

        while (!gameOver()) {
            // for each of 6 cups - states = oneMove(1, pos);
            System.out.println("  Initial state for Greedy Player1: " + getStones2() + " and Player2:" + getStones1() + '\n');
            bestMove = -1;
            mostStones = -1;
            for (int i = 0; i < boardSize - 1; i++) {
                if (stones1.get(i) == 0) continue;
                System.out.print("\t Greedy pick cup " + (i + 1));
                steps = oneMove(stones2, stones1, i, false);
                int j = steps.get(boardSize - 1);
                System.out.println("\t will lead to board state: \t" + splitBoard(steps) + "  \t with metric \t " + steps.get(boardSize - 1));
                if (mostStones < j) {
                    mostStones = j;
                    bestMove = i;
                }
//                System.out.println(" bestmove = " + bestMove + " j= " + mostStones);

            }
            System.out.println("\n\t  bestmove = cup-" + (bestMove + 1) + " metric= " + mostStones);

            steps = oneMove(getStones2(), getStones1(), bestMove, false);

            setStones(steps.subList(boardSize, steps.size()), steps.subList(0, boardSize));

            System.out.print(" \n\t Player2= " + getStones1() + " \t Player1= " + getStones2());
            System.out.println(" \t metric = " + metric(steps));

            if (AlgosCompete) {
                break;
            }

            //player2 simple
            for (int i = 0; i < boardSize - 1; i++) {
                if (stones2.get(i) != 0) {
                    steps = oneMove(getStones2(), getStones1(), i, false);
                    System.out.println("   player2 picked stones from cup " + (i + 1) + " to get to : \t" + steps + "  \t with metric \t " + steps.get(boardSize - 1));
                    break;
                }
            }

            setStones(steps.subList(boardSize, steps.size()), steps.subList(0, boardSize));

            System.out.println(" \t s1= " + getStones1() + " \t s2= " + getStones2() + '\n');

        }
    }


    double monteCarloExploreFactor = 100.0;
    double monteCarloDomainExpertFactor = 100.0;
    int numOpp = 0;
    int numMinimax = 0;
    int boardSize = 7;
    int numStones = 4;
    static boolean AlgosCompete = true;
    int MAX_LEVEL_COUNT = 9999999;
    Strategy s;

    public void setBoardState(int k) {
        Random rand = new Random(System.currentTimeMillis());
        switch (k) {
            case 0:
                for (int i = 0; i < boardSize - 1; i++) {
                    stones2.set(i, numStones);
                    stones1.set(i, numStones);
                }
                break;
            default:
                for (int i = 0; i < boardSize - 1; i++) {
                    stones2.set(i, rand.nextInt(boardSize));
                    stones1.set(i, rand.nextInt(boardSize));
                }
                stones1.set(boardSize - 1, 0);
                stones2.set(boardSize - 1, 0);
                System.out.println(" InitState=" + stones1 + stones2);
                break;

        }

    }

    public void playGreedyMinimaxVaryInitState(int depth) {
        for (int i = 0; i < 100; i++) {
            setBoardState(i);
            while (!gameOver()) {
                System.out.println("\n*** Turn of greedy ");
                greedy();
                if (gameOver()) break;
                System.out.println("\n*** Turn of Minimax ");
                minimax(depth, false);
            }
            numOpp = checkWin();
        }
        System.out.println("\n Winning Statistics:  Greedy= " + numOpp + "  Minimax= " + numMinimax + '\n');
    }

    public void playMCMinimaxVaryInitState(int depth) {
        for (int i = 0; i < 100; i++) {
            setBoardState(i);
            while (!gameOver()) {
                System.out.println("\n*** Turn of Monte Carlo ");
                monteCarlo();
                if (gameOver()) break;
                System.out.println("\n*** Turn of Minimax ");
                minimax(depth, false);
            }
            numOpp = checkWin();
        }
        System.out.println("\n Winning Statistics:  MonteCarlo= " + numOpp + "  Minimax= " + numMinimax + '\n');
    }

    public int checkWin() {
        System.out.println("\n*** GameOver: player1:" + getStones1() + " player2: " + getStones2() + '\n');
        int g = getStones2().get(boardSize - 1);
        int mm = getStones1().get(boardSize - 1);
        if (g > mm) {
            System.out.println("Opponent won by " + (g - mm));
            numOpp++;
        } else {
            System.out.println("Minimax won by " + (mm - g));
            numMinimax++;
        }
        return numOpp;
    }

    private final static String MODE = "mode";
    private final static String DEPTH = "depth";
    private final static String DEBUG = "debug";
    private final static boolean dbg = false;

    public static void main(String[] args) {

        Mancala game = new Mancala(gameType.Basic, Strategy.Greedy);
        Strategy x = Strategy.MCvsMinimax;
        int modeVal = -1;

        Options options = new Options();

        options.addOption(MODE, true, " Specify mode to run the program in, e.g. Usage: -" + MODE + " 0 for Greedy, 1 for Minimax, 2 for MonteCarlo, 3 for GreedyvsMinimax, 4 for MCvsMinimax");
        options.addOption(DEPTH, true, " Specify depth to explore for the algorithm e.g. Usage: -" + DEPTH + " 2");

        CommandLine cmd = null;
        int depth = 2;
        int MAX_DEPTH = 9;
        
        try {
            cmd = new DefaultParser().parse(options, args);
            if (cmd.hasOption(DEPTH)) {
                depth = Integer.valueOf(cmd.getOptionValue(DEPTH));
                System.out.println(" depth = " + depth);
            }

            if (!cmd.hasOption(MODE) || ((depth < 1) || (depth > MAX_DEPTH))) {
                new HelpFormatter().printHelp("Mancala", options);
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Unrecognized option or other error: " + e.getMessage());
            System.exit(1);
        }

        modeVal = Integer.valueOf(cmd.getOptionValue(MODE));
        System.out.println(" x = " + Strategy.values());

        long start = System.nanoTime();
        //initial state
        switch (Strategy.values()[modeVal]) {
            case Greedy:
                game.greedy();
                break;
            case Minimax:
                game.minimax(depth, false);
                break;
            case MonteCarlo:
                game.monteCarlo();
                break;
            case GreedyvsMinimax:
                game.playGreedyMinimaxVaryInitState(depth);
                break;
            case MCvsMinimax:
                game.playMCMinimaxVaryInitState(depth);

            default:
                break;
        }

        long now = System.nanoTime();
        System.out.println(" Time elapsed (ms) = " + (now - start) / 1000);
    }
}
