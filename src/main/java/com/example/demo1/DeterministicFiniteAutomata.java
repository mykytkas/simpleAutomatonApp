package com.example.demo1;

import java.util.ArrayList;

public class DeterministicFiniteAutomata implements FiniteAutomata{
    private ArrayList<Vertex> vertices;

    public DeterministicFiniteAutomata(){
        vertices = new ArrayList<>();
    }

    /**
     * adds a vertex as object
     * @param vertex
     */
    public void addVertex(Vertex vertex){
        vertices.add(vertex);
    }

    /**
     * creates a vertex and adds it to automata
     * @param start
     * @param end
     * @param name
     */
    @Override
    public void addVertex(boolean start, boolean end, String name){
        Vertex vertex = new Vertex(start, end, name);
        vertices.add(vertex);
    }
    @Override
    public Vertex getVertexById(int id){
        for (Vertex vertex : vertices){
            if(vertex.getId() == id) return vertex;
        }
        System.err.println("no such vertex found, returning false vertex with empty String name and id = -1");
        return new Vertex("");
    }
    @Override
    public void deleteVertex(int id){
        for (Vertex vertex : vertices){
            if (vertex.getId() != id) continue;

            vertices.remove(vertex);
            break;
        }
    }


    private int getStartingNodeId(){
        int startingNodeId = 0;
        for (Vertex vertex : vertices) {
            if (vertex.isStart()) {
                startingNodeId = vertex.getId();
                break;
            }
        }
        return startingNodeId;
    }
    private int[] getEndingNodeIds(){
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

    @Override
    public boolean isAcceptableString(String string){
        int startingVertexId = getStartingNodeId();
        int currentVertexId = startingVertexId;
        char[] inputs = string.toCharArray();
        for (char input : inputs){
            //if there is an edge from current to another vertex
            //make this another vertex current and go on
            if (getVertexById(currentVertexId).accepts(input)){
                currentVertexId = getVertexById(currentVertexId).getConnectedVertexIdFromInput(input);
            } else{
                System.err.println("not acceptable input character, ends at vertex " + getVertexById(currentVertexId)
                        + " and at input character " + input);
                return false;
            }
        }
        for (int endingId : getEndingNodeIds()){
            if (currentVertexId == endingId) return true;
        }
        System.err.println("not acceptable, ends at vertex " + getVertexById(currentVertexId));
        return false;
    }
}

