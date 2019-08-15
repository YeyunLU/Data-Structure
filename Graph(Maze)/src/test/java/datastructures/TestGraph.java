package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.Graph;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.BaseTest;
import misc.exceptions.NoPathExistsException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestGraph extends BaseTest {
    public static class SimpleEdge implements IEdge<String>, Comparable<SimpleEdge> {
        private String vertex1;
        private String vertex2;
        private double weight;

        public SimpleEdge(String vertex1, String vertex2, double weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }

        @Override
        public String getVertex1() {
            return this.vertex1;
        }

        @Override
        public String getVertex2() {
            return this.vertex2;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = vertex1.hashCode();
            result = 31 * result + vertex2.hashCode();
            temp = Double.doubleToLongBits(weight);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public int compareTo(SimpleEdge other) {
            return Double.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return String.format("IEdge(%s, %s, %s)", this.vertex1, this.vertex2, this.weight);
        }
    }

    /**
     * A convenience method for constructing a new SimpleEdge, since having to
     * type 'new SimpleEdge<>(...)' everywhere would be clunky and annoying.
     */
    public SimpleEdge edge(String v1, String v2, double weight) {
        return new SimpleEdge(v1, v2, weight);
    }

    public boolean checkPathMatches(IList<SimpleEdge> path, String[] expectedPath) {
        if (expectedPath.length - 1 != path.size()) {
            return false;
        }

        String curr = expectedPath[0];
        for (int i = 0; i < path.size(); i++) {
            SimpleEdge edge = path.get(i);

            if (!expectedPath[i].equals(edge.getVertex1()) && !expectedPath[i].equals(edge.getVertex2())) {
                return false;
            }

            String next = edge.getOtherVertex(curr);
            if (!expectedPath[i + 1].equals(next)) {
                return false;
            }

            curr = next;
        }

        return true;
    }

    /**
     * Tries to return a string representing the edges in the input path using intelligent vertex ordering.
     */
    public String pathToString(IList<SimpleEdge> path) {
        String prevVertex = null;
        StringBuilder output = new StringBuilder("[");
        // it's possible to do this iteration with an iterator, but the code is more readable using get
        for (int i = 0; i < path.size(); i++) {
            SimpleEdge curr = path.get(i);
            String v1 = curr.getVertex1();
            String v2 = curr.getVertex2();

            if (v1.equals(prevVertex)) {
                prevVertex = appendEdge(v1, v2, output);
                continue;
            }
            if (v2.equals(prevVertex)) {
                prevVertex = appendEdge(v2, v1, output);
                continue;
            }
            // if prevVertex is not in curr, check next edge to see if it contains any vertex in curr
            if (i < path.size() - 1) {
                SimpleEdge next = path.get(i+1);
                String n1 = next.getVertex1();
                String n2 = next.getVertex2();
                if (v1.equals(n1) || v1.equals(n2)) {
                    prevVertex = appendEdge(v2, v1, output);
                    continue;
                }
                if (v2.equals(n1) || v2.equals(n2)) {
                    prevVertex = appendEdge(v1, v2, output);
                    continue;
                }
            }
            // if next doesn't have anything either, just use alphabetical ordering
            if (v1.compareTo(v2) < 0) {
                prevVertex = appendEdge(v1, v2, output);
            } else {
                prevVertex = appendEdge(v2, v1, output);
            }

        }
        output.append(" ]");
        return output.toString();
    }

    public void assertShortestPathMatches(Graph<String, SimpleEdge> graph, double expectedCost, String[] expectedPath) {
        IList<SimpleEdge> path = graph.findShortestPathBetween(
                expectedPath[0],
                expectedPath[expectedPath.length - 1]);
        double cost = 0;
        for (SimpleEdge edge : path) {
            cost += edge.getWeight();
        }
        if (Math.abs(expectedCost - cost) > 0.0001 || !checkPathMatches(path, expectedPath)) {
            StringBuilder message = new StringBuilder("Expected: [");
            for (int i = 0; i < expectedPath.length - 1; i++) {
                appendEdge(expectedPath[i], expectedPath[i+1], message);
            }
            message.append(" ] (cost: ");
            message.append(expectedCost);
            message.append(") but was: ");
            message.append(pathToString(path));
            message.append(" (cost: ");

            message.append(cost);
            message.append(")");
            fail(message.toString());
        }
    }

    private String appendEdge(String v1, String v2, StringBuilder output) {
        output.append(" (");
        output.append(v1);
        output.append(",");
        output.append(v2);
        output.append(")");
        return v2;
    }

    public Graph<String, SimpleEdge> buildSimpleGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2));
        edges.add(edge("a", "c", 3));

        edges.add(edge("c", "d", 1));

        return new Graph<>(vertices, edges);
    }

    public Graph<String, SimpleEdge> buildNonSimpleGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");
        vertices.add("e");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2));
        edges.add(edge("a", "c", 3));
        edges.add(edge("a", "e", 2));

        edges.add(edge("b", "b", 1)); // self-loop
        edges.add(edge("b", "c", 0));
        edges.add(edge("b", "d", 4));

        edges.add(edge("c", "c", 0)); // self-loop
        edges.add(edge("c", "d", 2)); // parallel edge
        edges.add(edge("c", "d", 1)); // parallel edge
        edges.add(edge("c", "e", 3));

        return new Graph<>(vertices, edges);
    }

    public Graph<String, SimpleEdge> buildDisconnectedGraph() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");
        vertices.add("e");
        vertices.add("f");
        vertices.add("g");

        vertices.add("h");
        vertices.add("i");
        vertices.add("j");
        vertices.add("k");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 1));
        edges.add(edge("a", "c", 4));
        edges.add(edge("a", "d", 7));
        edges.add(edge("a", "g", 9));

        edges.add(edge("b", "c", 2));

        edges.add(edge("c", "d", 3));
        edges.add(edge("c", "f", 0));

        edges.add(edge("d", "d", 3)); // self-loop
        edges.add(edge("d", "g", 8));

        edges.add(edge("e", "f", 1));
        edges.add(edge("e", "g", 2)); // parallel edge
        edges.add(edge("e", "g", 3)); // parallel edge
        edges.add(edge("e", "g", 3)); // parallel edge

        edges.add(edge("h", "i", 3));
        edges.add(edge("h", "j", 1));
        edges.add(edge("h", "k", 1));

        edges.add(edge("i", "j", 4));
        edges.add(edge("i", "k", 2)); // parallel edge
        edges.add(edge("i", "k", 6)); // parallel edge

        edges.add(edge("j", "k", 3));

        return new Graph<>(vertices, edges);
    }

    @Test(timeout=SECOND)
    public void testSizeMethods() {
        Graph<String, SimpleEdge> graph1 = this.buildSimpleGraph();
        assertEquals(4, graph1.numVertices());
        assertEquals(3, graph1.numEdges());

        Graph<String, SimpleEdge> graph2 = this.buildNonSimpleGraph();
        assertEquals(5, graph2.numVertices());
        assertEquals(10, graph2.numEdges());

        Graph<String, SimpleEdge> graph3 = this.buildDisconnectedGraph();
        assertEquals(11, graph3.numVertices());
        assertEquals(20, graph3.numEdges());
    }

    @Test(timeout=SECOND)
    public void testGraphWithNegativeEdgesNotPermitted() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 3));
        edges.add(edge("b", "c", -1));
        edges.add(edge("a", "b", 4));

        try {
            new Graph<>(vertices, edges);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // All ok -- expected result
        }
    }

    @Test(timeout=SECOND)
    public void testGraphWithBadEdgeNotPermitted() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 3));
        edges.add(edge("b", "c", 4));
        edges.add(edge("c", "d", 4)); // 'd' is not a vertex in the above list

        try {
            new Graph<>(vertices, edges);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // All ok -- expected result
        }
    }

    @Test(timeout=SECOND)
    public void testFindingMst() {
        IList<String> vertices = new DoubleLinkedList<>();
        vertices.add("a");
        vertices.add("b");
        vertices.add("c");
        vertices.add("d");

        IList<SimpleEdge> edges = new DoubleLinkedList<>();
        edges.add(edge("a", "b", 2));
        edges.add(edge("a", "c", 3));

        edges.add(edge("c", "d", 1));

        Graph<String, SimpleEdge> graph = new Graph<>(vertices, edges);

        ISet<SimpleEdge> mst = graph.findMinimumSpanningTree();

        assertEquals(graph.numVertices() - 1, mst.size());
        for (SimpleEdge edge : edges) {
            assertTrue(mst.contains(edge));
        }
    }

    @Test(timeout=SECOND)
    public void testFindingShortestPathSimple() {
        Graph<String, SimpleEdge> graph = this.buildSimpleGraph();

        assertShortestPathMatches(graph, 2, new String[] {"a", "b"});
        assertShortestPathMatches(graph, 2, new String[] {"b", "a"});
        assertShortestPathMatches(graph, 4, new String[] {"a", "c", "d"});
        assertShortestPathMatches(graph, 4, new String[] {"d", "c", "a"});
    }

    @Test(timeout=SECOND)
    public void testFindingShortestPathComplex() {
        Graph<String, SimpleEdge> graph = this.buildNonSimpleGraph();

        //assertShortestPathMatches(graph, 3, new String[] {"a", "b", "c", "d"});
        assertShortestPathMatches(graph, 3, new String[] {"d", "c", "b", "a"});
        assertShortestPathMatches(graph, 4, new String[] {"d", "c", "e"});
        assertShortestPathMatches(graph, 4, new String[] {"e", "c", "d"});
        assertShortestPathMatches(graph, 2, new String[] {"a", "e"});
        assertShortestPathMatches(graph, 2, new String[] {"e", "a"});

    }

    @Test(timeout=SECOND)
    public void testFindingShortestPathSameStartAndEnd() {
        Graph<String, SimpleEdge> graph = this.buildNonSimpleGraph();
        IList<SimpleEdge> path = graph.findShortestPathBetween("a", "a");
        assertEquals(0, path.size());
    }

    @Test(timeout=SECOND)
    public void testFindingShortestPathDisconnectedComponents() {
        Graph<String, SimpleEdge> graph = this.buildDisconnectedGraph();

        assertShortestPathMatches(graph, 6, new String[] {"a", "b", "c", "f", "e", "g"});
        assertShortestPathMatches(graph, 2, new String[] {"i", "k"});

        try {
            graph.findShortestPathBetween("a", "i");
            fail("Expected NoPathExistsException");
        } catch (NoPathExistsException ex) {
            // All ok -- expected result
        }

        try {
            graph.findShortestPathBetween("i", "a");
            fail("Expected NoPathExistsException");
        } catch (NoPathExistsException ex) {
            // All ok -- expected result
        }
    }
}
