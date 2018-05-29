package com.ids.idsuserapp.wayfinding;


public class IndiciNavigazione {
    public final int current;
    public final int next;

    public IndiciNavigazione(int current, int next) {
        this.current = current;
        this.next = next;
    }

    public boolean isLast() {
        return current > 0 && next > 0 && current == next;
    }

    @Override
    public String toString() {
        return "IndiciNavigazione{" +
                "current=" + current +
                ", next=" + next +
                '}';
    }
}
