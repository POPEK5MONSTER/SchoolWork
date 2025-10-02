import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PerceptronIris {

    private static final double learningRate = 0.1;
    private static final int maxIterations = 5;

    static class Values {
        double[] attributes;
        String name;

        Values(double[] attributes, String name) {
            this.attributes = attributes;
            this.name = name;
        }
    }

    static List<Values> loadData(String filename) throws IOException {
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

    static double Sum(double[] weights, double[] attributes) {
        double result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += weights[i] * attributes[i];
        }
        return result;
    }

    static int classify(double[] weights, double[] attributes) {
        double dotProduct = Sum(weights, attributes);
        return dotProduct >= 0 ? 1 : 0;
    }

    static double[] train(List<Values> trainData, double learningRate, int maxIterations) {
        double[] weights = new double[trainData.get(0).attributes.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 0;
        }
        for (int iter = 0; iter < maxIterations; iter++) {
            for (Values instance : trainData) {
                double predicted = classify(weights, instance.attributes);
                double actual = instance.name.equals("Iris-setosa") ? 1 : 0;
                double[] attributes = instance.attributes;
                for (int i = 0; i < weights.length; i++) {
                    weights[i] += learningRate * (actual - predicted) * attributes[i];
                }
            }
        }
        return weights;
    }

    static double accuracy(List<Values> testData, double[] weights) {
        int correct = 0;
        for (Values instance : testData) {
            double predicted = classify(weights, instance.attributes);
            double actual = instance.name.equals("Iris-setosa") ? 1 : 0;
            if (predicted == actual) {
                correct++;
            }
        }
        return (double) correct / testData.size() * 100;
    }

    public static void main(String[] args) throws IOException {
        List<Values> trainData = loadData("src/iris_training.txt");
        List<Values> testData = loadData("src/iris_test.txt");

        Scanner scanner = new Scanner(System.in);

        double[] weights = train(trainData, learningRate, maxIterations);
        double accuracy = accuracy(testData, weights);

        System.out.println("Dokładność eksperymentu: " + String.format("%.2f", accuracy) + " %");

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

            int predictedClass = classify(weights, attributes);
            String className = predictedClass == 1 ? "Iris-setosa" : "Not-Iris-setosa";
            System.out.println("Przewidziana klasa: " + className);
        }
        scanner.close();
    }
}
