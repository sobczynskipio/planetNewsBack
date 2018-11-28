package com.planet.news.news.utils;

import java.util.Stack;

public class PlanetNewsUtils
{
    public static <T> Stack<T> getStackOfArray(T[] array, boolean reverse){
        Stack<T> stack = new Stack<T>();
        for(int i = 0; i < array.length; i++){
            int index = !reverse?i:array.length-(i+1);
            stack.push(array[index]);
        }
        return stack;
    }
}
