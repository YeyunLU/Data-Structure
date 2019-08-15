package datastructures.interfaces;

/**
 * Represents a weighted, undirected edge between two vertices.
 *
 * This class is designed to be generic -- the vertex can be
 * any arbitrary type the client wants.
 */
public interface IEdge<V> {
    /**
     * Returns one of the vertices in the edge.
     */
    public V getVertex1();

    /**
     * Returns the other vertex in the edge.
     */
    public V getVertex2();

    /**
     * Returns the numerical weight of this edge.
     */
    public double getWeight();

    /**
     * Given a vertex that is a part of this edge, returns the other vertex.
     *
     * Note: this method exists mainly because when doing graph traversals, it's
     * relatively common to have a vertex and an edge and want to find the other
     * vertex that's in the edge so we can continue traversing.
     *
     * This method lets us do that relatively cleanly.
     *
     * @throws IllegalArgumentException  if the vertex is null
     * @throws IllegalArgumentException  if the given vertex is not a part of this edge
     */
    public default V getOtherVertex(V vertex) {
        // Note: this is an example of a *default* method.
        // This is a method implementors of the IEdge class may override if they wish.
        // However, this would probably lead to a lot of redundancy between
        // different subclasses of IEdge, so the interface provides this
        // default implementation as a convenience.

        if (vertex == null) {
            throw new IllegalArgumentException();
        }

        V v1 = this.getVertex1();
        V v2 = this.getVertex2();

        if (vertex.equals(v1)) {
            return v2;
        } else if (vertex.equals(v2)) {
            return v1;
        } else {
            throw new IllegalArgumentException("Vertex does not seem to be a part of this edge.");
        }
    }
}
