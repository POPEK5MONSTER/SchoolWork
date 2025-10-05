import numpy as np


edge = {
    'left': 100,
    'right': 50,
    'top': 200,
    'bottom': 150
}


def gen_equation(n):
    M = [[0] * ((n - 1) ** 2) for _ in range((n - 1) ** 2)]
    y = [0] * ((n - 1) ** 2)
    for i in range((n - 1) ** 2):
        row, col = i // (n - 1), i % (n - 1)
        M[i][row * (n - 1) + col] = -4
        if row - 1 < 0:
            y[i] -= edge['top']
        else:
            M[i][row * (n - 1) + col - (n - 1)] = 1
        if row + 1 >= (n - 1):
            y[i] -= edge['bottom']
        else:
            M[i][row * (n - 1) + col + (n - 1)] = 1
        if col - 1 < 0:
            y[i] -= edge['left']
        else:
            M[i][row * (n - 1) + col - 1] = 1
        if col + 1 >= (n - 1):
            y[i] -= edge['right']
        else:
            M[i][row * (n - 1) + col + 1] = 1
    return M, y


n = 41

M, y = gen_equation(n)
M = np.array(M)
y = np.array(y)
x = np.linalg.solve(M, y)
x = x.reshape((n - 1, n - 1))
# wrap solved equation with initial edge starting values
left = np.full((n - 1, 1), edge['left'])
right = np.full((n - 1, 1), edge['right'])
top = np.concatenate([np.array([[0]]), np.full((1, n - 1), edge['top']), np.array([[0]])], axis=1)
bottom = np.concatenate([np.array([[0]]), np.full((1, n - 1), edge['bottom']), np.array([[0]])], axis=1)
x = np.concatenate([left, x, right], axis=1)
x = np.concatenate([top, x, bottom], axis=0)

for i in x:
    print(i, end="\n")


