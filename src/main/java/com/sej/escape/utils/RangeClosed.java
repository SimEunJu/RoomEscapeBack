package com.sej.escape.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// [lower, upper]
@Getter
@Setter
@AllArgsConstructor
@Builder
public class RangeClosed<T> {
    private T lower;
    private T upper;

    public RangeClosed(List<T> args) {
        this.lower = args.get(0);
        this.upper = args.get(1);
    }

    public List<T> getByList(){
        List<T> values = new ArrayList<>();
        values.add(this.lower);
        values.add(this.upper);
        return values;
    }
}
