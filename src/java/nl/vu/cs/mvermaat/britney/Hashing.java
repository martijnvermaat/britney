/*

Britney - A simple untyped Lambda Calculus interpreter

This file is part of Britney.
See the file "LICENSE" for copyright information and the
terms and conditions for copying, distribution and
modification of Britney.

*/


package nl.vu.cs.mvermaat.britney;


import nl.vu.cs.mvermaat.britney.grammar.analysis.*;
import nl.vu.cs.mvermaat.britney.grammar.node.*;
import java.util.*;


public class Hashing extends DepthFirstAdapter {


    private String hash;
    private HashMap map;
    private Stack bound;

    private int nextName;


    public void caseAVariableTerm(AVariableTerm node) {

        inAVariableTerm(node);

        if(node.getVariable() != null) {

            if (map.containsKey(node.getVariable().getText())) {

                hash += map.get(node.getVariable().getText());

            } else {

                hash += "[" + node.getVariable().getText() + "]"; 

            }

            node.getVariable().apply(this);

        }

        outAVariableTerm(node);

    }


    public void caseAAbstractionTerm(AAbstractionTerm node) {

        inAAbstractionTerm(node);

        hash += "a";

        String old = null;
        String name;

        if(node.getVariable() != null) {

            name = node.getVariable().getText();

            hash += nextName;

            if (map.containsKey(name)) {
                old = (String) map.get(name);
            }

            map.put(name, ""+nextName++);

            if(node.getVariable() != null) {
                node.getVariable().apply(this);
            }

            if(node.getTerm() != null) {
                node.getTerm().apply(this);
            }

            map.remove(name);

            if (old != null) {
                map.put(name, old);
            }

        }

        outAAbstractionTerm(node);

    }


    public void caseAApplicationTerm(AApplicationTerm node) {

        inAApplicationTerm(node);

        hash += "p";

        if(node.getFunction() != null) {
            node.getFunction().apply(this);
        }

        if(node.getArgument() != null) {
            node.getArgument().apply(this);
        }

        outAApplicationTerm(node);

    }


    public String getHash() {

        return hash;

    }


    public Hashing() {

        hash = "";
        map = new HashMap();
        bound = new Stack();

        nextName = 0;

    }


}
