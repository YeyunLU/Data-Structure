package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {

    IDictionary<V, IList<E>> graph;
    IList<E> edge;
    IList<V> vertex;

    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * Note that each edge in 'edges' represents a unique edge. For example, if 'edges'
     * contains an entry for '(A,B)' and for '(B,A)', that means there are two parallel
     * edges between vertex 'A' and vertex 'B'.
     *
     * @throws IllegalArgumentException if any edges have a negative weight
     * @throws IllegalArgumentException if any edges connect to a vertex not present in 'vertices'
     * @throws IllegalArgumentException if 'vertices' or 'edges' are null or contain null
     * @throws IllegalArgumentException if 'vertices' contains duplicates
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        graph = new ChainedHashDictionary<>();
        for (V v: vertices){
            if (v==null||graph.containsKey(v)){
                throw new IllegalArgumentException();
            }
            IList<E> adjacency = new DoubleLinkedList<>();
            graph.put(v, adjacency);
        }
        for (E e: edges) {
            V v1 = e.getVertex1();
            V v2 = e.getVertex2();
            if (e == null || e.getWeight() < 0 || !vertices.contains(v1) || !vertices.contains(v2)) {
                throw new IllegalArgumentException();
            }
            IList<E> l1 = graph.get(v1);
            IList<E> l2 = graph.get(v2);
            l1.add(e);
            l2.add(e);
            graph.put(v1, l1);
            graph.put(v2, l2);
        }
        this.edge=edges;
        this.vertex=vertices;
    }
    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vertex.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return edge.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        int numE = numEdges();
        IList<E> sortedE = Sorter.topKSort(numE, edge);
        IDisjointSet<V> disjointSet = new ArrayDisjointSet<>();
        ISet<E> mMST = new ChainedHashSet<>();
        for (V v: vertex){
            disjointSet.makeSet(v);
        }
        for (E e: sortedE){
            V v1 = e.getVertex1();
            V v2 = e.getVertex2();
            if (disjointSet.findSet(v1)==disjointSet.findSet(v2)){
                continue;
            }
            else {
                disjointSet.union(v1, v2);
                mMST.add(e);
            }
        }
        return mMST;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null or not in the graph
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        IList<E> shortestPath = new DoubleLinkedList<>();
        IDictionary<V, ComparableVertex<V, E>> comparableVertices = new ChainedHashDictionary<>();
        IPriorityQueue<ComparableVertex<V, E>> mPQ = new ArrayHeap<>();
        IDictionary<V, E> path = new ChainedHashDictionary<>();
        ISet<V> processed = new ChainedHashSet<>();
        boolean reachEnd = false;
        if (start==null || end==null || !graph.containsKey(start) || !graph.containsKey(end)){
            throw new IllegalArgumentException();
        }
        if (start==end) {
            return shortestPath;
        }
        // initialize distance for all vertices
        for (V v: vertex){
            ComparableVertex<V, E> cv = new ComparableVertex(v,  null, Double.POSITIVE_INFINITY);
            comparableVertices.put(v, cv);
        }
        ComparableVertex<V, E> source = new ComparableVertex(start,  null, 0.0);
        comparableVertices.put(start, source);
        mPQ.add(source); // add source
        while (!mPQ.isEmpty()){
            ComparableVertex<V, E> v = mPQ.removeMin();
            if (v.vertex.equals(end)){
                reachEnd = true;
                break;
            }
            for (E e :graph.get(v.vertex)){
                V nextV = e.getOtherVertex(v.vertex);
                if (processed.contains(nextV)||nextV.equals(v.vertex))
                {
                    continue;
                }
                Double oldDistance =  comparableVertices.get(nextV).distance; // v.dist
                Double newDistance = v.distance + e.getWeight(); //u.dist + edge
                ComparableVertex<V, E> nextVertex = new ComparableVertex<>(nextV, e, newDistance);
                if (oldDistance.equals(Double.POSITIVE_INFINITY)) {
                    mPQ.add(nextVertex);
                    comparableVertices.put(nextV, nextVertex);
                    path.put(nextVertex.vertex, nextVertex.edge);
                }
                else if (newDistance<oldDistance){
                    ComparableVertex<V, E> cv = comparableVertices.get(nextV);
                    mPQ.replace(cv, nextVertex);
                    comparableVertices.put(cv.vertex, nextVertex);
                    path.put(nextVertex.vertex, nextVertex.edge);
                }

            }
            processed.add(v.vertex);
        }
        if (!reachEnd){
            throw new NoPathExistsException();
        }
        V last = path.get(end).getOtherVertex(end);
        IList<E> reversePath = new DoubleLinkedList<>();
        reversePath.add(path.get(end));
        while (!last.equals(start)) {
            V tmp = path.get(last).getOtherVertex(last);
            reversePath.add(path.get(last));
            last = tmp;
        }
        while (!reversePath.isEmpty()){
            shortestPath.add(reversePath.remove());
        }
        return shortestPath;
    }

    private static class ComparableVertex<V, E>  implements Comparable<ComparableVertex<V, E>> {
        public V vertex;
        public E edge;
        public Double distance;

        public ComparableVertex(V vertex, E edge, Double distance) {
            this.vertex = vertex;
            this.edge = edge;
            this.distance = distance;
        }
        public int compareTo(ComparableVertex<V, E> v) {
            return this.distance.compareTo(v.distance);
        }
    }
}
