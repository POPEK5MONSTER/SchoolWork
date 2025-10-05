import turtle


def zastosuj_reguly(wyraz):
    reguly = {'X': 'F+[[X]-X]-F[-FX]+X', 'F': 'FF'}
    nowy_wyraz = ''
    for znak in wyraz:
        if znak in reguly:
            nowy_wyraz += reguly[znak]
        else:
            nowy_wyraz += znak
    return nowy_wyraz


def rysuj_rosliny():
    stos = []
    turtle.setheading(90)
    kat = 25
    odleglosc = 5

    wyraz = 'X'
    for i in range(5):
        wyraz = zastosuj_reguly(wyraz)

    for znak in wyraz:
        if znak == 'F':
            turtle.forward(odleglosc)
        elif znak == '+':
            turtle.right(kat)
        elif znak == '-':
            turtle.left(kat)
        elif znak == '[':
            stos.append((turtle.position(), turtle.heading()))
        elif znak == ']':
            pozycja, obrot = stos.pop()
            turtle.penup()
            turtle.goto(pozycja)
            turtle.setheading(obrot)
            turtle.pendown()


rysuj_rosliny()
turtle.done()