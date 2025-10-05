import turtle


class Screen:
    def __init__(self):
        self.window = turtle.Screen()
        self.window.title("Fractal plant")
        self.window.bgcolor("lightblue")
        self.turtle = turtle.Turtle()
        self.turtle.color("brown")
        self.turtle.goto(0, 0)
        self.turtle.left(25)
        self.turtle.speed(0)


def draw(move, screen):
    stack = []
    for char in move:
        if char == 'F':
            screen.turtle.forward(5)
        elif char == '-':  # right
            screen.turtle.right(25)
        elif char == '+':  # left
            screen.turtle.left(25)
        elif char == '[':
            coords = [screen.turtle.xcor(), screen.turtle.ycor()]
            angle = screen.turtle.heading()
            stack.append((angle, coords))
        elif char == ']':
            angle, coords = stack.pop()
            screen.turtle.penup()
            screen.turtle.setheading(angle)
            screen.turtle.goto(coords[0], coords[1])
            screen.turtle.pendown()

    screen.turtle.hideturtle()
    turtle.done()


def instruction(instructions, X, F, n):
    for i in range(n):
        instructions = instructions.replace("F", F)
        instructions = instructions.replace("X", X)
    return instructions


def main():
    n = 5
    start = "X"
    X = "F+[[X]-X]-F[-FX]+X"
    F = "FF"

    screen = Screen()
    instructions = instruction(start, X, F, n)
    draw(instructions, screen)


if __name__ == "__main__":
    main()
