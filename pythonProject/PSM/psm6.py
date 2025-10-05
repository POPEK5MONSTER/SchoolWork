import math

L = math.pi
n = 10
dx = L / n
dt = 0.2
dl = 30


def addList():
    list1 = []
    list2 = []
    list3 = []
    return list1, list2, list3


def run():
    list_t = []
    list_x = [dx * z for z in range(n + 1)]

    list_u, list_v, list_a = addList()

    list_u2, list_v2, list_a2 = addList()

    list_ep, list_ek, list_ec = addList()

    for z in range(dl):

        list_t = [0]

        for e in range(dl - 1):
            list_t.append(list_t[e] + dt)

        list_ui, list_vi, list_ai = [0], [0], [0]
        list_ui2, list_vi2, list_ai2 = [0], [0], [0]

        list_ep.append(0)
        list_ek.append(0)

        if z == 0:
            for i in range(1, n):
                list_ui.append(math.sin(list_x[i]))
                list_vi.append(0)
        else:
            for i in range(1, n):
                list_ui.append(list_u[z - 1][i] + list_v2[z - 1][i] * dt)
                list_vi.append(list_v[z - 1][i] + list_a2[z - 1][i] * dt)

        list_ui.append(0)
        list_vi.append(0)

        for i in range(1, n):
            list_ai.append((list_ui[i - 1] - 2 * list_ui[i] + list_ui[i + 1]) / (dx ** 2))

        list_ai.append(0)

        for i in range(1, n):
            list_ui2.append(list_ui[i] + (list_vi[i] * dt) / 2)
            list_vi2.append(list_vi[i] + (list_ai[i] * dt) / 2)

        list_ui2.append(0)
        list_vi2.append(0)

        for i in range(1, n):
            list_ai2.append((list_ui2[i - 1] - 2 * list_ui2[i] + list_ui2[i + 1]) / (dx ** 2))

        list_ai2.append(0)

        for e in range(n):
            list_ep[z] += (list_ui2[e + 1] - list_ui2[e]) ** 2

        for e in range(n):
            list_ek[z] += list_vi2[e] ** 2

        list_ep[z] *= 1 / (2 * dx)
        list_ek[z] *= dx / 2
        list_ec.append(list_ep[z] + list_ek[z])

        list_u.append(list_ui)
        list_v.append(list_vi)
        list_a.append(list_ai)

        list_u2.append(list_ui2)
        list_v2.append(list_vi2)
        list_a2.append(list_ai2)

    return list_ep, list_ek, list_ec, list_t


ep, ek, ec, t = run()

print("T    Ek      Ep      Ec")
for i in range(len(t)):
    print(str(round(t[i], 1)).replace('.', ','),
          str(ek[i]).replace('.', ','),
          str(ep[i]).replace('.', ','),
          str(ec[i]).replace('.', ','))


