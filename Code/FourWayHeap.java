
class FourWayHeap {
	
	int heapSize=0;
	Node[] heap;
	
	public FourWayHeap() {
		
		heap=new Node[20];
		heapSize=0;
	}
	
	public boolean isEmpty() {
		
		return heapSize == 0;
	}
	
	public void insert(Node n) {
		
		if (heapSize==0) {
			heap[0] = n;
			heapSize++;
			return;
		}
		
		if (heapSize == heap.length) {
			Node[] newArr = new Node[heapSize*2];
			for(int i=0; i<heapSize; i++)
				newArr[i] = heap[i];
			heap = newArr;
		}
		
		int i = heapSize;
		
		for(; heap[(i-1)/4].frequency > n.frequency; i=(i-1)/4) {
			if (i==0) 
				break;
			heap[i] = heap[(i-1)/4];
		}
		
		heap[i] = n;
		heapSize++;
	}
	

	public Node removeMin() {
		if (heapSize == 0) 
			throw new java.lang.IllegalStateException("Empty Heap"); 
		
		Node min = heap[0];
		Node lastNode = heap[heapSize-1];
		
		int minChild;
		
		int i=0;
		
		for(; (i*4)+1 < heapSize; i=minChild) 
		{
			minChild = (i*4)+1;
			if (minChild > heapSize) 
				break;
			int j=1, currentMinChild = minChild;
			for(; j<4; j++) 
			{
				if (minChild+j == heapSize) 
					break;
				if(heap[currentMinChild].frequency > heap[minChild+j].frequency)
					currentMinChild = minChild+j;
			}
			
			minChild = currentMinChild;
			if (lastNode.frequency > heap[minChild].frequency) 
				heap[i] = heap[minChild];
			else
				break;
		}
		
		heap[i] = lastNode;
		heapSize--;
		return min;
	}

	private int parent(int index){
		return (index - 1) / 4;
	}

	private void swap(int i, int j){
		Node temp = heap[i];
		heap[i] = heap[j];
		heap[j] = temp;
	}

	private int minChild(int index){
		int min = (index * 4) + 1;
		if(min > heapSize) return -1;
		int j=1, currentMinChild = min;
		for(; j<4; j++) {
			if (min + j == heapSize) 
				break;
			if(heap[currentMinChild].frequency > heap[min + j].frequency)
				currentMinChild = min + j;
		}
		return min;
	}
}