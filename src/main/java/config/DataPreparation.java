package config;
import java.util.InputMismatchException;
final public class  DataPreparation {
    private static DataPreparation instance;

        private DataPreparation(){}
        public static DataPreparation getInstance(){
            if (instance == null){
                instance = new DataPreparation();
            }
            return instance;
        }
        public static int validateLat(String lat) {
            try {
                double lt = Double.parseDouble(lat);
                if (lt < -90.0 || lt > 90.0) throw new NumberFormatException();
                return 1;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        public static int validateLon(String lon){
            try{
                double ln = Double.parseDouble(lon);
                if (ln < -180.0 || ln > 180.0) throw new NumberFormatException();
                return 1;
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
            public static int validateRadiusMeters(String radius_meters){
            try{
                int r = Integer.parseInt(radius_meters);
                if (r < 0) throw new NumberFormatException();
                return 1;
            } catch (NumberFormatException e){
                return 0;
                }
        }
}
