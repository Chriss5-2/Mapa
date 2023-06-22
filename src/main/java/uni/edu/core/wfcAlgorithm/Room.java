package uni.edu.core.wfcAlgorithm;

import java.util.ArrayList;

public class Room {

	/**
	 *Estado al que ah colapsado una celda <p>
	 * <strong>true: </strong> Habitacion <p>
	 * <strong>false:</strong> Vacio (Void)
	 */
	private boolean state;
	private int positionX;
	private int positionY;

	private boolean checked;
	private boolean collapsed;

	private int entropy;

	private Integer valueToCollapse;

	public Room(int x, int y){
		this.positionX = x;
		this.positionY = y;

		this.collapsed = false;
		this.checked = false;

		this.entropy = 3;
		this.valueToCollapse = null;
	}

	public Room(int x, int y, int entropy){
		this(x, y);
		this.entropy = entropy;
	}
	
	public boolean Collapse(boolean state, ArrayList<Room> neighbours){
		this.collapsed = true;
		if(this.entropy == 1){
			this.state = valueToCollapse == 1;
			return this.state;
		}

		this.entropy = 0;
		this.state = state;
		return state;
	}

	public void Notify(){
		//debo revisar a los vecinos y determinar segun estos si se requiere un colapso
		//las celdas no colapsadas influyen en el calculo de la entropia?
		ArrayList<Room> neighbours = this.GetNeighbours();
		ArrayList<Integer> values = new ArrayList<>();
			
		for (Room room : neighbours) {
			if(room == null) continue;
			values = room.CheckConditions();
			if(values.get(0) == 1) break;
		}

		entropy = values.get(0);
		valueToCollapse = values.get(1);

		checked = true;
	}

	/**
	 * @return
	 * <p>valor de la entropia en el <strong>index 0</strong> </p>
	 * <p>valor al que deberia colapsar la habitacion en el <strong> index 1 </strong>
	 */
	private ArrayList<Integer> CheckConditions(){
		ArrayList<Integer> output = new ArrayList<>();
		Integer entropy = 2;
		Integer room = null;
		
		ArrayList<Room> neighbours = this.GetNeighbours();
		neighbours.removeIf(r -> r==null);

		int voidArround = 0;
		int roomArround = 0;
		int noNullArround = neighbours.size();

		for (Room r : neighbours) {
			if(!r.collapsed) continue;
			if(r.state) roomArround++;
			else voidArround++;
		}
		//-revision de estados por cantidad alrededor de una habitacion
		if(this.state == true){
			if(noNullArround - voidArround == 1){
				entropy = 1;
				room = 1;
			}
			else if(noNullArround - roomArround == 1){
				entropy = 1;
				room = 0;
			}
			else{
				entropy = 2;
			}
		}

		output.add(0, entropy);
		output.add(1, room);
		return output;
	}

	

	//{RightRoom, DownRoom, LeftRoom, UpRoom}
	public ArrayList<Room> GetNeighbours() {
		Map map = Map.Get();
		ArrayList<Room> neighbours = new ArrayList<>();
		int x = this.GetPositionX();
		int y = this.GetPositionY();
		
		neighbours.add(map.SearchRoom(x + 1, y));
		neighbours.add(map.SearchRoom(x, y + 1));
		neighbours.add(map.SearchRoom(x - 1, y));
		neighbours.add(map.SearchRoom(x, y - 1));
		
		return neighbours;
	}

	public void Uncheck(){
		this.checked = false;
	}

	public int GetEntropy(){
		return entropy;
	}

	public void SetEntropy(int entropy){
		this.entropy = entropy;
	}

	public boolean IsCollapsed(){
		return collapsed;
	}

	public int GetPositionX(){
		return this.positionX;
	}

	public int GetPositionY(){
		return this.positionY;
	}

	public boolean IsChecked(){
		return this.checked;
	}

	public boolean GetState(){
		return this.state;
	}
}
