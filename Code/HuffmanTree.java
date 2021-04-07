
class Node {
	Integer frequency;
	Integer key;
	Node leftChild;
	Node rightChild;
	
	public Node(Integer freq, Integer k, Node lC, Node rC) {
		frequency = freq;
		key = k;
		leftChild = lC;
		rightChild = rC;
	}
}

public class HuffmanTree {
	Node root;
	HuffmanTree() {
		//initialize an empty tree
		root = null;
	}
}
