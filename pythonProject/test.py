import numpy as np


def solve_gaussian(A, B):
    n = len(A)
    for p in range(n):
        max_index = np.argmax(np.abs(A[p:, p])) + p
        A[[p, max_index]] = A[[max_index, p]]
        B[p], B[max_index] = B[max_index], B[p]
        if abs(A[p, p]) <= 1e-10:
            raise RuntimeError("Matrix is singular or nearly singular")
        for i in range(p + 1, n):
            alpha = A[i, p] / A[p, p]
            B[i] -= alpha * B[p]
            A[i, p:] -= alpha * A[p, p:]
    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = (B[i] - np.sum(A[i, i + 1:] * x[i + 1:])) / A[i, i]
    return x


class Matrix:
    def __init__(self, size, top, left, bottom, right):
        self.matrix = np.zeros((size * size, size * size))
        self.vector = np.zeros(size * size)
        self.size = size
        self.top = top
        self.left = left
        self.bottom = bottom
        self.right = right

    def calculate(self):
        index = 0
        for i in range(self.size):
            for j in range(self.size):
                self.calculate_top_part(i, j, index)
                self.calculate_lower_part(i, j, index)
                self.calculate_right_side(i, j, index)
                self.calculate_left_side(i, j, index)
                self.matrix[index, index] = -4
                index += 1

    def calculate_top_part(self, i, j, index):
        if i == self.size - 1:
            self.vector[index] += self.top
        else:
            self.matrix[index, (i + 1) * self.size + j] = 1

    def calculate_lower_part(self, i, j, index):
        if i == 0:
            self.vector[index] += self.bottom
        else:
            self.matrix[index, (i - 1) * self.size + j] = 1

    def calculate_left_side(self, i, j, index):
        if j == self.size - 1:
            self.vector[index] += self.left
        else:
            self.matrix[index, i * self.size + j + 1] = 1

    def calculate_right_side(self, i, j, index):
        if j == 0:
            self.vector[index] += self.right
        else:
            self.matrix[index, i * self.size + j - 1] = 1

    def get_matrix(self):
        return self.matrix

    def get_vector(self):
        return self.vector


def main():
    count_matrix = Matrix(40, 200, 100, 150, 50)
    count_matrix.calculate()
    answer = solve_gaussian(count_matrix.get_matrix(), count_matrix.get_vector())
    for i in range(39, -1, -1):
        for j in range(39, -1, -1):
            print(f"{answer[40 * i + j]:.2f}", end=" ")
        print()
d


if __name__ == "__main__":
    main()