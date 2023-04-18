package uni.edu.core;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Map {
    private static int rows = 0;
    private static int columns = 0;

    private ArrayList<Room> rooms = null;

    private static Map map;

    private Map() {
        if (rows * columns == 0) {
            generateSize();
        }
    }

    public static Map Get() {
        if (map == null) {
            map = new Map();
        }
        return map;
    }

    // TODO: hacer validacion de datos con el scanner
    private static void generateSize() {

        Scanner scanner = new Scanner(System.in);

        while (rows <= 0) {
            try {
                System.out.print("Ingrese el numero de filas: ");
                rows = scanner.nextInt();
                if (rows < 0)
                    throw new Exception();
            } catch (Exception e) {
                System.err.println("Debe ingresar un entero positivo!");
            }
        }

        while (columns <= 0) {
            try {
                System.out.print("Ingrese el numero de columnas: ");
                columns = scanner.nextInt();
                if (columns < 0)
                    throw new Exception();
            } catch (Exception e) {
                System.err.println("Debe ingresar un entero positivo!");
            }
        }
        scanner.close();
    }

    /*
     * Se usara el algoritmo Wave Function collapse
     * para poder generar un mapa de cierta cantidad
     * de celdas rows*columns que posteriormente sera
     * usado para dibujarlo usando openGL
     */
    public void generateMap(long... _seed){
        //-comprobacion de la semilla
        long seed = _seed.length > 0? _seed[0] : System.currentTimeMillis();
        Random randomGenerator = new Random(seed);
        //-mapa provisional
        generateRooms();

        Room selectedRoom;
        do{
            //-1 conseguir la celda con menor entropia
            selectedRoom = GetMinorEntropyRoom();
            try{
                //-2 colapsar la celda escogida
                boolean collapsedState = selectedRoom.Collapse(randomGenerator.nextBoolean());

                //-3 propagar la informacion a cada una de
                //- las celdas colindantes a la colapsada y
                //- a las que redujeron su entropia
                NotifyNeighbours(selectedRoom, collapsedState);

                //-marcar todos como no revisados y volver a empezar
                UncheckRooms();
            } catch(NullPointerException e){
                System.out.println("Terminado!");
            }
            
        }
        while(selectedRoom != null);

    }

    private void generateRooms() {
        // -genera habitaciones con la maxima entropia siguiendo el numero de
        // -filas y columnas establecidas
        rooms = new ArrayList<Room>();
        for (int i = 0; i < rows * columns; i++) {
            rooms.add(new Room((i % columns), i / columns));
        }
    }

    @SuppressWarnings("unchecked")
    private Room GetMinorEntropyRoom() {
        // -1 hacer un clon de la lista
        ArrayList<Room> clonList = (ArrayList<Room>) rooms.clone();
        // -2 filtrar los no colapsados y ordenar por entropia (menor a mayor)
        clonList.removeIf((r) -> (r.IsCollapsed()));
        clonList.sort((r1, r2) -> {
            if (r1.GetEntropy() > r2.GetEntropy())
                return 1; // busca el menor
            else
                return -1;
        });

        // -3 devolver el primer elemento
        if (clonList.size() > 0) {
            return clonList.get(0);
        }
        return null;
    }

    private void UncheckRooms() {
        rooms.forEach((r) -> {
            r.Uncheck();
        });
    }

    public Room SearchRoom(int x, int y) {
        Room room = this.rooms.get(y * columns + x);
        return room;
    }

    private void NotifyNeighbours(Room room, boolean collapsedState) {
        ArrayList<Room> neighbours = getNeighbours(room);
        if (collapsedState) {
            neighbours.forEach((r) -> {
                r.IncrementRoomsArround();
            });
        } else {
            neighbours.forEach((r) -> {
                r.IncrementVoidArround();
            });
        }
        neighbours.forEach((r) -> {
            r.Notify();
        });
    }

    private ArrayList<Room> getNeighbours(Room room) {
        ArrayList<Room> neighbours = new ArrayList<>();
        int x = room.GetPositionX();
        int y = room.GetPositionY();
        if (x + 1 < columns) {
            neighbours.add(SearchRoom(x + 1, y));
        }
        if (x - 1 > -1) {
            neighbours.add(SearchRoom(x - 1, y));
        }
        if (y - 1 > -1) {
            neighbours.add(SearchRoom(x, y - 1));
        }
        if (y + 1 < rows) {
            neighbours.add(SearchRoom(x, y + 1));
        }
        return neighbours;
    }

    public void PrintMap() {
        for (Room room : this.rooms) {
            String space = room.GetState()? "#": " ";
            System.out.print(space + ", ");
            if (room.GetPositionX() == columns - 1) {
                System.out.println();
            }
        }
    }
}
