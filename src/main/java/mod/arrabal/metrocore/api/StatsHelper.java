package mod.arrabal.metrocore.api;



/**
 * Created by Arrabal on 6/19/2014.
 */
public class StatsHelper {

    public int getMean(int[] data){
        if (data.length > 0) {
            int sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += data[i];
            }
            return sum / data.length;
        }
        return 0;
    }

    public int getStandardDeviation(int[] data, int mean){
        int SSD = 0;
        for (int i = 0; i < data.length; i ++){
            SSD += (data[i] - mean) * (data[i] - mean);
        }
        double variation = ((double) SSD) / ((double) data.length - 1.0d);
        double stDev = Math.sqrt(variation);
        return (int) Math.floor(stDev);
    }

    public int getStandardDeviation(int[] data){
        if (data.length > 0){
            return getStandardDeviation(data, getMean(data));
        }
        return 0;
    }

    public static int getStaticMean(int[] data){
        if (data.length > 0) {
            int sum = 0;
            for (int i = 0; i < data.length; i++) {
                sum += data[i];
            }
            return sum / data.length;
        }
        return 0;
    }

    public static int getStaticStandardDeviation(int[] data, int mean){
        int SSD = 0;
        for (int i = 0; i < data.length; i ++){
            SSD += (data[i] - mean) * (data[i] - mean);
        }
        double variation = ((double) SSD) / ((double) data.length - 1.0d);
        double stDev = Math.sqrt(variation);
        return (int) Math.floor(stDev);
    }

    public static int getStaticStandardDeviation(int[] data){
        if (data.length > 0){
            return getStaticStandardDeviation(data, getStaticMean(data));
        }
        return 0;
    }
}
