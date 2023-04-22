package salesmanagement.salesmanagement;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class providing common functionality for working with JavaFX nodes and node trees.
 *
 * @author THANHAN
 * @since 1.4
 */
public class Utils {
    /**
     * Returns a list of all descendant nodes of the specified root node, including the root node itself.
     *
     * @param root the root node of the tree to traverse
     * @return an ArrayList of all nodes in the tree
     * @throws NullPointerException if the root node is null
     * @implNote This method uses a recursive approach to traverse the tree structure, starting from the
     * root node and iterating over its children to collect all descendant nodes. The implementation uses
     * the {@code getChildrenUnmodifiable} method to retrieve the list of children for each parent node.
     * The returned list includes the root node as the first element, followed by its descendant nodes in
     * a depth-first order.
     */
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    /**
     * Adds all the descendant nodes of the specified parent node to the specified ArrayList.
     * This method does not clear the nodes list before adding nodes.
     *
     * @param parent the parent node to traverse
     * @param nodes  the ArrayList to add nodes to
     * @implNote This method is used by the {@code getAllNodes} method to recursively traverse the
     * tree structure and collect all descendant nodes. The implementation uses the {@code getChildrenUnmodifiable}
     * method to retrieve the list of children for each parent node. If a child node is itself a parent node,
     * this method is called recursively to add its descendants to the nodes list.
     */
    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
    }

    /**
     * Locks all child nodes of the given parent node that are instances of the provided locked classes,
     * except for any nodes specified in the list of exception nodes. Locking a node disables its interactivity
     * and may also modify other properties or attributes of the node.
     *
     * @param parent         the parent node whose child nodes should be locked
     * @param lockedClasses  a list of classes to identify the types of nodes to lock
     * @param exceptionNodes a list of nodes to exclude from the locking process
     * @throws NullPointerException if the parent node or any input list is null
     * @implNote This method uses the {@code isInstance} method to check if a node is an instance of any of the
     * locked classes. To unlock a node, it must be included in the list of exception nodes. This method iterates
     * over all the child nodes of the given parent node, so the performance may be affected for large node trees.
     */
    public void lockAllChildren(Parent parent, List<Class<?>> lockedClasses, List<Node> exceptionNodes) {
        if (parent == null || lockedClasses == null || exceptionNodes == null) {
            return;
        }

        if (lockedClasses.isEmpty()) {
            return;
        }

        for (Node node : getAllNodes(parent)) {
            for (Class<?> c : lockedClasses) {
                if (c.isInstance(node)) {
                    node.setDisable(true);
                }
            }
            if (exceptionNodes.contains(node)) {
                node.setDisable(false);
            }
        }
    }
    /**
     Finds a Pair in a list of Pair<int, int> based on the specified key value, and returns its corresponding value.
     @param list The list of Pair<int, int> to search.
     @param key The value of the key to match.
     @return The value of the first Pair found with a key matching the specified value, or -1 if no matching Pair is found.
     */
    public static int findPairWithKey(ArrayList<Pair<Integer, Integer>> list, int key) {
        for (Pair<Integer, Integer> pair : list) {
            if (pair.getKey() == key) {
                return pair.getValue();
            }
        }
        return -1;
    }
}
