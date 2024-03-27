import java.util.PriorityQueue;
import java.util.HashMap;

class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char data;
    HuffmanNode left, right;

    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public HuffmanNode(char data, int frequency, HuffmanNode left, HuffmanNode right) {
        this.data = data;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.frequency - o.frequency;
    }
}

public class Main {

    private static final HashMap<Character, String> huffmanCodes = new HashMap<>();
    private static HuffmanNode huffmanTreeRoot;

    public static void main(String[] args) {
        String text = "Hello, World!";
        System.out.println("Original text: " + text);

        String encodedText = encode(text);
        System.out.println("Encoded text: " + encodedText);

        String decodedText = decode(encodedText);
        System.out.println("Decoded text: " + decodedText);
    }

    public static String encode(String text) {
        HashMap<Character, Integer> frequencyMap = buildFrequencyMap(text);
        huffmanTreeRoot = buildHuffmanTree(frequencyMap);
        buildHuffmanCodes(huffmanTreeRoot, "");
        return generateEncodedText(text);
    }

    private static HashMap<Character, Integer> buildFrequencyMap(String text) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private static HuffmanNode buildHuffmanTree(HashMap<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (char c : frequencyMap.keySet()) {
            priorityQueue.offer(new HuffmanNode(c, frequencyMap.get(c)));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            assert right != null;
            HuffmanNode parent = new HuffmanNode('\0', left.frequency + right.frequency, left, right);
            priorityQueue.offer(parent);
        }

        return priorityQueue.poll();
    }

    private static void buildHuffmanCodes(HuffmanNode root, String code) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.data, code);
        }
        buildHuffmanCodes(root.left, code + "0");
        buildHuffmanCodes(root.right, code + "1");
    }

    private static String generateEncodedText(String text) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(c));
        }
        return encodedText.toString();
    }

    public static String decode(String encodedText) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode current = huffmanTreeRoot;

        for (char bit : encodedText.toCharArray()) {
            if (bit == '0') {
                current = current.left;
            } else if (bit == '1') {
                current = current.right;
            }

            assert current != null;
            if (current.left == null && current.right == null) {
                decodedText.append(current.data);
                current = huffmanTreeRoot;
            }
        }
        return decodedText.toString();
    }
}
