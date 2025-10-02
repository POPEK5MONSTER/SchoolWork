package nothing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    private List<double[]> trainingData;
    private List<double[]> testData;
    private List<String> trainingLabels;
    private List<String> testLabels;
    private double[] weights;
    private double learningRate = 0.1;
    private int maxIterations = 1000;

    public Perceptron() {
        trainingData = new ArrayList<>();
        testData = new ArrayList<>();
        trainingLabels = new ArrayList<>();
        testLabels = new ArrayList<>();
    }

    public void loadTrainingData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\s+");
                    double[] attributes = new double[parts.length - 1];
                    for (int i = 0; i < parts.length - 1; i++) {
                        attributes[i] = Double.parseDouble(parts[i]);
                    }
                    trainingData.add(attributes);
                    trainingLabels.add(parts[parts.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTestData(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split("\\s+");
                    double[] attributes = new double[parts.length - 1];
                    for (int i = 0; i < parts.length - 1; i++) {
                        attributes[i] = Double.parseDouble(parts[i]);
                    }
                    testData.add(attributes);
                    testLabels.add(parts[parts.length - 1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void train() {
        weights = new double[trainingData.get(0).length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
        }

        int iteration = 0;
        while (iteration < maxIterations) {
            for (int i = 0; i < trainingData.size(); i++) {
                double[] input = trainingData.get(i);
                int target = trainingLabels.get(i).equals("Iris-setosa") ? 1 : -1;
                int output = calculateOutput(input);
                if (output != target) {
                    updateWeights(input, target, output);
                }
            }
            iteration++;
        }
    }

    private int calculateOutput(double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += weights[i] * input[i];
        }
        return (sum > 0) ? 1 : -1;
    }

    private void updateWeights(double[] input, int target, int output) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * (target - output) * input[i];
        }
    }

    public void test() {
        int correct = 0;
        for (int i = 0; i < testData.size(); i++) {
            double[] input = testData.get(i);
            int target = testLabels.get(i).equals("Iris-setosa") ? 1 : -1;
            int output = calculateOutput(input);
            if (output == target) {
                correct++;
            }
        }
        double accuracy = (double) correct / testData.size() * 100;
        System.out.println("Accuracy: " + accuracy + "%");
    }

    public void classifyInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter attributes of iris (sepal length, sepal width, petal length, petal width):");
        String inputLine = reader.readLine();
        String[] parts = inputLine.split("\\s+");
        double[] attributes = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            attributes[i] = Double.parseDouble(parts[i]);
        }
        int output = calculateOutput(attributes);
        System.out.println("Predicted class: " + (output == 1 ? "Iris-setosa" : "Not Iris-setosa"));
    }

    public static void main(String[] args) throws IOException {
        Perceptron perceptron = new Perceptron();
        perceptron.loadTrainingData("src/iris_training.txt");
        perceptron.loadTestData("src/iris_test.txt");
        perceptron.train();
        perceptron.test();
        perceptron.classifyInput();
    }
}
