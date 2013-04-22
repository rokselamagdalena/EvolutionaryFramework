package CSP.tests.test1;

import CSP.CSP.*;
import CSP.tests.test1.components.ADF0Components;
import CSP.tests.test1.components.ADF0Input;
import CSP.tests.test1.components.ArithmeticComponents;
import CSP.tests.test1.components.Components;
import geneExpressionProgramming.GEPPopulationDelegate;
import geneticAlgorithm.EvaluationInterface;
import geneticAlgorithm.GADelegate;
import geneticAlgorithm.Genome;
import geneticAlgorithm.selection.TournamentSelection;
import grammar.adf.ADF;
import grammar.adf.ADFDelegate;
import java.util.HashMap;
import test.Test;

/**
 *
 * @author Jesús Irais González Romero
 */

public class CSPTestGEPCoevolutive extends Test implements EvaluationInterface{
    static CSP cspForEvaluation = new XMLLoader().createCSP("instanceCSP01.xml");
    
    //static CSP cspForEvaluation = new CSP(8, 5, .4, .4, 1);
    //static CSP cspX = CSP.loadFromFile("instanciaCSP01.txt");
    //CSP cspForEvaluation = CSP.loadFromFile("instanciaCSP01.xml");
    @Override
    public String getName() {
        return "GEP, Coevolutive";
    }
    

    @Override
    public GADelegate configure() {
        GEPPopulationDelegate adf0PopulationDelegate = new GEPPopulationDelegate(
                new ADF[0], 100, false, new TournamentSelection(99, 2),
                new Class<?>[]{ADF0Components.class, ArithmeticComponents.class},
                Variable.class, ADF0Input.class, new HashMap(),
                10, new CSPLinkerFunction(), .05,
                .1, .1, .1, .1, .1, .1);
        adf0PopulationDelegate.initiate();
        ADFDelegate adf0Delegate = new ADFDelegate(ADFDelegate.ADFType.COEVOLUTIVE, adf0PopulationDelegate);
        ADF adf0 = new ADF(0, adf0Delegate);
        
        GEPPopulationDelegate mainPopulationDelegate = new GEPPopulationDelegate(
                new ADF[]{adf0}, 100, false, new TournamentSelection(99,2), 
                new Class<?>[]{Components.class, ArithmeticComponents.class},
            Variable.class, CSP.class, new HashMap(), 20,  new CSPLinkerFunction(), 
                .05, .1, .1, .1, .1, .1, .1);
        mainPopulationDelegate.initiate();
        return new GADelegate(mainPopulationDelegate, 100, new ADF[]{adf0}, this);
    }
    
    @Override
    public double evaluateGenome(Genome genome) {
        CSP csp = new CSP(cspForEvaluation);
        GACSPSolver solver = new GACSPSolver(csp, genome);
        Variable[] variables = solver.solve(ValueOrderingHeuristics.MNC, ConstraintOrderingHeuristics.NONE);
        
        long checks = solver.getConstraintChecks();
        return checks;
        
    }
    
}