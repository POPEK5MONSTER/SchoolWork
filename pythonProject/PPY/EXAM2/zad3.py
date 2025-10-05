import numpy as np


def print_matrix_elements_by_index_sum(matrix):
    N = matrix.shape[0]
    elements = []

    # Zbieramy wszystkie elementy macierzy wraz z ich indeksami
    for i in range(N):
        for j in range(N):
            elements.append((i + j, j, matrix[i, j]))

    # Sortujemy elementy według sumy indeksów, a w przypadku równości według indeksu kolumny
    elements.sort()

    # Wypisujemy posortowane elementy
    result = [elem[2] for elem in elements]
    print(result)


# Testowanie programu
N = np.random.randint(5, 11)  # Losujemy rozmiar macierzy z przedziału [5, 10]
matrix = np.random.randint(1, 101, size=(N, N))  # Losujemy elementy macierzy z przedziału [1, 100]

matrixTest = np.arange(1, 26).reshape(5, 5)

print("Macierz:")
print(matrixTest)
print("\nElementy macierzy według rosnącej sumy indeksów:")
print_matrix_elements_by_index_sum(matrixTest)
