import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by xintongyu on 23/02/2017.
 */
public class HashCode2017 {

    //containing the length of each video
    public static int[] videos;
    public static int[][] requests;
    public static int[][] endpointsLatency;
    public static int[][] output;



    public static void main(String[] args) throws IOException {

        Charset charset = Charset.forName("US-ASCII");
        Path file = FileSystems.getDefault().getPath("src", "videos_worth_spreading.in");
        BufferedReader reader = Files.newBufferedReader(file, charset);
        String line = reader.readLine();

        int vidNo = Integer.parseInt(line.split(" ")[0]);
        int endNo = Integer.parseInt(line.split(" ")[1]);
        int reqNo = Integer.parseInt(line.split(" ")[2]);
        int cacheNo = Integer.parseInt(line.split(" ")[3]);
        int cacheSize = Integer.parseInt(line.split(" ")[4]);

        requests = new int[endNo][vidNo];
        endpointsLatency = new int[endNo][cacheNo+1];
        videos = new int[vidNo];

        //store videos
        line = reader.readLine();
        for (int i = 0; i < vidNo; i++) {
            videos[i] = Integer.parseInt(line.split(" ")[i]);
        }

        //store endPointsLatency
        for (int i = 0; i < endNo; i++) {
            line = reader.readLine();
            int centerLatency = Integer.parseInt(line.split(" ")[0]);
            endpointsLatency[i][0] = centerLatency;
            int connectCacheNo = Integer.parseInt(line.split(" ")[1]);
            for (int j = 0; j < connectCacheNo; j++){
                line = reader.readLine();
                int cNo = Integer.parseInt(line.split(" ")[0]);
                int cLat = Integer.parseInt(line.split(" ")[1]);
                endpointsLatency[i][cNo+1] = cLat;
            }
        }

        //video latency
        int[][] vidLat = new int[endNo][vidNo];
        for (int i =0; i< endNo; i++) {
            for (int j =0; j< vidNo; j++) {
                vidLat[i][j] = endpointsLatency[i][0];
            }
        }

        //store requests
        for (int i = 0; i < reqNo; i++) {
            line = reader.readLine();
            int vid = Integer.parseInt(line.split(" ")[0]);
            int end = Integer.parseInt(line.split(" ")[1]);
            int req = Integer.parseInt(line.split(" ")[2]);
            requests[end][vid] = req;
        }

        BufferedWriter bw = null;
        FileWriter fw = null;

        fw = new FileWriter("videos.out");
        bw = new BufferedWriter(fw);
        bw.write(String.format("%d\n", cacheNo));
        System.out.print(String.format("%d\n", cacheNo));

        for (int i = 0; i < cacheNo; i++) {
            Cache c = new Cache(i, cacheSize, vidNo, videos, requests, endpointsLatency, vidLat);
            for (int j = 0; j < endNo; j++) {
                if (endpointsLatency[j][i+1] != 0) {
                    c.connectEndPoint(j);
                }
            }
            vidLat = c.recalculate();
            bw.write(String.format("%d ", i));
            System.out.print(String.format("%d ", i));
            for(int vid: c.getVideos()){
                bw.write(String.format("%d ", vid));
                System.out.print(String.format("%d ", vid));
            }
            System.out.println();
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }
}
