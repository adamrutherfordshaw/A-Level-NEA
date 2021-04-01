package com.example.asdaclickcollectemployeesystem;

import android.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class WeightedGraph {

    public static class Graph {

        public LinkedList<Edge>[] adjacencyList; // stores a linked list of edges
        List<Pair<Stack<Integer>, Integer>> allPaths = new ArrayList<>();

        Graph() {
            adjacencyList = new LinkedList[36]; //initialize adjacency lists for all the vertices
            for (int i = 0; i <= 35; i++) {
                adjacencyList[i] = new LinkedList<>();
            }
        }

        public void addEdge(int w, int v1, int v2, boolean both) {
            Edge edge1 = new Edge(w, v1, v2);
            Edge edge2 = new Edge(w, v2, v1);

            if (both) {
                adjacencyList[v1].add(edge1);
                adjacencyList[v2].add(edge2);
            } else {
                adjacencyList[v1].add(edge1);
            }
        }

        public Stack<String> printGraph() {
            List<Edge> list;
            Stack<String> adjList = new Stack<>();
            for (int i = 0; i < 36; i++) { // iterates through each vertex in the graph
                list = adjacencyList[i]; // creates a linked list of each edge connected to the selected vertex
                for (int j = 0; j < list.size(); j++) {
                    adjList.push(" Vertex-" + i + " is connected to " + list.get(j).vName2 + " with weight " + list.get(j).weight + "\n");
                }
            }
            return adjList;
        }

        public int[][] fastPath(int v1) { // finds the fastest path between the specified vertex and all the other vertices
            List<Integer> unvisited = new ArrayList<>();
            int[][] dijkstraFastPath = new int[36][3]; // 36 rows of 3 columns - | vertex to be visited | total distance | previous vertex |
            for (int i = 0; i <= 35; i++) {
                dijkstraFastPath[i][0] = ((v1 + i) % 36); // stores which vertex is being measured
                dijkstraFastPath[i][1] = 2147483647; // initiates path lengths
                unvisited.add(dijkstraFastPath[i][0]); // adds all vertices to unvisited list
            }
            dijkstraFastPath[0][1] = 0; // sets distance of source to source to 0

            while (!unvisited.isEmpty()) {
                Integer closestV = 0; // stores closest vertex
                int smallestW = 2147483647;
                for (int i = 0; i < 36; i++) {
                    if ((dijkstraFastPath[i][1] < smallestW) && (unvisited.contains(dijkstraFastPath[i][0]))) {
                        closestV = dijkstraFastPath[i][0];
                        smallestW = dijkstraFastPath[i][1];
                    }
                }

                unvisited.remove(Integer.valueOf(closestV)); // remove vertex from unvisited

                for (Edge x : adjacencyList[closestV]) {
                    if (unvisited.contains(x.vName2)) {
                        int alt = smallestW + x.weight;

                        if (v1 > x.vName2) {
                            if (dijkstraFastPath[((x.vName2) + 36) - v1][1] > alt) {
                                dijkstraFastPath[((x.vName2) + 36) - v1][1] = alt;
                                dijkstraFastPath[((x.vName2) + 36) - v1][2] = x.vName1;
                            }

                        } else if (v1 < x.vName2) {
                            if (dijkstraFastPath[(x.vName2) - v1][1] > alt) {
                                dijkstraFastPath[(x.vName2) - v1][1] = alt;
                                dijkstraFastPath[(x.vName2) - v1][2] = x.vName1;
                            }
                        }
                    }
                }
            }
            return dijkstraFastPath;
        }


        public Stack<Integer> calculateFastestPathAroundStore(List<Integer> requiredV) {

            int currentCounter = 0; // initiates the counter through the allPaths list

            Stack<Integer> stack = new Stack<>(); // initiates stack and weight values for new path
            stack.push(0);
            Integer weight = 0;
            Pair<Stack<Integer>, Integer> pair = new Pair<>(stack, weight);
            allPaths.add(pair);

            calculateEachPath(requiredV, currentCounter);

            int shortestPathPointer = 0;

            for (int i = 0; i < allPaths.size(); i++) {
                if (allPaths.get(i).second < allPaths.get(shortestPathPointer).second) {
                    shortestPathPointer = i;
                }
            }

            return allPaths.get(shortestPathPointer).first;
        }

        public void calculateEachPath(List<Integer> requiredV, int currentCounter) {

            boolean pathsCalculated = false; // boolean to represent whether all paths in allPaths are requiredV size long

            List<Pair<Integer, Integer>> weightsList = new ArrayList<>(); // stores all connected edges to current v on current path

            while (!pathsCalculated) {

                weightsList.clear();
                Pair<Integer, Integer> tempPair;
                int currentV = allPaths.get(currentCounter).first.peek();

                for (Edge e : adjacencyList[currentV]) {
                    Pair<Integer, Integer> pair = new Pair<>(e.vName2, e.weight);
                    weightsList.add(pair);

                }

                if (allPaths.get(currentCounter).first.size() == requiredV.size()) {

                    weightsList.clear();

                    for (Edge e : adjacencyList[35]) {
                        if (e.vName2 == currentV) {

                            tempPair = new Pair<>(e.vName1, e.weight);
                            weightsList.add(tempPair);
                            break;
                        }
                    }

                } else {

                    List<Pair<Integer, Integer>> pairsToRemove = new ArrayList<>();
                    for (Pair<Integer, Integer> p : weightsList) { // iterates through each connected vertex
                        if (allPaths.get(currentCounter).first.contains(p.first)) { // removes edge from list if vertex already visited
                            pairsToRemove.add(p);
                        } else if (p.first == 35) {
                            pairsToRemove.add(p);
                        }
                    }


                    for (Pair<Integer, Integer> p : pairsToRemove) {
                        weightsList.remove(p);
                    }
                }

                boolean swapped = true; // boolean to represent whether the weightsList is sorted, bubble sort

                while (swapped) { // orders connected edges by weight, bubble sort
                    swapped = false;

                    for (int i = 1; i < weightsList.size(); i++) {
                        if (weightsList.get(i - 1).second > weightsList.get(i).second) {

                            tempPair = weightsList.get(i - 1);
                            weightsList.set(i - 1, weightsList.get(i));
                            weightsList.set(i, tempPair);
                            swapped = true;

                        }
                    }
                }

                if (weightsList.size() == 1) {
                    Stack<Integer> stack = allPaths.get(currentCounter).first;
                    stack.push(weightsList.get(0).first);
                    Integer weight = allPaths.get(currentCounter).second + weightsList.get(0).second;
                    Pair<Stack<Integer>, Integer> pair = new Pair<>(stack, weight);
                    allPaths.set(currentCounter, pair);
                } else {
                    for (int i = 1; i < weightsList.size(); i++) {
                        if (weightsList.get(i).second == weightsList.get(0).second) {
                            Stack<Integer> stack = (Stack<Integer>) allPaths.get(currentCounter).first.clone();
                            Integer weight = allPaths.get(currentCounter).second + weightsList.get(i).second;
                            stack.push(weightsList.get(i).first);
                            Pair<Stack<Integer>, Integer> pair = new Pair<>(stack, weight);
                            allPaths.add(pair);
                        } else {
                            break;
                        }
                    }

                    Stack<Integer> stack = (Stack<Integer>) allPaths.get(currentCounter).first.clone();
                    stack.push(weightsList.get(0).first);
                    Integer weight = allPaths.get(currentCounter).second + weightsList.get(0).second;
                    Pair<Stack<Integer>, Integer> pair = new Pair<>(stack, weight);
                    allPaths.set(currentCounter, pair);
                }

                if (allPaths.get(currentCounter).first.size() == requiredV.size() + 1) {
                    if (allPaths.size() == currentCounter + 1) {
                        pathsCalculated = true;
                    } else {
                        currentCounter++;
                    }
                }
            }
        }
    }

    public static class Edge {
        public int weight, vName1, vName2;

        public Edge(int w, int v1, int v2) {
            weight = w;
            vName1 = v1;
            vName2 = v2;
        }
    }
}