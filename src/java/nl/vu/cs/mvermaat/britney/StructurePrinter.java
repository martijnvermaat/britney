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
import java.io.*;


public class StructurePrinter extends PrettyPrinter {


    public void inAAbstractionTerm(AAbstractionTerm node) {
        out.print("(\\");
        defaultIn(node);
    }


    public void outAAbstractionTerm(AAbstractionTerm node) {
        defaultOut(node);
        out.print(")");
    }


    public void caseAAbstractionTerm(AAbstractionTerm node) {
        inAAbstractionTerm(node);
        if(node.getVariable() != null) {
            node.getVariable().apply(this);
        }
        out.print(".");
        if(node.getTerm() != null) {
            node.getTerm().apply(this);
        }
        outAAbstractionTerm(node);
    }


    public void inAApplicationTerm(AApplicationTerm node) {
        out.print("(");
        defaultIn(node);
    }


    public void outAApplicationTerm(AApplicationTerm node) {
        defaultOut(node);
        out.print(")");
    }


    public void caseAApplicationTerm(AApplicationTerm node) {
        inAApplicationTerm(node);
        if(node.getFunction() != null) {
            node.getFunction().apply(this);
        }
        out.print(" ");
        if(node.getArgument() != null) {
            node.getArgument().apply(this);
        }
        outAApplicationTerm(node);
    }


    public StructurePrinter() {
        super();
    }


    public StructurePrinter(PrintWriter out) {
        super(out);
    }


}
