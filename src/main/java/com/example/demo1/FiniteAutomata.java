package com.example.demo1;

public interface FiniteAutomata {
    public void addVertex(boolean start, boolean end, String name);
    public Vertex getVertexById(int id);
    public void deleteVertex(int id);
    public boolean isAcceptableString(String string);
}
