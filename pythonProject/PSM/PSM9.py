import numpy as np


class GameOfLife:

    def __init__(self, grid_size, game_of_life_rules):
        self.size = grid_size
        self.game_of_life_rules = game_of_life_rules

        self.field = np.zeros((grid_size, grid_size), dtype=int)
        self.new_field = np.zeros((grid_size, grid_size), dtype=int)

    def set_field_alive(self, x, y):
        self.field[x][y] = 1

    def print_fields(self):
        for i in range(self.size):
            for j in range(self.size):
                print('■' if self.field[i][j] == 1 else '0', end=' ')
            print()

    def iteration(self):
        for x in range(self.size):
            for y in range(self.size):

                current_x, current_y = np.meshgrid(

                    np.mod(np.arange(x - 1, x + 2), self.size),
                    np.mod(np.arange(y - 1, y + 2), self.size),

                    indexing="ij",
                )

                neighbours_value = np.sum(self.field[current_x, current_y]) - self.field[x, y]

                if self.field[x][y] == 1:
                    self.new_field[x][y] = self.game_of_life_rules.should_die(neighbours_value)
                else:
                    self.new_field[x][y] = self.game_of_life_rules.should_resurrect(neighbours_value)

        self.field = np.copy(self.new_field)


class GameRules:
    def __init__(self):
        self.death_rules = np.zeros(10, dtype=int)
        self.resurrection_rules = np.zeros(10, dtype=int)

    def set_rules(self, death_rules, resurrection_rules):
        self.death_rules = np.zeros(10, dtype=int)
        self.resurrection_rules = np.zeros(10, dtype=int)

        for c in death_rules:
            i = int(c)
            if i <= 8:
                self.death_rules[i] = 1

        for c in resurrection_rules:
            j = int(c)
            if j <= 8:
                self.resurrection_rules[j] = 1

    def should_resurrect(self, neighbours_value):
        # 1 - jak tak
        # 0 - jak nie
        return 1 if self.resurrection_rules[neighbours_value] > 0 else 0

    def should_die(self, neighbours_value):
        # 1 - jak tak
        # 0 - jak nie
        return 1 if self.death_rules[neighbours_value] > 0 else 0


def main():
    grid_size = int(input("Rozmiar siatki (np. 20): "))
    death_rules = input("Zasady dla pozostania żywym (np. 23) : ")
    resurrection_rules = input("Zasady dla ożywiania komórki (np. 3): ")
    game_rules = GameRules()
    game_rules.set_rules(death_rules, resurrection_rules)
    game_of_life = GameOfLife(grid_size, game_rules)

    user_input = input("Czy chcesz wybrać stan początkowy? (t/n): ")
    if user_input == "t":
        user_input = input("Czy chcesz wybrać stan początkowy losowo? (t/n): ")
        if user_input == "t":
            alive_cells = int(input("Podaj liczbę żywych komórek: "))
            for i in range(alive_cells):
                x = np.random.randint(0, grid_size)
                y = np.random.randint(0, grid_size)
                game_of_life.set_field_alive(x, y)
        else:
            game_of_life.set_field_alive(15, 15)
            game_of_life.set_field_alive(16, 15)
            game_of_life.set_field_alive(17, 15)
            game_of_life.set_field_alive(17, 14)
            game_of_life.set_field_alive(16, 13)

    input("Pierwsza iteracja")
    max_grid_size = grid_size - 1
    print(game_of_life.field[max_grid_size][max_grid_size])

    iterations = 0

    while True:
        user_input = input()
        if user_input == "quit":
            break
        if user_input == "change":
            # możliwość zmiany reguł
            death_rules = input("Zasady dla życia: ")
            resurrection_rules = input("Zasady dla ożywiania: ")
            game_rules.set_rules(death_rules, resurrection_rules)
            game_of_life.game_of_life_rules = game_rules

        print("iteracja: ", iterations)
        game_of_life.print_fields()
        game_of_life.iteration()
        iterations += 1
        print("Aby zmienić zasady wpisz 'change' albo 'quit' by wyjść z programu")
        print("Aby przejść do kolejnej iteracji wciśnij Enter")


if __name__ == "__main__":
    main()  # 20,23,3