package config;
import java.util.Locale;
import java.util.Scanner;
import java.util.InputMismatchException;
final public class  DataPreparation {
    private static DataPreparation instance;
    public static double lat;
    public static double lon;
    public static int radius_meters;
    public static final String API_KEY = "e0653b95af4751561059fedf25757f425fc507a4";
    //  public static final String API_SECRET_KEY = "eb189f423a6fdba7a381430d72ce86205d07a305";;

        private DataPreparation(){}
        public static DataPreparation getInstance(){
            if (instance == null){
                instance = new DataPreparation();
            }
            return instance;
        }
        public static void setParameters() {
            Scanner in = new Scanner(System.in).useLocale(Locale.US);;
            double ln, lt; int r;
            while (true) {
                try {
                    System.out.print("Введите значение широты: ");
                    lt = in.nextDouble();
                    if (lt < -90.0 || lt > 90.0) throw new InputMismatchException();
                    lat = lt;
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Вы ввели некорректное значение широты!");
                    in.nextLine();
                }
                finally {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            }
            while (true){
                try{
                    System.out.print("Введите значение долготы: ");
                    ln = in.nextDouble();
                    if (ln < -180.0 || ln > 180.0) throw new InputMismatchException();
                    lon = ln;
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Вы ввели некорректное значение долготы!");
                    in.nextLine();
                }
                finally {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            }
            while (true){
                try{
                    System.out.print("Введите значения радиуса: ");
                    r = in.nextInt();
                    if (r < 0) throw new InputMismatchException();
                    radius_meters = r;
                    break;
                } catch (InputMismatchException e){
                    System.out.println("Вы ввели некорректное значение радиуса");
                    in.nextLine();
                }
                finally {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }
            }
        }


}
