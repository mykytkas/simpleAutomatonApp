package com.example.demo1;

import java.util.Arrays;

public class Edge {
    private int idFrom;
    private int idTo;
    private char[] values;

    /**
     * create a vertex that has multiple inputs
     * @param idFrom
     * @param idTo
     * @param values
     */

    public Edge(int idFrom, int idTo, char[] values){
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.values = Arrays.copyOf(values,values.length);
    }

    /**
     * create a vertex that has only a single input,
     * the single input-value is written in array anyway
     * @param idFrom
     * @param idTo
     * @param value
     */
    public Edge(int idFrom, int idTo, char value) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        values = new char[]{value};
    }

    public int getIdFrom(){
        return idFrom;
    }

    public int getIdTo(){
        return idTo;
    }

    public boolean isValidValue(char value){
        for (char character : values){
            if (character == value) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return getIdFrom() == edge.getIdFrom() && getIdTo() == edge.getIdTo() && Arrays.equals(values, edge.values);
    }
}

