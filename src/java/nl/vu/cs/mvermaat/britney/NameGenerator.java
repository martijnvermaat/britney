/*

Britney - A simple untyped Lambda Calculus interpreter

This file is part of Britney.
See the file "LICENSE" for copyright information and the
terms and conditions for copying, distribution and
modification of Britney.

*/


package nl.vu.cs.mvermaat.britney;


class NameGenerator {


    private int i;


    public NameGenerator() {

        i = 0;

    }


    public String next() {

        String name = "";

        int mod;
        int n = i++;

        do {

            mod = n%26;
            name = (char) (((int)('a')) + mod) + name;

            n = n/26;

        } while (n > 0);

        return name;

    }


}
