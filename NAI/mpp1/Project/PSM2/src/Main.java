import java.text.DecimalFormat;

public class Main {
    static double V0 = 10; // Prędkość początkowa
    static double alfa_rad = Math.PI / 4;

    public static void main(String[] args) {
        System.out.println("Metoda Eulera:");
        eulerMethod(0.1);
        System.out.println("\nUlepszona metoda Eulera:");
        improvedEulerMethod(0.1);
    }

    public static void eulerMethod(double q) {
        DecimalFormat df = new DecimalFormat("#,##0.000000");
        double Sx = 0.0;
        double Sy = 0.0;
        double Vx = V0 * Math.cos(alfa_rad);
        double Vy = V0 * Math.sin(alfa_rad);
        double m = 1.0; // Masa
        double dt = 0.1;

        for (int i = 0; i < 1000; i++) {
            if (Sy < -1)
                break;
            if (i == 0)
                System.out.println((int) Sx + " " + (int) Sy);

            double gy = -9.81 * m;
            double gx = 0;

            double Fx = -q * Vx * Vx;
            double Fy = -q * Vy * Vy;

            double DSx = Vx * dt;
            double DSy = Vy * dt;

            double DVx = (gx + Fx) / m * dt;
            double DVy = (gy + Fy) / m * dt;

            Vx += DVx;
            Vy += DVy;
            Sx += DSx;
            Sy += DSy;
            System.out.println(df.format(Sx).replace('.', ',') + " " + df.format(Sy).replace('.', ','));
        }
    }

    public static void improvedEulerMethod(double q) {
        DecimalFormat df = new DecimalFormat("#,##0.000000");
        double Sx = 0.0;
        double Sy = 0.0;
        double Vx = V0 * Math.cos(alfa_rad);
        double Vy = V0 * Math.sin(alfa_rad);
        double m = 1.0; // Masa
        double dt = 0.1;

        for (int i = 0; i < 1000; i++) {
            if (Sy < -1)
                break;
            if (i == 0)
                System.out.println((int) Sx + " " + (int) Sy);

            double gy = -9.81 * m;
            double gx = 0;

            double Fx = -q * Vx * Vx;
            double Fy = -q * Vy * Vy;

            double Vx0 = Vx;
            double Vy0 = Vy;
            double DVx1 = (gx + Fx) / m * dt;
            double DVy1 = (gy + Fy) / m * dt;

            double Vx1 = Vx0 + DVx1;
            double Vy1 = Vy0 + DVy1;

            double DSx = (Vx0 + Vx1) / 2 * dt;
            double DSy = (Vy0 + Vy1) / 2 * dt;
            double DVx = (gx + Fx) / m * dt;
            double DVy = (gy + Fy) / m * dt;

            Vx += DVx;
            Vy += DVy;
            Sx += DSx;
            Sy += DSy;
            System.out.println(df.format(Sx).replace('.', ',') + " " + df.format(Sy).replace('.', ','));
        }
    }
}
