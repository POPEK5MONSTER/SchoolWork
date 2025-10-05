def palindrom(x):
    n = x
    reversed = 0
    while n > 0:
        digit = n % 10
        reversed = reversed * 10 + digit
        n //= 10
    if reversed == x:
        print(f"Liczba {x} jest palindromem.")
    else:
        print(f"Liczba {x} nie jest palindromem.")


def max_min(l):
    max = l[0]
    min = l[0]
    for number in l:
        if max < number:
            max = number
        elif min > number:
            min = number
    print(f"Max: {max}\nMin: {min}")


def wypisz_w_kolejnosci(l):
    l2 = []
    for y in range(len(l)):
        l1 = []
        index = 0
        max = 0
        for x1 in range(len(l)):
            if max < l[x1][y]:
                max = l[x1][y]
                index = x1
        l1.append(max)
        for x2 in range(1, len(l)):
            l1.append(l[index - x2][y])
        l2.append(l1)
    print(l2)


def rozklad(N):
    rozklad_list = []
    for a in range(1, int(N ** 0.5) + 1):
        for b in range(1, int(N ** 0.5) + 1):
            if a ** 2 + b ** 2 == N:
                rozklad_list.append([a, b])
    if len(rozklad_list) >= 6:
        return rozklad_list
    else:
        return [0]


def liczba_rozkladow(N):
    lrozkladow = []
    for i in range(1, N + 1):
        x = len(rozklad(i))
        if (x >= 3):
            print(f"\nliczba {i} ma takie rozklady:")
            lrozkladow.append(i)
            for j in range(len(rozklad(i))//2):
                print(rozklad(i)[j], end=" ")
    print(f"\n{lrozkladow}")


class wec:
    def __init__(self, a, b, c):
        self._a = a
        self._b = b
        self._c = c

    def __str__(self):
        return f'[{self._a}, {self._b}, {self._c}]'

    @property
    def a(self):
        return self._a

    @property
    def b(self):
        return self._b

    @property
    def c(self):
        return self._c

    def __add__(self, other):
        return wec(self._a + other.a, self._b + other.b, self._c + other.c)

    def __sub__(self, other):
        return wec(self._a - other.a, self._b - other.b, self._c - other.c)

    def __mul__(self, other):
        if type(self) == wec:
            return wec(self._a * other, self._b * other, self._c * other)

    def __rmul__(self, other):
         return self.__mul__(other)

    def iloczynsklarny(self, other):
        return self._a * other.a + self._b * other.b + self._c * other.c


palindrom(25752)
print("----------------")
max_min([5, 5, 10, 7, 4, 10, 3, 6, 5, 7])
print("----------------")
wypisz_w_kolejnosci([[1, 2, 3, 16],
         [13, 6, 15, 8],
         [9, 10, 15, 12],
         [5, 14, 7, 4]])
print("----------------")
liczba_rozkladow(1000)
print("----------------")
x = wec(1, 2, 3)
y = wec(1, 2, 3)
print(x)
print(x + y)
print(x - y)
print(x * 3)
print(3 * x)
print(wec.iloczynsklarny(x, y))

