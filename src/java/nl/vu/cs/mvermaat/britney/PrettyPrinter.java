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


public class PrettyPrinter extends DepthFirstAdapter {


    protected PrintWriter out;


    public void caseTVariable(TVariable node) {
        out.print(node.getText());
    }


    public void caseAAbstractionTerm(AAbstractionTerm node) {

        inAAbstractionTerm(node);

        if (node.parent() != null && node.parent() instanceof AAbstractionTerm) {
            out.print(" ");
        } else {
            out.print("\\");
        }

        if(node.getVariable() != null) {
            node.getVariable().apply(this);
        }

        if(node.getTerm() != null) {
            if (!(node.getTerm() instanceof AAbstractionTerm)) {
                out.print(".");
            }
            node.getTerm().apply(this);
        }

        outAAbstractionTerm(node);

    }


    public void caseAApplicationTerm(AApplicationTerm node) {

        inAApplicationTerm(node);

        if (node.getFunction() != null) {
            if (node.getFunction() instanceof AAbstractionTerm) {
                out.print("(");
            }
            node.getFunction().apply(this);
            if (node.getFunction() instanceof AAbstractionTerm) {
                out.print(")");
            }
        }

        out.print(" ");

        if(node.getArgument() != null) {
            if (node.getArgument() instanceof AApplicationTerm) {
                out.print("(");
            }
            node.getArgument().apply(this);
            if (node.getArgument() instanceof AApplicationTerm) {
                out.print(")");
            }
        }

        outAApplicationTerm(node);

    }


    public PrettyPrinter(PrintWriter out) {

        this.out = out;

    }


    public PrettyPrinter() {

        this(new PrintWriter(System.out));

    }


}
