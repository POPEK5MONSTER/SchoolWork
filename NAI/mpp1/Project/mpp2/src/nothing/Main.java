package nothing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final double LEARNING_RATE = 0.01;
    private static final int MAX_ITERATIONS = 10000;

    private static final String SETOSA_CLASS = "Iris-setosa";

    private List<Double> weights;
    private double threshold;

    public Main(int numAttributes) {
        weights = new ArrayList<>();
        initializeWeights(numAttributes);
        threshold = 0;
    }

    private void initializeWeights(int numAttributes) {
        for (int i = 0; i < numAttributes; i++) {
            weights.add(0.0);
        }
    }

    public void train(List<List<Double>> trainingData) {
        int numIterations = 0;
        boolean converged = false;

        while (!converged && numIterations < MAX_ITERATIONS) {
            converged = true;
            for (List<Double> instance : trainingData) {
                List<Double> attributes = instance.subList(0, instance.size() - 1);
                double predictedClass = classify(attributes);

                predictedClass = predictedClass == 1 ? 0 : 1;

                double error = instance.get(instance.size() - 1) - predictedClass;
                if (error != 0) {
                    updateWeights(attributes, error);
                    threshold += LEARNING_RATE * error;
                    converged = false;
                }
            }
            numIterations++;
        }
    }

    private double classify(List<Double> attributes) {
        double sum = 0;
        for (int i = 0; i < attributes.size(); i++) {
            sum += attributes.get(i) * weights.get(i);
        }
        sum += threshold;
        return sum > 0 ? 1 : 0;
    }

    private void updateWeights(List<Double> attributes, double error) {
        for (int i = 0; i < weights.size(); i++) {
            double newWeight = weights.get(i) + LEARNING_RATE * error * attributes.get(i);
            weights.set(i, newWeight);
        }
    }

    public double test(List<List<Double>> testData) {
        int correctlyClassified = 0;
        for (List<Double> instance : testData) {
            List<Double> attributes = instance.subList(0, instance.size() - 1);
            double predictedClass = classify(attributes);

            if (predictedClass == 0) {
                correctlyClassified++;
            }
        }
        return (double) correctlyClassified / testData.size() * 100;
    }


    private static List<List<Double>> loadData(String filename) throws IOException {
        List<List<Double>> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Usunięcie białych znaków z początku i końca linii
            if (!line.isEmpty()) { // Sprawdzenie czy linia nie jest pusta
                String[] values = line.split("\\s+");
                List<Double> instance = new ArrayList<>();
                for (int i = 0; i < values.length - 1; i++) {
                    instance.add(Double.parseDouble(values[i].replace(",", ".")));
                }
                if (values.length > 0) {
                    String className = values[values.length - 1];
                    instance.add(className.equals("Iris-setosa") ? 1.0 : 0.0);
                }
                data.add(instance);
            }
        }
        reader.close();
        return data;
    }

    public static void main(String[] args) {
        try {
            List<List<Double>> trainingData = loadData("src/iris_training.txt");
            List<List<Double>> testData = loadData("src/iris_test.txt");

            int numAttributes = trainingData.get(0).size() - 1; // Number of attributes excluding the class

            Main perceptron = new Main(numAttributes);
            perceptron.train(trainingData);

            double accuracy = perceptron.test(testData);
            System.out.println("Accuracy: " + accuracy + "%");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Enter attribute vector (separated by spaces, type 'exit' to quit):");
                String input = reader.readLine();
                if (input.equals("exit")) {
                    break;
                }

                String[] values = input.split(",");
                List<Double> attributes = new ArrayList<>();
                for (String value : values) {
                    attributes.add(Double.parseDouble(value));
                }

                double predictedClass = perceptron.classify(attributes);

                if (predictedClass == 1) {
                    System.out.println("Predicted class: " + SETOSA_CLASS);
                } else {
                    System.out.println("Predicted class: Not " + SETOSA_CLASS);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
