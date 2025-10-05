import numpy as np
import matplotlib.pyplot as plt

# Parametry fali
A = 1.0  # Amplituda
k = 2 * np.pi / 10  # Liczba falowa
omega = 2 * np.pi / 5  # Częstotliwość

# Tworzenie siatki czasowej i przestrzennej
x = np.linspace(0, 100, 1000)
t = np.linspace(0, 20, 5)  # Pięć chwil czasowych


# Funkcja fali
def wave_func(x, t):
    return A * np.sin(k * x - omega * t)


# Tworzenie wykresów dla różnych chwil czasowych
plt.figure(figsize=(12, 8))
for ti in t:
    plt.plot(x, wave_func(x, ti), label=f't={ti:.1f}')

plt.xlabel('Odległość')
plt.ylabel('Amplituda')
plt.title('Symulacja propagacji fali na wodzie')
plt.grid(True)
plt.show()
