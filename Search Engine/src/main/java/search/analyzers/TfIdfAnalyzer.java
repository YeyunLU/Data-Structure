package search.analyzers;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
//import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

import java.net.URI;
//import java.util.SplittableRandom;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;

    // Feel free to add extra fields and helper methods.

    /**
     * @param webpages  A set of all webpages we have parsed. Must be non-null and
     *                  must not contain nulls.
     */
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        /*
         Implementation note: We have commented these method calls out so your
         search engine doesn't immediately crash when you try running it for the
         first time.
        */

        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> worddic = new ChainedHashDictionary<>();
        IDictionary<String, Double> res = new ChainedHashDictionary<>();
        int pagesnumber = pages.size();
        for (Webpage page:pages){
            IList<String> pagewords = page.getWords();
            IDictionary<String, Double> dup = new ChainedHashDictionary<>();
            for (String word: pagewords){
                if (!dup.containsKey(word)) {
                    dup.put(word, 1.0);
                    worddic.put(word, worddic.getOrDefault(word, 0.0) + 1.0);
                }
            }
        }

        for (KVPair<String, Double> item:worddic){
            String key = item.getKey();
            Double value = item.getValue();
            Double idf = Math.log(pagesnumber/value);
            res.put(key, idf);
        }

        return res;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Double> res = new ChainedHashDictionary<>();
        int number = words.size();
        for (String word:words){
            res.put(word, res.getOrDefault(word, 0.0)+1.0/number);
        }

        return res;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
        IDictionary<URI, IDictionary<String, Double>> res = new ChainedHashDictionary<>();

        for (Webpage page:pages){
            IList<String> pagewords = page.getWords();
            IDictionary<String, Double> worddic = new ChainedHashDictionary<>();
            IDictionary<String, Double> tf = computeTfScores(pagewords);
            for (String word:pagewords){
                if (!worddic.containsKey(word)){
                    worddic.put(word, this.idfScores.get(word)*tf.get(word));
                }
            }
            res.put(page.getUri(), worddic);
        }
        return res;
    }

    private double norm(IDictionary<String, Double> vector){
        double res = 0.0;
        for (KVPair<String, Double> word: vector){
            Double value = word.getValue();
            res += value*value;
        }
        return Math.sqrt(res);
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.

        IDictionary<String, Double> documentVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> qrtf = computeTfScores(query);

        double numerator = 0.0;

        for (String word:query){
            Double idf=0.0;
            if (this.idfScores.containsKey(word)){
                idf = this.idfScores.get(word);
            }
            queryVector.put(word, idf*qrtf.get(word));
        }

        for (String word:query){
            double docWordScore=0.0;
            double queryWordScore;
            if (documentVector.containsKey(word)){
                docWordScore = documentVector.get(word);
            }
            queryWordScore = queryVector.get(word);
            numerator += docWordScore*queryWordScore;
        }

        double denominator = norm(documentVector)* norm(queryVector);

        if (denominator != 0.0){
            return numerator/denominator;
        }else {
            return 0.0;
        }
    }
}
