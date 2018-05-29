package com.ids.idsuserapp.wayfinding;


import com.ids.idsuserapp.db.entity.Beacon;
import com.ids.idsuserapp.db.entity.Tronco;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.Function;


public class Dijkstra {
    public static final String TAG = Dijkstra.class.getName();
    private Beacon origin;
    private Grafo grafo;
    private SearchProblem problem;
    private double normalizationBasis;
    private boolean emergency = false;

    /**
     * Imposta il nodo di partenza
     *
     * @param origin
     * @return
     */
    public Dijkstra inizio(Beacon origin) {
        this.origin = origin;
        return this;
    }

    /**
     * Imposta il grafo
     *
     * @param grafo
     * @return
     */
    public Dijkstra in(Grafo grafo) {
        this.grafo = grafo;
        return this;
    }

    /**
     * Cerca le due soluzioni per raggiungere la destinazione
     *
     * @param destination
     * @return
     */
    public List<Percorso> searchDoublePath(Beacon destination) {
        List<Percorso> solutions = new ArrayList<>();

        // Ricerca della prima soluzione
        solutions.add(ricerca(destination));

        // Ricerca della seconda soluzione
        List<Tronco> star = grafo.getStar(solutions.get(0).getOrigine());
        if(star.size() > 1) {
            Tronco troncoToKill = grafo.getTrunkToBreakPath(solutions.get(0));
            Grafo subGrafo = grafo.createNewGraphWithoutATrunk(troncoToKill);
            if (subGrafo.Connessione()) {
                Percorso secondSolution = ricerca(destination, subGrafo);
                secondSolution.setTroncoEscluso(troncoToKill);
                solutions.add(secondSolution);
            }
        }

        return solutions;
    }

    public Percorso ricerca(Beacon destination) {
        return ricerca(destination, grafo);
    }

    private Percorso ricerca(Beacon destination, Grafo searchGrafo) {
        buildProblemWithGraph(searchGrafo);
        return getPathToReachDestination(destination);
    }

    public List<Percorso> searchNearestExits(List<? extends Checkpoint> exits) {
        List<Percorso> solutions = new ArrayList<>();
        Algorithm.SearchResult result;

        buildProblemWithGraph(grafo);
        for (Checkpoint exit : exits) {
            result = Hipster.createDijkstra(problem).search(exit);
            Percorso percorso = getFirstPath(result);
            percorso.setGoalState((WeightedNode) result.getGoalNode());
            solutions.add(percorso);
        }

        Collections.sort(solutions, new PathComparator());

        return new ArrayList<>(solutions.subList(0, 2));
    }

    private Percorso getPathToReachDestination(Beacon destination) {
        Algorithm.SearchResult result =
                Hipster.createDijkstra(problem).search(destination);
//        System.out.println(problem.getInitialNode());
        return getFirstPath(result);
    }

    private Dijkstra buildProblemWithGraph(Grafo grafoProblem) {
        // Creazione del grafo
        GraphBuilder<Beacon, Tronco> graphBuilder = GraphBuilder.create();
        for (Tronco tronco : grafoProblem) {
            graphBuilder.connect(tronco.getBeginBeacon())
                    .to(tronco.getEndBeacon())
                    .withEdge(tronco);
        }
        HipsterGraph<Beacon, Tronco> hipsterGraph = graphBuilder
                .createUndirectedGraph();

        // Creazione del problema
        problem = GraphSearchProblem
                .startingFrom(origin)
                .in(hipsterGraph)
                .extractCostFromEdges(new Function<Tronco, Double>() {
                    @Override
                    public Double apply(Tronco edge) {
                        return edge.getCosto(normalizationBasis, emergency);
                    }
                })
                .build();
        return this;
    }

    private Percorso getFirstPath(Algorithm.SearchResult result) {
        return new Percorso((List<Beacon>) result.getOptimalPaths().get(0));
    }

    public Dijkstra setNormalizationBasis(double normalizationBasis) {
        this.normalizationBasis = normalizationBasis;
        return this;
    }

    public boolean isEmergency() {
        return emergency;
    }

    public Dijkstra setEmergency(boolean emergency) {
        this.emergency = emergency;
        return this;
    }
}
