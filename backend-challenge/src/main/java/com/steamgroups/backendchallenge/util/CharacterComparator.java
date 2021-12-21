package com.steamgroups.backendchallenge.util;

import com.steamgroups.backendchallenge.model.Character;

import java.util.Comparator;

public class CharacterComparator implements Comparator<Character> {

    @Override
    public int compare(Character o1, Character o2) {
        if (o1.getNumberOfTimesThatAppeared() > o2.getNumberOfTimesThatAppeared())
            return -1;

        else if (o1.getNumberOfTimesThatAppeared() < o2.getNumberOfTimesThatAppeared())
            return 1;

        return 0;
    }
}
