import math
import numpy as np
import matplotlib.pyplot as plt

# Wartości globalne
g = 6.6743e-11
dt = 7200
n = math.ceil(31556926 / dt)
mk = 7.347e22

# Wartości globalne księżyca
mz = 5.972e24
rzk = 384000000
v_k = math.sqrt(g * mz / rzk)

# Wartości globalne słońca
ms = 1.989e30
rzs = 1.5e11
v_s = math.sqrt(g * ms / rzs)

# Dane wejściowe
xk, yk, vx, vy = 0, rzk, v_k, 0
xz, yz, s_vx, s_vy = 0, rzs, v_s, 0

# Tablice
ksiezyc_r = np.zeros(n)
ksiezyc_theta = np.zeros(n)
ziemia_x = np.zeros(n)
ziemia_y = np.zeros(n)

# Iteracja w krokach
for i in range(n):
    # Calculate Moon's position and velocity
    ksiezyc_r[i] = np.sqrt((xk + xz)**2 + (yk + yz)**2)
    ksiezyc_theta[i] = np.arctan2(yk + yz, xk + xz)
    ziemia_x[i], ziemia_y[i] = xz, yz

    # Aktualizacja prędkości i pozycji księżyca
    wx, wy = 0 - xk, 0 - yk
    dzk = math.sqrt(wx**2 + wy**2)
    ux, uy = wx / dzk, wy / dzk
    a = g * mz / dzk**2
    ax, ay = ux * a, uy * a

    # Aktualizacja prędkości i pozycji księżyca — midpoint
    m_xk, m_yk = xk + vx * dt / 2, yk + vy * dt / 2
    m_vx, m_vy = vx + ax * dt / 2, vy + ay * dt / 2
    dx, dy = m_vx * dt, m_vy * dt
    dvx, dvy = ax * dt, ay * dt

    xk, yk = xk + dx, yk + dy
    vx, vy = vx + dvx, vy + dvy

    # Aktualizacja prędkości i pozycji ziemi
    s_wx, s_wy = 0 - xz, 0 - yz
    dzs = math.sqrt(s_wx**2 + s_wy**2)
    s_ux, s_uy = s_wx / dzs, s_wy / dzs
    s_a = g * ms / dzs**2
    s_ax, s_ay = s_ux * s_a, s_uy * s_a

    # Aktualizacja prędkości i pozycji księżyca — midpoint
    m_xz, m_yz = xz + s_vx * dt / 2, yz + s_vy * dt / 2
    m_s_vx, m_s_vy = s_vx + s_ax * dt / 2, s_vy + s_ay * dt / 2
    s_dx, s_dy = m_s_vx * dt, m_s_vy * dt
    s_dvx, s_dvy = s_ax * dt, s_ay * dt

    xz, yz = xz + s_dx, yz + s_dy
    s_vx, s_vy = s_vx + s_dvx, s_vy + s_dvy

ksiezyc_x = rzk * np.cos(ksiezyc_theta)
ksiezyc_y = rzk * np.sin(ksiezyc_theta)

ksiezyc_pozycja = np.column_stack((ksiezyc_x, ksiezyc_y))
ziemia_pozycja = np.column_stack((ziemia_x, ziemia_y))

ksiezyc_max_x = np.max(np.abs(ksiezyc_pozycja[:, 0]))
ksiezyc_max_y = np.max(np.abs(ksiezyc_pozycja[:, 1]))
ziemia_max_x = np.max(np.abs(ziemia_pozycja[:, 0]))
ziemia_max_y = np.max(np.abs(ziemia_pozycja[:, 1]))

plt.figure()
plt.plot(ksiezyc_pozycja[:, 0], ksiezyc_pozycja[:, 1], label="Księżyc")
plt.xlabel("x (m)")
plt.ylabel("y (m)")
plt.title("Orbita ksiezyca")
plt.xlim(-ksiezyc_max_x, ksiezyc_max_x)
plt.ylim(-ksiezyc_max_y, ksiezyc_max_y)
plt.legend()
plt.grid()
plt.show()

plt.figure()
plt.plot(ziemia_pozycja[:, 0], ziemia_pozycja[:, 1], label="Ziemia")
plt.xlabel("x (m)")
plt.ylabel("y (m)")
plt.title("Orbita ziemi")
plt.xlim(-ziemia_max_x, ziemia_max_x)
plt.ylim(-ziemia_max_y, ziemia_max_y)
plt.legend()
plt.grid()
plt.show()