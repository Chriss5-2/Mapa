package uni.edu.jade;

import uni.edu.core.wfcAlgorithm.Map;
import uni.edu.core.wfcAlgorithm.Room;

public class DrawRoom extends ImplementedDrawableObject{

	private float[] pivot;
	private float 
	r = 0.0f,
	g = 0.0f,
	b = 1.0f;


	public DrawRoom(Room room, Map map){
		float[] step = {2f/(float)map.GetCollumns(), 2f/(float)map.GetRows()};

		pivot = CalcPivot(room.GetPositionX(), room.GetPositionY(), step);


		float[] vertexArray = {
			//position			//color
			pivot[0], 			pivot[1], 			0.0f,	r, g, b, 1.0f, 	//top left
			pivot[0]+step[0], 	pivot[1], 			0.0f,	r, g, b, 1.0f, 	//top right
			pivot[0]+step[0], 	pivot[1]-step[1], 	0.0f,	r, g, b, 1.0f,	//bottom right
			pivot[0],			pivot[1]-step[1], 	0.0f,	r, g, b, 1,0f,	//bottom left
		};

		int[] elementArray = {
			0,1,2,
			0,2,3
		};

		CreateVAO(vertexArray, elementArray);
	}

	public DrawRoom(Room room, int collumns, int rows){
		float[] step = {2f/(float)collumns, 2f/(float)rows};

		pivot = CalcPivot(room.GetPositionX(), room.GetPositionY(), step);


		float[] vertexArray = {
			//position			//color
			pivot[0], 			pivot[1], 			0.0f,	r, g, b, 1.0f, 	//top left
			pivot[0]+step[0], 	pivot[1], 			0.0f,	r, g, b, 1.0f, 	//top right
			pivot[0]+step[0], 	pivot[1]-step[1], 	0.0f,	r, g, b, 1.0f,	//bottom right
			pivot[0],			pivot[1]-step[1], 	0.0f,	r, g, b, 1,0f,	//bottom left
		};

		int[] elementArray = {
			0,1,2,
			0,2,3
		};

		CreateVAO(vertexArray, elementArray);
	}
	
	private static float[] CalcPivot(int x, int y, float[] step){
		
		float[] newPivot = {-1.0f + x*step[0], 1.0f- y*step[1]};
		
		return newPivot;
	}

	

}
