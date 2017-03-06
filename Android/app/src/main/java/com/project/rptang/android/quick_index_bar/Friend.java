package com.project.rptang.android.quick_index_bar;

/**
 * Created by felix on 15/4/25.
 */
public class Friend implements Comparable<Friend> {
    String name;

    public Friend(String name) {
        this.name = name;
    }

    public Friend() {
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Friend another) {
        return this.name.compareTo(another.name);
    }


}
