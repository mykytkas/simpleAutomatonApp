package com.example.demo1;

import java.util.ArrayList;

public class Vertex {
    private boolean start;
    private boolean end;
    private String name;
    private static int staticId = 0;
    private int id;
    private ArrayList<Edge> edges;

    /**
     * create node specifying it as end-node or start-node
     *
     * @param start
     * @param end
     * @param name
     */
    public Vertex(boolean start, boolean end, String name){
        this.start = start;
        this.end = end;
        this.name = name;
        this.id = staticId++;
        edges = new ArrayList<>();
    }

    /**
     * create node that is not end- or start-node
     *
     * @param name
     */
    public Vertex(String name){
        this.start = false;
        this.end = false;
        this.name = name;
        this.id = staticId++;
        edges = new ArrayList<>();
    }
    public boolean isStart() {
        return start;
    }

    public boolean isEnd() {
        return end;
    }
    public void setEnd(){end = true;}
    public void unsetEnd(){end = false;}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void addEdge(int idTo, char[] values){
        Edge edge = new Edge(this.id, idTo, values);
        edges.add(edge);
    }
    public void addEdge(int idTo, String string){
        Edge edge = new Edge(this.id, idTo, string.toCharArray());
        edges.add(edge);
    }
    public void addEdge(int idTo, char value){
        Edge edge = new Edge(this.id, idTo, value);
        edges.add(edge);
    }


    public void deleteEdge(int idTo){// does it work?
        for (Edge edge : edges){
            if(edge.getIdTo() == idTo) {
                edges.remove(edge);
                break;
            }
        }
    }

    /**
     * proofs if vertex has a vertex to another vertex using Object reference
     * @param vertex
     */
    public boolean hasEdgeTo(Vertex vertex){
        for (Edge edge : edges){
            if(edge.getIdTo() == vertex.getId()) return true;
        }
        return false;
    }

    /**
     * proofs if vertex has edge to another vertex using id
     * @param id
     */
    public boolean hasEdgeTo(int id){
        for (Edge edge : edges){
            if(edge.getIdTo() == id) return true;
        }
        return false;
    }

    /**
     * proofs if vertex has edge to another vertex using character
     * @param input
     */
    public boolean accepts(char input){
        for (Edge edge : edges){
            if(edge.isValidValue(input)) return true;
        }
        return false;
    }

    public int getConnectedVertexIdFromInput(char input){
        for (Edge edge : edges){
            if(edge.isValidValue(input)) return edge.getIdTo();
        }
        return -1;
    }

    public ArrayList<Integer> getConnectedVerticesIdsFromInput(char input){
        ArrayList<Integer> connectedVertices = new ArrayList<>();
        for(Edge edge : edges){
            if(edge.isValidValue(input)) connectedVertices.add(edge.getIdTo());
        }
        return connectedVertices;
    }


    @Override
    public String toString() {
        return '\''+ "Name: "+ getName() + " id: " + id + '\'';
    }
}

