import math
import random


def read_data(filename):
    data = []
    with open(filename, 'r') as file:
        for line in file:
            line = line.strip().split('\t')
            features = [float(x.replace(',', '.')) for x in line[:-1]]
            label = line[-1]
            data.append((features, label))
    return data


def euc_distance(point1, point2):  # euklides
    distance = 0.0
    for i in range(len(point1)):
        distance += (point1[i] - point2[i]) ** 2
    return math.sqrt(distance)


def init_centroids(data, k):
    if k > len(data):
        raise Exception("k is too high")
    centroids = []
    indices = set()
    while len(indices) < k:
        index = random.randint(0, len(data) - 1)
        if index not in indices:
            indices.add(index)
            centroids.append(data[index][0])
    return centroids


def assign_clusters(data, centroids):  # pszypisac klastry
    clusters = {i: [] for i in range(len(centroids))}
    for point, label in data:
        min_dist = float('inf')
        closest_centroid = None
        for i, centroid in enumerate(centroids):
            dist = euc_distance(point, centroid)
            if dist < min_dist:
                min_dist = dist
                closest_centroid = i
        clusters[closest_centroid].append((point, label))
    return clusters


def calculate_new_centroids(clusters):
    centroids = []
    for cluster_idx, cluster in clusters.items():
        cluster_points = [point for point, label in cluster]
        num_points = len(cluster_points)
        if num_points > 0:
            centroid = [sum(coords) / num_points for coords in zip(*cluster_points)]
            centroids.append(centroid)

    return centroids


def k_means(data, k, max_iterations=100):
    clusters = []
    iterations = 0
    centroids = init_centroids(data, k)
    prev_centroids = None

    for iteration in range(max_iterations):
        clusters = assign_clusters(data, centroids)

        # Calculate new centroids and track changes
        new_centroids = calculate_new_centroids(clusters)
        centroid_changes = [euc_distance(prev, new) for prev, new in zip(centroids, new_centroids)]
        centroids = new_centroids

        # Print sum of squares distances for each iteration
        sum_of_squares = sum(
            euc_distance(point, centroids[i]) ** 2 for i in range(len(centroids)) for point, _ in clusters[i])
        print(f"Iteration {iteration + 1}: Sum of squares distance = {sum_of_squares}")
        iterations += 1
        # Check for convergence
        if prev_centroids is not None and all(change < 0.001 for change in centroid_changes):
            break

        prev_centroids = centroids.copy()

    print(f"{iterations} iterations")
    return clusters, centroids


def calculate_entropy(cluster):
    labels = [label for _, label in cluster]
    total_samples = len(labels)
    unique_labels = set(labels)
    entropy = 0.0
    for label in unique_labels:
        label_count = labels.count(label)
        label_probability = label_count / total_samples
        entropy -= label_probability * math.log2(label_probability)
    return entropy


def main():
    filename = 'iris_training.txt'
    k = int(input("Enter the value of k: "))
    if k <= 0:
        raise Exception("k is too low")
    data = read_data(filename)
    clusters, centroids = k_means(data, k)

    # Display sum of squares distances at each iteration
    for iteration, centroid in enumerate(centroids):
        sum_of_squares = 0.0
        for point, _ in clusters[iteration]:
            sum_of_squares += euc_distance(point, centroid) ** 2
        print(f"Cluster {iteration + 1}: Sum of squares distance = {sum_of_squares}")

    for i, cluster in clusters.items():
        print(f"Cluster {i + 1}:")
        entropy = calculate_entropy(cluster)
        print(f"Entropy: {entropy}")
        last_iteration_clusters = cluster
        iris_count = {}
        for _, label in last_iteration_clusters:
            iris_count[label] = iris_count.get(label, 0) + 1
        print(f"Iris counts: {iris_count}")

    # Display clusters with their entropy
    for i, cluster in clusters.items():
        print(f"Cluster {i + 1}:")
        for point, label in cluster:
            distance_to_centroid = euc_distance(point, centroids[i])
            print(f"{point}  {label}  {distance_to_centroid}")


if __name__ == "__main__":
    main()
