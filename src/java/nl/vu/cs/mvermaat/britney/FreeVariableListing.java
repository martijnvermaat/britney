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


public class FreeVariableListing extends DepthFirstAdapter {


    private Vector free;
    private Vector bound;


    /*
      On each occurance of a variable:
      - Check if it is bound
      - If it is not, add it to free variables
        (if it is not already there)
    */

    public void caseAVariableTerm(AVariableTerm node) {

        if (node.getVariable() != null) {

            if (!bound.contains(node.getVariable().getText())) {

                // This variable is free

                if (!free.contains(node.getVariable().getText())) {
                    free.add(node.getVariable().getText());
                }

            }

        }

    }


    /*
      On each occurance of an abstraction:
      - Push variable on bound variables stack
      - Walk down the abstraction subtree
      - Pop the variable of the bound variables stack
    */

    public void caseAAbstractionTerm(AAbstractionTerm node) {

        if(node.getVariable() != null) {
            bound.add(node.getVariable().getText());
        }

        if(node.getTerm() != null) {
            node.getTerm().apply(this);
        }

        if(node.getVariable() != null) {
            bound.remove(node.getVariable().getText());
        }

    }


    public void caseAApplicationTerm(AApplicationTerm node) {

        if(node.getFunction() != null) {
            node.getFunction().apply(this);
        }

        if(node.getArgument() != null) {
            node.getArgument().apply(this);
        }

    }


    public Vector getVariables() {

        return free;

    }



    /*
      All variables that are not bound in this tree
      are added to the Vector free.
    */

    public FreeVariableListing() {

        this.free = new Vector();
        this.bound = new Vector();

    }


}
