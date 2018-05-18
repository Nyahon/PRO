package Daryll.serialisation.data;

public class TimeSlot {

    private ClassRoom classroom = ClassRoom.NO_CLASSROOM;
    private long timeStart = 0;
    private long timeEnd = 0;

    public TimeSlot(){}
    
    public TimeSlot(String classroom, long timeStart) {
        this.classroom.setClassRoom( classroom );
        this.timeStart = timeStart;
    }
    
    public TimeSlot(String classroom, long timeStart, long timeEnd) {
        this.classroom.setClassRoom( classroom );
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public TimeSlot(ClassRoom classroom, long timeStart, long timeEnd) {
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

    public long getTime_start() {
        return timeStart;
    }

    public void setTime_start(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTime_end() {
        return timeEnd;
    }

    public void setTime_end(long timeEnd) {
        this.timeEnd = timeEnd;
    }
    
}
