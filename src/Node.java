import java.util.ArrayList;
import java.util.List;

public class Node {
    List<Edge> adj;
    int id;

    public Node(int i) {
        adj = new ArrayList<>();
        id = i;
    }
}
