import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class Values{
        double[] attributes;
        String name;

        Values(double[] attributes, String name) {
            this.attributes = attributes;
            this.name = name;
        }
    }

    static List<Values> load(String filename) throws IOException {
        List<Values> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            double[] attributes = new double[parts.length - 1];
            for (int i = 0; i < attributes.length; i++) {
                String value = parts[i].replace(',', '.');
                attributes[i] = Double.parseDouble(value);
            }
            data.add(new Values(attributes, parts[parts.length - 1]));
        }
        reader.close();
        return data;
    }


    static double distance(double[] point1, double[] point2) {
        double distance = 0;
        for (int i = 0; i < point1.length; i++) {
            distance += Math.sqrt(Math.pow(point1[i] - point2[i], 2));
        }
        return distance;
    }

    static String closeNeighbor(List<Values> trainData, double[] testValues, int k) {
        Values[] nearestNeighbors = new Values[k];
        double[] distances = new double[k];

        for (int i = 0; i < k; i++) {
            nearestNeighbors[i] = trainData.get(i);
            distances[i] = distance(nearestNeighbors[i].attributes, testValues);
        }

        for (int i = k; i < trainData.size(); i++) {
            double dist = distance(trainData.get(i).attributes, testValues);
            int maxIndex = 0;
            double maxDist = distances[0];
            for (int j = 1; j < k; j++) {
                if (distances[j] > maxDist) {
                    maxIndex = j;
                    maxDist = distances[j];
                }
            }
            if (dist < maxDist) {
                nearestNeighbors[maxIndex] = trainData.get(i);
                distances[maxIndex] = dist;
            }
        }

        int[] counts = new int[k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                if (nearestNeighbors[i].name.equals(nearestNeighbors[j].name)) {
                    counts[i]++;
                }
            }
        }

        int maxCount = counts[0];
        String mostName = nearestNeighbors[0].name;
        for (int i = 1; i < k; i++) {
            if (counts[i] > maxCount) {
                maxCount = counts[i];
                mostName = nearestNeighbors[i].name;
            }
        }

        return mostName;
    }

    static double accuracy(List<Values> testData, List<Values> trainData, int k) {
        int correct = 0;
        for (Values testValues : testData) {
            String predictedName = closeNeighbor(trainData, testValues.attributes, k);
            if (predictedName.equals(testValues.name)) {
                correct++;
            }
        }
        return (double) correct / testData.size() * 100;
    }

    public static void main(String[] args) throws Exception {
        List<Values> trainData = load("src/iris_training.txt");
        List<Values> testData = load("src/iris_test.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj wartość parametru k: ");
        int k = scanner.nextInt();

        double accuracy = accuracy(testData, trainData, k);
        System.out.println("Dokładność eksperymentu: " + String.format("%.2f", accuracy) + " %");

        try {
            while (true) {
                System.out.print("Wprowadź wektor atrybutów oddzielonych przecinkami lub wpisz 'exit' aby zakończyć: ");
                String input = scanner.next();
                if (input.equals("exit")) {
                    System.out.println("Wyjscie z programu");
                    break;
                }
                String[] attributesStr = input.split(",");
                double[] attributes = new double[attributesStr.length];
                for (int i = 0; i < attributesStr.length; i++) {
                    attributes[i] = Double.parseDouble(attributesStr[i]);
                }

                String predictedClass = closeNeighbor(trainData, attributes, k);
                System.out.println("Przewidziana klasa: " + predictedClass);
            }
            scanner.close();
        }catch (Exception e){
            System.out.println("Błednie wprowadzona dana");
        }
    }
}