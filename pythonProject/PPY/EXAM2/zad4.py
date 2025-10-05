import numpy as np
import math


def average_decorator(func):
    def wrapper(x):
        func_value = func(x)

        samples = np.arange(x - 1, x + 1, 0.1)

        func_values = [func(sample) for sample in samples]

        avg_value = sum(func_values) / len(func_values)

        return (func_value, avg_value)


    return wrapper


@average_decorator
def f1(x):
    return math.exp(math.sin(x + 1))


@average_decorator
def f2(x):
    return math.cos(math.exp(- x ** 2))


for x in [1, 2, 3]:
    print(f"f1({x}) = {f1(x)}")
    print(f"f2({x}) = {f2(x)}")
