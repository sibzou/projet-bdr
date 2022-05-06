package com.lp.bdr.lizard;

public interface QueryPerformer<Q> {
    QueryResult perform(Q query);
}
