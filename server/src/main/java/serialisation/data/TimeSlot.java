package serialisation.data;

public class TimeSlot {

    private ClassRoom classroom = ClassRoom.NO_CLASSROOM;
    private int timeStart = 0;
    private int timeEnd = 0;

    public TimeSlot(){}

    public TimeSlot(String classroom, int timeStart, int timeEnd) {
        this.classroom.setClassRoom( classroom );
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public TimeSlot(ClassRoom classroom, int timeStart, int timeEnd) {
        this.classroom = classroom;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getClassroom() {
        return classroom.getClassRoom();
    }

    public void setClassroom(String classroom) {
        this.classroom.setClassRoom( classroom );

    }

    public int getTime_start() {
        return timeStart;
    }

    public void setTime_start(int timeStart) {
        this.timeStart = timeStart;
    }

    public int getTime_end() {
        return timeEnd;
    }

    public void setTime_end(int timeEnd) {
        this.timeEnd = timeEnd;
    }
}
