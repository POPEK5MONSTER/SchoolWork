import numpy as np


def adding_edges(temp):
    for row in range(len(temp)):
        for column in range(len(temp[row])):
            if row == 0:
                temp[row][column] = 200
            if row == len(temp) - 1:
                temp[row][column] = 150
            if column == 0:
                temp[row][column] = 100
            if column == len(temp[row]) - 1:
                temp[row][column] = 50

    temp[0][0] = 0
    temp[0][len(temp) - 1] = 0
    temp[len(temp) - 1][0] = 0
    temp[len(temp) - 1][len(temp) - 1] = 0


def temp_calculation(temp, matrix_help):
    for row in range(size[1] - 2):
        for column in range(size[0] - 2):

            index_Help = row * (size[0] - 2) + column
            index_y = row + 1
            index_x = column + 1
            matrix_help[index_Help][index_Help] = -4

            # up
            if temp[index_y - 1][index_x] == 0:
                matrix_help[index_Help][(row - 1) * (size[0] - 2) + column] = 1
            else:
                boundary_temp[index_Help] -= temp[index_y - 1][index_x]

            # down
            if temp[index_y + 1][index_x] == 0:
                matrix_help[index_Help][(row + 1) * (size[0] - 2) + column] = 1
            else:
                boundary_temp[index_Help] -= temp[index_y + 1][index_x]

            # left
            if temp[index_y][index_x - 1] == 0:
                matrix_help[index_Help][row * (size[0] - 2) + (column - 1)] = 1
            else:
                boundary_temp[index_Help] -= temp[index_y][index_x - 1]

            # right
            if temp[index_y][index_x + 1] == 0:
                matrix_help[index_Help][row * (size[0] - 2) + (column + 1)] = 1
            else:
                boundary_temp[index_Help] -= temp[index_y][index_x + 1]


size = [42, 42]
temperature = np.array([[0.0] * size[0]] * size[1])
adding_edges(temperature)
matrix = np.array([[0] * ((size[0] - 2) * (size[1] - 2))] * ((size[0] - 2) * (size[1] - 2)))
boundary_temp = np.array([0] * ((size[0] - 2) * (size[1] - 2)))
temp_calculation(temperature, matrix)
result = np.linalg.solve(matrix, boundary_temp)

for number in range(len(result)):
    x, y = int(number / (size[0] - 2)), number % (size[0] - 2)
    temperature[x + 1][y + 1] = result[number]

for i in temperature:
    for j in i:
        print(str(round(j, 4)).replace('.', ','), end=" ")
    print()

