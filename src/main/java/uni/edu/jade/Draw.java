package uni.edu.jade;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.util.ArrayList;

import uni.edu.core.wfcAlgorithm.Map;
import uni.edu.core.wfcAlgorithm.Room;

public class Draw {
    public final int DEFAULT_SHADER = 0;
    private static Shader defaultShader;

    private static ArrayList<DrawableObject> mapVAO = new ArrayList<>();
    private static ArrayList<DrawableObject> grid = new ArrayList<>();

    public static void CompileShaders() {

        defaultShader = new Shader(
                "src/main/java/uni/edu/assets/shaders/defaultVertex.glsl",
                "src/main/java/uni/edu/assets/shaders/defaultFragment.glsl");
    }

    public static void SetMapVAO(Map map) {
        ArrayList<Room> rooms = map.GetRooms();
        rooms.removeIf(r -> !r.GetState());

        for (Room room : rooms) {
            mapVAO.add(new DrawRoom(room, map));

        }
    }

    public static void SetGridVAO(int columns, int rows) {

        int[] elementArray = { 0, 1 };

        // VAO columns
        for (int index = 1; index < columns; index++) {
            float step = 2f / (float) columns;
            float[] vertexArray = {
                    -1f + index * step, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                    -1f + index * step, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            };
            ImplementedDrawableObject line = new ImplementedDrawableObject();
            line.CreateVAO(vertexArray, elementArray);
            grid.add(line);
        }

        // VAO rows
        for (int index = 1; index < rows; index++) {
            float step = 2f / (float) rows;
            float[] vertexArray = {
                    1.0f, -1f + index * step, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                    -1.0f, -1f + index * step, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            };
            ImplementedDrawableObject line = new ImplementedDrawableObject();
            line.CreateVAO(vertexArray, elementArray);
            grid.add(line);
        }
    }

    public static void DrawTest() {

        float[] vertexArray = {
                // position //color
                0.5f, 0.0f, 0.0f, 0.37f, 0.73f, 0.58f, 1.0f,
                -0.5f, 0.0f, 0.0f, 0.37f, 0.73f, 0.58f, 1.0f,
                0.0f, 0.5f, 0.0f, 0.37f, 0.73f, 0.58f, 1.0f,
        };

        int[] elementArray = {
                0, 1, 2
        };

        ImplementedDrawableObject object = new ImplementedDrawableObject();

        object.CreateVAO(vertexArray, elementArray);

        object.Draw(defaultShader, GL_TRIANGLES, 3);

    }

    public static void DrawMap(Map map) {
        defaultShader.Use();
        for (DrawableObject object : mapVAO) {
            object.Draw(defaultShader, GL_TRIANGLES, 6);

        }
        DrawGrid();
    }

    private static void DrawGrid() {
        for (DrawableObject line : grid) {
            line.Draw(defaultShader, GL_LINES, 2);
        }
    }

    public Shader GetShader(int shaderID) {
        switch (shaderID) {
            case 0:
                return defaultShader;
            default:
                System.out.println("ERROR: shaderID unknown");
                return null;
        }
    }

}
