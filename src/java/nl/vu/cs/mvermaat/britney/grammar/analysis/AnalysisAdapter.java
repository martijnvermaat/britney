/* This file was generated by SableCC (http://www.sablecc.org/). */

package nl.vu.cs.mvermaat.britney.grammar.analysis;

import java.util.*;
import nl.vu.cs.mvermaat.britney.grammar.node.*;

public class AnalysisAdapter implements Analysis
{
    private Hashtable in;
    private Hashtable out;

    public Object getIn(Node node)
    {
        if(in == null)
        {
            return null;
        }

        return in.get(node);
    }

    public void setIn(Node node, Object in)
    {
        if(this.in == null)
        {
            this.in = new Hashtable(1);
        }

        if(in != null)
        {
            this.in.put(node, in);
        }
        else
        {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node)
    {
        if(out == null)
        {
            return null;
        }

        return out.get(node);
    }

    public void setOut(Node node, Object out)
    {
        if(this.out == null)
        {
            this.out = new Hashtable(1);
        }

        if(out != null)
        {
            this.out.put(node, out);
        }
        else
        {
            this.out.remove(node);
        }
    }
    public void caseStart(Start node)
    {
        defaultCase(node);
    }

    public void caseAVariableTerm(AVariableTerm node)
    {
        defaultCase(node);
    }

    public void caseAAbstractionTerm(AAbstractionTerm node)
    {
        defaultCase(node);
    }

    public void caseAApplicationTerm(AApplicationTerm node)
    {
        defaultCase(node);
    }

    public void caseTHead(THead node)
    {
        defaultCase(node);
    }

    public void caseTBody(TBody node)
    {
        defaultCase(node);
    }

    public void caseTVariable(TVariable node)
    {
        defaultCase(node);
    }

    public void caseTLPar(TLPar node)
    {
        defaultCase(node);
    }

    public void caseTRPar(TRPar node)
    {
        defaultCase(node);
    }

    public void caseTBlank(TBlank node)
    {
        defaultCase(node);
    }

    public void caseEOF(EOF node)
    {
        defaultCase(node);
    }

    public void defaultCase(Node node)
    {
    }
}