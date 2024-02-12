package com.example.demo1;

import java.util.ArrayList;

public class NonDeterministicFiniteAutomata implements FiniteAutomata{

    ArrayList<Vertex> vertices;

    public NonDeterministicFiniteAutomata(){
        vertices = new ArrayList<>();
    }
    /**
     * creates a vertex and adds it to automata
     *
     * @param start
     * @param end
     * @param name
     */
    @Override
    public void addVertex(boolean start, boolean end, String name) {
        Vertex vertex = new Vertex(start, end, name);
        vertices.add(vertex);
    }

    /**
     * @param id
     */
    @Override
    public Vertex getVertexById(int id) {
        for (Vertex vertex : vertices){
            if(vertex.getId() == id) return vertex;
        }
        System.err.println("no such vertex found, returning false vertex with empty String name and id = -1");
        return new Vertex("");
    }

    /**
     * @param id
     */
    @Override
    public void deleteVertex(int id) {
        for (Vertex vertex : vertices){
            if (vertex.getId() != id) continue;

            vertices.remove(vertex);
            break;
        }
    }
    private int getStartingVertexId(){
        int startingNodeId = 0;
        for (Vertex vertex : vertices) {
            if (vertex.isStart()) {
                startingNodeId = vertex.getId();
                break;
            }
        }
        return startingNodeId;
    }
    private int[] getEndingVertexIds(){
        int length = 0;
        for (Vertex vertex : vertices){
            if(vertex.isEnd()) length++;
        }
        int[] endingVertexIds = new int[length];
        for (int i = 0, j = 0; i < vertices.size() && j < endingVertexIds.length; i++) {
            if(vertices.get(i).isEnd()) {
                endingVertexIds[j] = vertices.get(i).getId();
                j++;
            }
        }
        return endingVertexIds;
    }
    /**
     * @param string
     * @return
     */
    @Override
    public boolean isAcceptableString(String string) {

        int startingVertexId = getStartingVertexId();
        ArrayList<Integer> currentVerticesIds = new ArrayList<>();
        currentVerticesIds.add(startingVertexId);
        char[] inputs = string.toCharArray();

        for (char input : inputs){
            ArrayList<Integer> nextVertices = new ArrayList<>();
            for (Integer id : currentVerticesIds){
                //if there is an edge from one of the current vertices to another vertex
                //remove current and add that another vertex
                if (getVertexById(id).accepts(input)){
                    nextVertices.addAll(getVertexById(id).getConnectedVerticesIdsFromInput(input));
                }
                else {//maybe not needed here, is called everytime a vertex
                    System.err.println("not acceptable input character, ends at vertex " + getVertexById(id)
                            + " and at input character " + input);
                }
            }
            currentVerticesIds = nextVertices;

        }

        int[] endingVertices = getEndingVertexIds();
        for (int endingId : endingVertices){
            for (Integer currentId : currentVerticesIds){
                if (endingId == (int)currentId) return true;
            }
        }
        System.err.println("not acceptable, possible ending vertices: " + currentVerticesIds);
        return false;
    }
}
























