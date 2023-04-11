package uni.edu.core;

import java.util.ArrayList;

public class Map {
    private static int rows;
    private static int columns;

    private ArrayList<ArrayList<Room>> Rooms = null;

    private static Map map;
    
    private Map(){
        if(rows * columns == 0){
            generateSize();
        }
        generateMap();
    }

    private static void generateMap(){

    }

    private static void generateSize(){
        while(rows == 0){
            try {
                
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        while(columns == 0){
            try {
                
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    public static Map Get(){
        if(map == null){
            map = new Map();
        }
        return map;
    }
}
