class GeomSeq:
    def __init__(self, a0, q, N):
        self.a0 = a0
        self.q = q
        self.N = N

    def __getitem__(self, i):
        if i < 0 or i >= self.N:
            raise IndexError
        else:
            if i == 0:
                return self.a0
            else:
                return self.a0 * self.q**i

    def __len__(self):
        return self.N


x = GeomSeq(3, 0.5, 10)

for i in range(len(x)):
    print(x[i])
