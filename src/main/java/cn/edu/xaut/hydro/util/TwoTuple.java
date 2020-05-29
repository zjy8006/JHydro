package cn.edu.xaut.hydro.util;

/**
 * 两变量元组返回类型
 */
public class TwoTuple<A,B> {
    public final A first;
    public final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }


    @Override
    public String toString() {
        return "("+first+", "+second+")";
    }
}
