package com.project.rptang.android.quick_index_bar;

import java.util.ArrayList;

/**
 * Created by felix on 15/4/25.
 */
public class FriendsManager {

    private static ArrayList<Friend> mFriends=new ArrayList<Friend>();
    static{
        mFriends.add(new Friend("Felix"));
        mFriends.add(new Friend("Alex"));
        mFriends.add(new Friend("James"));
        mFriends.add(new Friend("Tom"));
        mFriends.add(new Friend("Jon"));
        mFriends.add(new Friend("Robert"));
        mFriends.add(new Friend("Sedgwick"));
        mFriends.add(new Friend("Yoo"));
        mFriends.add(new Friend("Fun"));
        mFriends.add(new Friend("Bob"));
        mFriends.add(new Friend("Jolt"));
        mFriends.add(new Friend("Bom"));
        mFriends.add(new Friend("Afrank"));
        mFriends.add(new Friend("Frank"));
        mFriends.add(new Friend("Zzz"));
        mFriends.add(new Friend("X"));
        mFriends.add(new Friend("Lang"));
        mFriends.add(new Friend("Li"));
        mFriends.add(new Friend("Mother"));
        mFriends.add(new Friend("Father"));
        mFriends.add(new Friend("Wife"));
        mFriends.add(new Friend("Hero"));
        mFriends.add(new Friend("张三"));
        mFriends.add(new Friend("李四"));
        mFriends.add(new Friend("网迷"));
        mFriends.add(new Friend("张玲"));
        mFriends.add(new Friend("天天"));

    }

    public static ArrayList<Friend> getFriends() {
        return mFriends;
    }


}
