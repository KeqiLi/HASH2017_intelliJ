import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by KAY on 23/02/2017.
 */
public class Cache {
    private int size, noVid;
    private ArrayList<Integer> endPoints = new ArrayList<Integer>();
    private int[][] requests;
    private int[] vidSize;
    private int[] demand;
    private ArrayList<Integer> videos = new ArrayList<Integer>();
    private int[][] lattencies;
    private int[][] vidLat;
    private int no;

    public Cache(int no, int size, int noVid, int[] vidSize, int[][] requests, int[][] lattencies, int[][] vidLat){
        this.no = no;
        this.size = size;
        this.noVid = noVid;
//        this.requests = new int[noVid];
        this.demand = new int[noVid];
        this.vidSize = vidSize;
        this.requests = requests;
        this.lattencies = lattencies;
        this.vidLat = vidLat;
    }

    public void connectEndPoint(int point){
        this.endPoints.add(point);
    }

    public int[][] recalculate(){
        this.demand = new int[noVid];
        for(int p: endPoints){
            Arrays.setAll(this.demand, i -> this.demand[i] + requests[p][i]*(vidLat[p][i]-lattencies[p][no+1]));
        }

        Pair[] pairs = new Pair[noVid];
        for(int i=0; i<noVid; i++){
            pairs[i] = new Pair(i, demand[i]);
        }

        Arrays.sort(pairs, Collections.reverseOrder());
        int storage = 0;
        for(int i=0; i<noVid; i++){
            if (storage + vidSize[pairs[i].index] <= this.size) {
                storage += vidSize[pairs[i].index];
                this.videos.add(pairs[i].index);
                for(int p: endPoints){
                    vidLat[p][pairs[i].index] = lattencies[p][no+1];
                }
            }
        }

        return vidLat;
    }

    public ArrayList<Integer> getVideos(){
        return this.videos;
    }

    public class Pair implements Comparable<Pair>{
        private int index;
        private int value;
        public Pair(int index, int value){
            this.index = index;
            this.value = value;
        }


        @Override
        public int compareTo(Pair obj){
            if (this.value < obj.value) return -1;
            if (this.value > obj.value) return 1;
            return 0;
        }
    }

}
