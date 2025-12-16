# JAVA-PROJECT-PLAGIARISM-CHECKER-
# 📄 Plagiarism Checker (Cosine Similarity)

## 📌 Project Description
This project is a Java-based Plagiarism Checker that compares two CSV documents and calculates their similarity using the Cosine Similarity algorithm.  
It reads text data from CSV files, processes the words, computes similarity, and generates a detailed plagiarism report in a text file.

---

## 🎯 Features
- Reads input from CSV files
- Uses Cosine Similarity for plagiarism detection
- Handles file and I/O exceptions
- Writes plagiarism report to a text file
- Displays similarity percentage on console
- Uses Java Collections (List, Map, Set)

---

## 🛠️ Technologies Used
- Java
- File Handling (BufferedReader, FileReader, FileWriter)
- Collections Framework
- Exception Handling

---

## 📂 Project Structure
PlagiarismChecker.java  
document1.csv  
document2.csv  
report/  
 └── plagiarism_report.txt  
README.md  

---

## 📥 Input Files
The program expects two CSV files containing text.

### Example: document1.csv
Java is a programming language,  
It is widely used  

### Example: document2.csv
Java is a popular language,  
It is used in many applications  

---

## ▶️ How to Run the Program

### Compile
javac PlagiarismChecker.java

### Run
java PlagiarismChecker

### Sample Input
Enter first CSV file: document1.csv  
Enter second CSV file: document2.csv  
Enter output file name: report/plagiarism_report.txt  

---

## 📤 Output

### Console Output
Reading input files...  
Calculating similarity...  
Writing report to file...  
Plagiarism Check Completed Successfully  
Similarity Score: 72.45%  

### Report File
=== PLAGIARISM DETECTION REPORT ===  
File 1: document1.csv  
File 2: document2.csv  
Cosine Similarity Score: 0.7245  
Similarity Percentage: 72.45%  
Result: MODERATE PLAGIARISM DETECTED  

---

## ⚙️ How It Works
1. Reads CSV files line by line
2. Cleans and tokenizes text
3. Builds word frequency maps
4. Calculates cosine similarity
5. Generates plagiarism report

---

## 📌 Similarity Interpretation
≥ 0.8  → High Plagiarism  
≥ 0.5  → Moderate Plagiarism  
≥ 0.3  → Low Plagiarism  
< 0.3  → No Significant Plagiarism  

---

## ✅ Conclusion
This project demonstrates file handling, text processing, collections usage, and plagiarism detection using cosine similarity.
