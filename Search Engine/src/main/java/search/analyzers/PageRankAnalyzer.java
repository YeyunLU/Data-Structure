package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;
import java.util.Objects;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed. Must be non-null and must not contain
     *                  nulls.
     * @param decay     Represents the "decay" factor when computing page rank (see spec). Must be a
     *                  number between 0 and 1, inclusive.
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating. Must be a non-negative number.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges. Must be a non-negative number. (A limit of 0 should
     *                  simply return the initial page rank values from 'computePageRank'.)
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        ISet<URI>  pageSet = new ChainedHashSet<>();
        for (Webpage webPage: webpages){
            URI uriExist = webPage.getUri();
            pageSet.add(uriExist);
        }
        for (Webpage webPage: webpages){
            URI uri = webPage.getUri();
            ISet<URI> links = new ChainedHashSet<>();
            // loop over all the neighbors
            for (URI link: webPage.getLinks()) {
                if (Objects.equals(uri, link)) {
                    continue; // avoid loop in graph
                }
                if (pageSet.contains(link)){
                    links.add(link); // use Set to avoid duplicate links
                }
            }
            graph.put(uri, links);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {

        IDictionary<URI, Double> oldRanks = new ChainedHashDictionary<>();
        double iniScore = 1.0/graph.size();

       for (KVPair<URI, ISet<URI>> g:graph){
           oldRanks.put(g.getKey(), iniScore);
       }
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            IDictionary<URI, Double> newRanks = new ChainedHashDictionary<>();
            for (KVPair<URI, ISet<URI>> rank: graph){
                URI page = rank.getKey();
                ISet<URI> links = graph.get(page); // unique links
                if (links.size()==0){
                    // incrementAll
                    double increment = oldRanks.get(page)/graph.size()*decay;
                    for (KVPair<URI, Double> newR:newRanks){
                        URI uriNew = newR.getKey();
                        newRanks.put(uriNew, newRanks.getOrDefault(uriNew, 0.0)+increment);
                    }
                }
                else {
                    double increment = oldRanks.get(page)/links.size()*decay;
                    for (URI uniLink:links){
                        newRanks.put(uniLink, newRanks.getOrDefault(uniLink, 0.0)+increment);
                    }
                }
                double increment = (1.0-decay)/graph.size();
                newRanks.put(page, newRanks.getOrDefault(page, 0.0)+increment);

            }

            // Step 3: the convergence step should go here.
            // Return early if we've converged.

            boolean converge = true;
            for (KVPair<URI, Double> rank:newRanks){
                URI uri=rank.getKey();
                if (Math.abs(oldRanks.get(uri)-newRanks.get(uri))>=epsilon){
                    converge = false;
                }
            }
            if (converge){ // all the pages change slightly, return early
                return oldRanks;
            }
            else {
                oldRanks=newRanks;
            }

        }
        return oldRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}
