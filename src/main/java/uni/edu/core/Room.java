package uni.edu.core;

import java.util.ArrayList;
import java.util.HashMap;

public class Room {

    private boolean state;
    private int positionX;
    private int positionY;

    private boolean checked;
    private boolean collapsed;
    private int roomsArround;
    private int voidArround;

    private int entropy;

    public Room(int x, int y){
        this.positionX = x;
        this.positionY = y;

        this.collapsed = false;
        this.checked = false;

        this.roomsArround = 0;
        this.entropy = Integer.MAX_VALUE;
    }

    public Room(int x, int y, int entropy){
        this(x, y);
        this.entropy = entropy;
    }
    
    public boolean Collapse(boolean state){
        this.collapsed = true;
        if(this.entropy == 0){
            this.state = 
            (Boolean)
            this.CheckConditions()
            .values()
            .toArray()[0];
            return this.state;
        }

        this.entropy = 0;
        this.state = state;
        return state;
    }
    public void Notify(){
        if(this.collapsed)return;
        this.SetEntropy(
            (int)
            this.CheckConditions()
            .keySet()
            .toArray()[0]
        );
        checked = true;
    }

    private HashMap<Integer, Boolean> CheckConditions(){
        Boolean room = null;
        Integer newEntropy = null;
        HashMap <Integer, Boolean> output = new HashMap<>();
        
        if(this.entropy == 0){
            //- define a donde deberia colapsar
            
            output.put(newEntropy, room);
            return output;
        }
        //- Revisa las condiciones no admisibles y determina si la habitacion deberia colapsar
        
        newEntropy = 2;
        output.put(newEntropy, room);
        return output;
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

    public int GetRoomsArround(){
        return this.roomsArround;
    }

    public boolean GetState(){
        return this.state;
    }

    public void IncrementRoomsArround(){
        this.roomsArround++;
    }
    public void IncrementVoidArround(){
        this.voidArround++;
    }
}
