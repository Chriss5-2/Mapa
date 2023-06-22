package uni.edu.core.wfcAlgorithm;

import java.util.ArrayList;
import java.util.HashSet;
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
		long seed = _seed != null? _seed[0] : System.currentTimeMillis();
		Random randomGenerator = new Random(seed);

		//-mapa de celdas
		generateRooms();

		Room selectedRoom;

		selectedRoom = GetMinorEntropyRoom();

		do{

			//-2 colapsar la celda escogida
			boolean collapsedState = selectedRoom.Collapse(
				randomGenerator.nextBoolean(), 
				selectedRoom.GetNeighbours()
			);

			//PrintMap();
			//-3 propagar la informacion a cada una de
			//- las celdas colindantes a la colapsada y
			//- a las que redujeron su entropia
			NotifyNeighbours(selectedRoom, collapsedState);
			
			//PrintEntropy();

			//-marcar todos como no revisados y volver a empezar
			UncheckRooms();

			//-1 conseguir la celda con menor entropia
			selectedRoom = GetMinorEntropyRoom();
		}
		while(selectedRoom != null);

		System.out.println("terminado!");

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
			return (r1.GetEntropy() > r2.GetEntropy())? 1: -1; //busca al menor
		});

		// -3 devolver el primer elemento
		if (clonList.size() > 0) {
			return clonList.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Room> GetSurrounding(){

		ArrayList<Room> surrounding = (ArrayList<Room>) rooms.clone();

		surrounding.removeIf((r) -> r.IsCollapsed());
		surrounding.removeIf((r) -> r.GetEntropy() == 3); //remueve celdas con entropia 

		return surrounding;
	}

	private void UncheckRooms() {
		rooms.forEach((r) -> {
			r.Uncheck();
		});
	}

	public Room SearchRoom(int x, int y) {
		if(x >= columns || y >= rows || x < 0 || y < 0) return null;
		Room room = this.rooms.get(y * columns + x);
		return room;
		
	}

	private void NotifyNeighbours(Room room, boolean collapsedState) {
		HashSet<Room> cells = new HashSet<>();
		//- notificar alrededor: Vecinos - vecinos nulos/colapsados + celdas con entropia < 3

		cells.addAll(room.GetNeighbours());
		cells.addAll(GetSurrounding());

		cells.removeIf((r -> r==null));
		cells.removeIf((r -> r.IsCollapsed()));

		cells.forEach((r) -> {
			r.Notify();
		});
	}

	public void PrintMap() {
		for (Room room : this.rooms) {
			String space = room.GetState()? "#": " ";
			if(!room.IsCollapsed()) space = " ";
			System.out.print(space + ", ");
			if (room.GetPositionX() == columns - 1) {
				System.out.println();
			}
		}
	}

	public void PrintEntropy(){
		for (Room room : rooms) {
			System.out.print(room.GetEntropy() + ", ");
			if (room.GetPositionX() == columns - 1) {
				System.out.println();
			}
		}
	}

	public ArrayList<Room> GetRooms(){
		return rooms;
	}

	public int GetRows(){
		return rows;
	}

	public int GetCollumns(){
		return columns;
	}

}
