
class DTreeNode {
	Integer value;
	DTreeNode leftChild;
	DTreeNode rightChild;
	
	public DTreeNode(Integer val, DTreeNode lC, DTreeNode rC) {
		value = val;
		leftChild = lC;
		rightChild = rC;
	}
}

public class DecodeTree {
	DTreeNode root;
	DecodeTree(){
		root = null;
	}
}
