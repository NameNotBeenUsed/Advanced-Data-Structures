
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map.Entry;

public class encoder {

	public static void main(String[] args) {
		String filename = "";
		if(args.length > 0) {
			filename = args[0];
		}
		else {
			System.err.println("Invalid argument.");
			System.exit(0);
		}
		HashMap<Integer, Integer> freqTable = buildFreqTable(filename);
		HashMap<Integer, String> codeTable = buildTreeCodeTable(freqTable);
		//private void encode(String filename, HashMap<Integer, String> codeTable)
		encode(filename, codeTable);
	}
	
	private static HashMap<Integer, Integer> buildFreqTable(String filename) {
		HashMap<Integer,Integer> freqTable = new HashMap<Integer,Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    String line;
		    while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
		    	Integer key = Integer.parseInt(line.trim());
		    	Integer oldFreq = freqTable.putIfAbsent(key, 1);
		    	if(oldFreq!=null)
		    		freqTable.replace(key, ++oldFreq);
		    }
		    br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return freqTable;
	}

	private static HashMap<Integer, String> buildTreeCodeTable(HashMap<Integer, Integer> freqTable) {
		
		HuffmanTree tree = new HuffmanTree();
		tree.root = buildTreeUsingFourWayHeap(freqTable);		
		HashMap<Integer,String> codeTable = generateHuffmanCodes(tree.root,"",new HashMap<Integer,String>());
		
		String filename="code_table.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for(Entry<Integer,String> e:codeTable.entrySet())
            	writer.write(e.getKey().toString()+" "+e.getValue()+'\n');
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		return codeTable;
	}
	
	private static HashMap<Integer,String> generateHuffmanCodes(Node root,String huffmanCode,HashMap<Integer,String> codeTable) {
		if(root!=null) {
			huffmanCode = huffmanCode + "0";
			generateHuffmanCodes(root.leftChild, huffmanCode, codeTable);
			//delete the code attached by previous null node
			huffmanCode = huffmanCode.substring(0, huffmanCode.length() - 1);
			//reach a leaf node
			if(root.leftChild == null && root.rightChild == null)
				codeTable.put(root.key, huffmanCode);
			huffmanCode = huffmanCode + "1";
			generateHuffmanCodes(root.rightChild, huffmanCode, codeTable);
		}
		return codeTable;
	}
	
	private static Node buildTreeUsingFourWayHeap(HashMap<Integer, Integer> freqTable) 
	{
		Node root = null;
		
		FourWayHeap minheap = new FourWayHeap();
		
		for(Entry<Integer,Integer> e:freqTable.entrySet())
			minheap.insert(new Node(e.getValue(), e.getKey(), null, null));
		
		while(!minheap.isEmpty())
		{
			Node node1 = minheap.removeMin();
			if(minheap.isEmpty())
			{
				root=node1;
				break;
			}
			Node node2=minheap.removeMin();
			Integer newFreq = node1.frequency + node2.frequency;
			if(minheap.isEmpty())
			{
				//public Node(Integer freq, Integer k, Node lC, Node rC)
				if(node1.frequency > node2.frequency)
					root=new Node(newFreq, -1, node2, node1);
				else
					root=new Node(newFreq, -1, node1, node2);
				break;
			}
			if(node1.frequency > node2.frequency)
				minheap.insert(new Node(newFreq,-1,node2,node1));
			else
				minheap.insert(new Node(newFreq,-1,node1,node2));
		}
		return root;
	}
	
	private static void encode(String filename, HashMap<Integer, String> codeTable) {
		String outputFilename = "encoded.bin";
		try(FileOutputStream outputStream = new FileOutputStream(outputFilename)) {
			try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			    String line;
			    String buffer = "";
			    while((line = br.readLine()) != null && !line.trim().isEmpty()) {
			    	buffer = buffer + codeTable.get(Integer.parseInt(line.trim()));
			    	//System.out.print("Read: " + line.trim() + " ");
			    	if(buffer.length() % 8 == 0) {
			    		for (int i = 0; i < buffer.length(); i += 8) {
				            String byteString = buffer.substring(i, i + 8); //1 byte = 8 bit
				            outputStream.write(Integer.parseInt(byteString, 2));
				        }
			    		buffer = "";
			    	}
			    }
			    if(buffer.length()!=0) {
			    	while (buffer.length() % 8 != 0)
			    		buffer += "0"; // add extra bits until we have full bytes
			    	for (int i = 0; i < buffer.length(); i += 8) {
			            String byteString = buffer.substring(i, i + 8); 
			            outputStream.write(Integer.parseInt(byteString, 2)); 
			        }
			    }
			    br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			outputStream.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}


