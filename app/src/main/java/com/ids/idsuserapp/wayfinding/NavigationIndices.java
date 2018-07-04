package com.ids.idsuserapp.wayfinding;


public class NavigationIndices {
    public final int current;
    public final int next;

    public NavigationIndices(int current, int next) {
        this.current = current;
        this.next = next;
    }

    public boolean isLast() {
        return current > 0 && next > 0 && current == next;
    }

    @Override
    public String toString() {
        return "NavigationIndices{" +
                "current=" + current +
                ", next=" + next +
                '}';
    }
}
