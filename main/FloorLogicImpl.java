import java.util.HashMap;
import java.util.Map;

public class FloorLogicImpl {
    private Map<Integer, Floor> upperFloors;//x>=0
    private Map<Integer, Floor> lowerFloors;//x<0
    private int currentFloorNumber;

    public FloorLogicImpl() {
        this.currentFloorNumber = 0;
        this.upperFloors = new HashMap<Integer, Floor>();
        this.lowerFloors = new HashMap<Integer, Floor>();

        this.upperFloors.put(currentFloorNumber, new Floor());//floor 0
    }

    public Floor getCurrentFloor() {
        if(this.currentFloorNumber >= 0) {
            return this.upperFloors.get(this.currentFloorNumber);
        }else{
            return this.lowerFloors.get(this.currentFloorNumber);
        }
    }

    public Floor getFloor(int floor) {
        if(floor >= 0) {
            return this.upperFloors.get(floor);
        }else{
            return this.lowerFloors.get(floor);
        }
    }

    public int getUpperFloorsSize() {
        return this.upperFloors.size()-1;
    }

    public int getLowerFloorsSize() {
        return this.lowerFloors.size();
    }

    public int getCurrentFloorNumber() {
        return this.currentFloorNumber;
    }

    public void copyFloor(int nuovoPiano) {
        Floor newFloor = getFloor(nuovoPiano);
        newFloor.setFloor(this.getCurrentFloor());
        this.currentFloorNumber = nuovoPiano;
    }

    public void moveToLowerFloor() {
        this.currentFloorNumber--;
        createNewFloor();
    }

    public void moveToHigherFloor() {
        this.currentFloorNumber++;
        createNewFloor();
    }

    private void createNewFloor() {
    	if(this.currentFloorNumber == this.upperFloors.size()) {
            this.upperFloors.put(this.currentFloorNumber, new Floor());
        } else if(this.currentFloorNumber == -1-this.lowerFloors.size()) {
            this.lowerFloors.put(this.currentFloorNumber, new Floor());
        }
    }
}
