/* This file was generated by SableCC (http://www.sablecc.org/). */

package nl.vu.cs.mvermaat.britney.grammar.analysis;

import nl.vu.cs.mvermaat.britney.grammar.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(Node node)
    {
    }

    public void defaultOut(Node node)
    {
    }

    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPTerm().apply(this);
        outStart(node);
    }

    public void inAVariableTerm(AVariableTerm node)
    {
        defaultIn(node);
    }

    public void outAVariableTerm(AVariableTerm node)
    {
        defaultOut(node);
    }

    public void caseAVariableTerm(AVariableTerm node)
    {
        inAVariableTerm(node);
        if(node.getVariable() != null)
        {
            node.getVariable().apply(this);
        }
        outAVariableTerm(node);
    }

    public void inAAbstractionTerm(AAbstractionTerm node)
    {
        defaultIn(node);
    }

    public void outAAbstractionTerm(AAbstractionTerm node)
    {
        defaultOut(node);
    }

    public void caseAAbstractionTerm(AAbstractionTerm node)
    {
        inAAbstractionTerm(node);
        if(node.getTerm() != null)
        {
            node.getTerm().apply(this);
        }
        if(node.getVariable() != null)
        {
            node.getVariable().apply(this);
        }
        outAAbstractionTerm(node);
    }

    public void inAApplicationTerm(AApplicationTerm node)
    {
        defaultIn(node);
    }

    public void outAApplicationTerm(AApplicationTerm node)
    {
        defaultOut(node);
    }

    public void caseAApplicationTerm(AApplicationTerm node)
    {
        inAApplicationTerm(node);
        if(node.getArgument() != null)
        {
            node.getArgument().apply(this);
        }
        if(node.getFunction() != null)
        {
            node.getFunction().apply(this);
        }
        outAApplicationTerm(node);
    }
}