 import java.io.*;
import java.util.*;

interface DocumentReader {
    Document readDocument(String filename) throws IOException;
}

interface SimilarityStrategy {
    double calculateSimilarity(List<String> doc1, List<String> doc2);
}

interface ReportGenerator {
    void generateReport(String filename, String file1, String file2, double similarity) throws IOException;
}

abstract class BaseDocument {
    protected String filename;
    protected List<String> content;

    public BaseDocument(String filename, List<String> content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public List<String> getContent() {
        return content;
    }

    public abstract int getWordCount();
}

class Document extends BaseDocument {
    
    public Document(String filename, List<String> content) {
        super(filename, content);
    }

    @Override
    public int getWordCount() {
        return content.size();
    }
}

class CSVReader implements DocumentReader {
    
    @Override
    public Document readDocument(String filename) throws IOException {
        List<String> documents = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    if (!value.trim().isEmpty()) {
                        documents.add(value.trim().toLowerCase());
                    }
                }
            }
        }

        return new Document(filename, documents);
    }
}

abstract class MapBuilder {
    
    public abstract Map<String, Integer> buildMap(List<String> words);
    
    protected String cleanToken(String token) {
        return token.replaceAll("[^a-zA-Z0-9]", "");
    }
}

class FrequencyMapBuilder extends MapBuilder {
    
    @Override
    public Map<String, Integer> buildMap(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (String word : words) {
            String[] tokens = word.split("\\s+");
            for (String token : tokens) {
                token = cleanToken(token);
                if (!token.isEmpty()) {
                    frequencyMap.put(token, frequencyMap.getOrDefault(token, 0) + 1);
                }
            }
        }

        return frequencyMap;
    }
}

class CosineSimilarityCalculator implements SimilarityStrategy {
    private MapBuilder mapBuilder;

    public CosineSimilarityCalculator() {
        this.mapBuilder = new FrequencyMapBuilder();
    }

    @Override
    public double calculateSimilarity(List<String> doc1, List<String> doc2) {
        Map<String, Integer> freq1 = mapBuilder.buildMap(doc1);
        Map<String, Integer> freq2 = mapBuilder.buildMap(doc2);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(freq1.keySet());
        allWords.addAll(freq2.keySet());

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String word : allWords) {
            int count1 = freq1.getOrDefault(word, 0);
            int count2 = freq2.getOrDefault(word, 0);

            dotProduct += count1 * count2;
            magnitude1 += count1 * count1;
            magnitude2 += count2 * count2;
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }
}

abstract class BaseReportWriter implements ReportGenerator {
    
    protected abstract String getResultMessage(double similarity);
    
    protected void writeHeader(PrintWriter pw, String file1, String file2) {
        pw.println("\n----------------------------------------");
        pw.println("Execution Time: " + new Date());
        pw.println("----------------------------------------");
        pw.println("=== PLAGIARISM DETECTION REPORT ===");
        pw.println("File 1: " + file1);
        pw.println("File 2: " + file2);
    }
}

class ReportWriter extends BaseReportWriter {
    
    @Override
    protected String getResultMessage(double similarity) {
        if (similarity >= 0.8) {
            return "Result: HIGH PLAGIARISM DETECTED";
        } else if (similarity >= 0.5) {
            return "Result: MODERATE PLAGIARISM DETECTED";
        } else if (similarity >= 0.3) {
            return "Result: LOW PLAGIARISM DETECTED";
        } else {
            return "Result: NO SIGNIFICANT PLAGIARISM";
        }
    }

    @Override
    public void generateReport(String filename, String file1, String file2, double similarity) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename, true))) {
            writeHeader(pw, file1, file2);
            pw.printf("Cosine Similarity Score: %.4f\n", similarity);
            pw.printf("Similarity Percentage: %.2f%%\n", similarity * 100);
            pw.println(getResultMessage(similarity));
        }
    }
}

public class PlagiarismChecker {
    private DocumentReader reader;
    private SimilarityStrategy calculator;
    private ReportGenerator writer;

    public PlagiarismChecker() {
        this.reader = new CSVReader();
        this.calculator = new CosineSimilarityCalculator();
        this.writer = new ReportWriter();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first CSV file: ");
        String file1 = sc.nextLine();
        System.out.print("Enter second CSV file: ");
        String file2 = sc.nextLine();
        String outputFile = "report/plagiarism_report.txt";

        PlagiarismChecker checker = new PlagiarismChecker();

        try {
            System.out.println("Reading input files...");
            Document doc1 = (Document) checker.reader.readDocument(file1);
            Document doc2 = (Document) checker.reader.readDocument(file2);

            System.out.println("Calculating similarity...");
            double similarity = checker.calculator.calculateSimilarity(doc1.getContent(), doc2.getContent());

            System.out.println("Writing report to file...");
            checker.writer.generateReport(outputFile, file1, file2, similarity);

            System.out.println("* Plagiarism Check Completed Successfully *");
            System.out.printf("Similarity Score: %.2f%%\n", similarity * 100);
            System.out.println("Report saved to: " + outputFile);

        } catch (FileNotFoundException e) {
            System.err.println("* ERROR: File Not Found *");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Please ensure both CSV files exist in the current directory.");
        } catch (IOException e) {
            System.err.println("\n=== ERROR: Input/Output Exception ===");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Please check file permissions and disk space.");
        } catch (Exception e) {
            System.err.println("\n=== ERROR: Unexpected Exception ===");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}